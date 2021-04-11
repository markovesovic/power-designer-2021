const router = require('express').Router({ mergeParams: true });
const got = require('got');

const Response = require('../../utils/response');
const { projectService } = require('../../../common/services');
const { RQM_SERVICE_URL } = require('../../../../config');

/**
 * Projects
 */

router.get('/', async (req, res, next) => {
	try {
		const userID = req.headers.user_id;
		const teamID = req.headers.team_id;

		if (!teamID) {
			const projects = projectService.getProjectsByUserID(userID);

			res.status(200)
				.json(Response.success({
					projects
				}))
				.end();
		} else {
			const projects = projectService.getProjectsByTeamID(teamID);

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
			const rqmRes = await got.get(`${RQM_SERVICE_URL}/rqm/${model.id}`);

			return JSON.parse(rqmRes.body);
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
		await projectService.deleteProjectByID(req.params.project_id);

		const modelIDs = await projectService.getAllModelsByProjectID(req.params.project_id);
		const promises = modelIDs.map(async model => {
			await got.delete(`${RQM_SERVICE_URL}/rqm/${model.id}`);
		});

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
