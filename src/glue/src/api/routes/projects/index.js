const router = require('express').Router({ mergeParams: true });
const got = require('got');

const Response = require('../../utils/response');
const { ProjectNotFoundError } = require('../../../common/errors');
const { projectService } = require('../../../common/services');
const { RQM_SERVICE_URL } = require('../../../../config');

/**
 * Projects
 */

router.get('/', async (req, res, next) => {
	res.send(200);
	try {
		const userID = req.headers.user_id;
		const teamID = req.headers.team_id;
		if(teamID == null) {
			const projects = projectService.getProjectsByUserID(userID);

			res.status(200)
				.json(Response.success({
					projects,
				}))
				.end();
		} else {
			const projects = projectService.getProjectsByTeamID(teamID);
			res.status(200)
				.json(Response.success({
					projects: projects,
				}))
				.end();
		}

	} catch (err) {
		next(err);
	}
});

router.get('/:project_id', projectService.checkProjectsByUser, async (req, res, next) => {
	try {
		const project = projectService.getProjectByID(req.params.project_id);
		const modelIDs = projectService.getAllModelsByProjectID(req.params.project_id);

		const promises = modelIDs.map(async modelID => {
			const rqmRes = await got.get(`${RQM_SERVICE_URL}/rqm/${modelID}`);

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
		const project = await projectService.createProject(req.body);

		res.status(200)
			.json(Response.success({
				project: project,
			}))
			.end();
	} catch (err) {
		next(err);
	}
});

router.delete('/:project_id', projectService.checkProjectsByUser, async (req, res, next) => {
	try {
		await projectService.deleteProjectById(req.params.project_id);

		const ids = projectService.getAllModelsByProjectID(req.params.project_id);
		const promises = ids.map(async id => {
			await got.delete(`${RQM_SERVICE_URL}/rqm/${id}`);
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
		const count = await projectService.updateProjectByID({
			id: req.params.project_id,
			changes: req.body,
		});

		if(count) {
			res.status(200)
				.json(Response.success())
				.end();
		} else {
			throw new ProjectNotFoundError();
		}
	} catch (err) {
		next(err);
	}
});

router.use('/:project_id/models', require('./models'));

module.exports = router;
