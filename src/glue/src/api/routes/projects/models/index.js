const router = require('express').Router({ mergeParams: true });
const got = require('got');

const Response = require('../../../utils/response');

const { projectService,
		  modelValidationService } = require('../../../../common/services');

const { RQM_SERVICE_URL,
	 	  USE_CASE_SERVICE_URL,
	  	  CLASS_MODEL_SERVICE_URL } = require('../../../../../config');

/**
 * Models
 */
router.post('/', projectService.checkProjectsByUser, modelValidationService.validateModel, async (req, res, next) => {
	try {
		let modelService;
		let modelServiceRoute;

		if(req.body.model_type == 'rqm') {
			modelService = RQM_SERVICE_URL;
			modelServiceRoute = 'rqm';

		} else if(req.body.model_type == 'use_case') {
			modelService = USE_CASE_SERVICE_URL;
			modelServiceRoute = 'use-case';

		} else if(req.body.model_type == 'class_model') {
			modelService = CLASS_MODEL_SERVICE_URL;
			modelServiceRoute = 'class-model';
		}

		const modelRes = await got.post(`${modelService}/${modelServiceRoute}`, {
			headers: {
				'content-type': 'application/json'
			},
			body: JSON.stringify(req.body)
		});

		if (!req.body.id) {
			await projectService.createModel(JSON.parse(modelRes.body).id, req.params.project_id, req.body.model_type);
		}

		res.status(200)
			.end(modelRes.body);
	} catch (err) {
		console.error(err);
		next(err);
	}
});

router.get('/:model_id', projectService.checkProjectsByUser, async (req, res, next) => {
	try {

		let modelRes;
		const model = await projectService.getModelByID(req.params.model_id);


		if(model.model_type == 'rqm') {
			modelRes = await got.get(`${RQM_SERVICE_URL}/rqm/${req.params.model_id}?version=${+req.query.version}`);
		} else if(model.model_type == 'use_case') {
			modelRes = await got.get(`${USE_CASE_SERVICE_URL}/use-case/${req.params.model_id}?version=${+req.query.version}`);
		} else if(model.model_type == 'class_model') {
			modelRes = await got.get(`${CLASS_MODEL_SERVICE_URL}/class-model/${req.params.model_id}?version=${+req.query.version}`);
		}

		res.status(200)
			.end(modelRes.body);
	} catch (err) {
		next(err);
	}
});

router.delete('/:model_id', projectService.checkProjectsByUser, async (req, res, next) => {
	try {
		await projectService.deleteModel(req.params.model_id);

		if(req.body.model_type == 'rqm') {
			await got.delete(`${RQM_SERVICE_URL}/rqm/${req.params.model_id}`);
		} else if(req.body.model_type == 'use_case') {
			await got.delete(`${USE_CASE_SERVICE_URL}/use-case/${req.params.model_id}`);
		} else if(req.body.model_type == 'class_model') {
			await got.delete(`${CLASS_MODEL_SERVICE_URL}/class-model/${req.params.model_id}`);
		}

		res.status(200)
			.json(Response.success())
			.end();
	} catch (err) {
		next(err);
	}
});

module.exports = router;
