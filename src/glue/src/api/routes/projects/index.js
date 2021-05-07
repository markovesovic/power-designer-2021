const router = require('express').Router({ mergeParams: true });
const got = require('got');

const Response = require('../../utils/response');
const { projectService } = require('../../../common/services');
const { RQM_SERVICE_URL,
	 USE_CASE_SERVICE_URL,
	  CLASS_MODEL_SERVICE_URL } = require('../../../../config');

/**
 * Projects
 */

router.get('/', async (req, res, next) => {
	try {
		const userID = req.headers.user_id;
		const teamID = req.headers.team_id;

		if (!teamID) {
			const projects = await projectService.getProjectsByUserID(userID);

			res.status(200)
				.json(Response.success({
					projects
				}))
				.end();
		} else {
			const projects = await projectService.getProjectsByTeamID(teamID);

			res.status(200)
				.json(Response.success({
					projects
				}))
				.end();
		}

	} catch (err) {
		next(err);
	}
});

router.get('/:project_id', projectService.checkProjectsByUser, async (req, res, next) => {
	try {
		const [ project, modelIDs ] = await Promise.all([
			projectService.getProjectByID(req.params.project_id),
			projectService.getAllModelsByProjectID(req.params.project_id)
		]);

		const promises = modelIDs.map(async model => {

			let modelRes;
			if(model.type == 'rqm') {
				modelRes = await got.get(`${RQM_SERVICE_URL}/rqm/${model.id}`);
			} else if(model.type == 'use_case') {
				modelRes = await got.get(`${USE_CASE_SERVICE_URL}/use-case/${model.id}`);
			} else if(model.type == 'class_model') {
				modelRes = await got.get(`${CLASS_MODEL_SERVICE_URL}/class-model/${model.id}`);
			}

			return JSON.parse(modelRes.body);
		});

		const models = await Promise.all(promises);

		res.status(200)
			.json(Response.success({
				project: project,
				models: models,
			}))
			.end();

	} catch (err) {
		next(err);
	}
});

router.post('/', async (req, res, next) => {
	try {
		const project = await projectService.createProject(req.headers.user_id, req.headers.team_id);

		res.status(200)
			.json(Response.success({
				project
			}))
			.end();
	} catch (err) {
		next(err);
	}
});

router.delete('/:project_id', projectService.checkProjectsByUser, async (req, res, next) => {
	try {
		const modelIDs = await projectService.getAllModelsByProjectID(req.params.project_id);

		await projectService.deleteProjectByID(req.params.project_id);

		const promises = modelIDs.map(async model => {
			
			if(model.type == 'rqm') {
				await got.delete(`${RQM_SERVICE_URL}/rqm/${model.id}`);
			} else if(model.type == 'use_case') {
				await got.delete(`${USE_CASE_SERVICE_URL}/use-case/${model.id}`);
			} else if(model.type == 'class_model') {
				await got.delete(`${CLASS_MODEL_SERVICE_URL}/class-model/${model.id}`);
			}

		});

		await projectService.deleteAllModelsByProjectID(req.params.project_id);

		await Promise.all(promises);

		res.status(200)
			.json(Response.success())
			.end();
	} catch (err) {
		next(err);
	}
});

router.patch('/:project_id', projectService.checkProjectsByUser, async (req, res, next) => {
	try {
		await projectService.updateProjectByID(req.params.project_id, req.body);
		const project = await projectService.getProjectByID(req.params.project_id);

		res.status(200)
			.json(Response.success({
				project
			}))
			.end();
	} catch (err) {
		next(err);
	}
});

router.use('/:project_id/models', require('./models'));

module.exports = router;
