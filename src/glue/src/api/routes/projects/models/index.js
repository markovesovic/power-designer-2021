const router = require('express').Router({ mergeParams: true });
const got = require('got');

const Response = require('../../../utils/response');
const { projectService } = require('../../../../common/services');
const { RQM_SERVICE_URL } = require('../../../../../config');

/**
 * Models
 */
router.post('/', projectService.checkProjectsByUser, async (req, res, next) => {
	try {
		const res = await got.post(`${RQM_SERVICE_URL}/rqm`, {
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(req.body)
		});

		const model = await projectService.createModel(JSON.parse(res.body).id, req.params.project_id, req.body.model_type);

		res.status(200)
			.json(Response.success({
				model,
			}))
			.end();
	} catch (err) {
		next(err);
	}
});

router.get('/:model_id', projectService.checkProjectsByUser, async (req, res, next) => {
	try {
		const res = await got.get(`${RQM_SERVICE_URL}/rqm/${req.params.model_id}`);

		res.status(200)
			.end(res.body);
	} catch (err) {
		next(err);
	}
});

router.patch('/:model_id', async (req, res, next) => {
	try {
		// TO-DO: Implement

		res.status(200)
			.json(Response.success())
			.end();
	} catch (err) {
		next(err);
	}
});

router.delete('/:model_id', projectService.checkProjectsByUser, async (req, res, next) => {
	try {
		await projectService.deleteModel(req.params.model_id);
		await got.delete(`${RQM_SERVICE_URL}/rqm/${req.params.model_id}`);

		res.status(200)
			.json(Response.success())
			.end();
	} catch (err) {
		next(err);
	}
});

module.exports = router;
