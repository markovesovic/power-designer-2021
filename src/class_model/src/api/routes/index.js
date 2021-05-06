const router = require('express').Router({ mergeParams: true });

const Response = require('@api/utils/response');
const { classModelService } = require('@common/services');

router.post('/class-model', async (req, res, next) => {
	try {
		const model = await classModelService.addModel(req.body);

		res.status(200)
			.json(Response.success({
				id: model.id,
				version: model.version
			}))
			.end();
	} catch (err) {
		next(err);
	}
});

router.get('/class-model/:id', async (req, res, next) => {
	try {
		const id = req.params.id;
		const version = +req.query.version;

		const classModel = await classModelService.getModel(id, version);

		res.status(200)
			.json(Response.success(classModel))
			.end();
	} catch (err) {
		next(err);
	}
});

router.delete('/class-model/:id', async (req, res, next) => {
	try {
		const id = req.params.id;

		await classModelService.deleteModel(id);

		res.status(200)
			.json(Response.success())
			.end();
	} catch (err) {
		next(err);
	}
});

module.exports = router;
