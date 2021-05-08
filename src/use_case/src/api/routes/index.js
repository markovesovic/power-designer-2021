const router = require('express').Router({ mergeParams: true });

const Response = require('@api/utils/response');
const { useCaseService } = require('@common/services');

router.post('/use-case', async (req, res, next) => {
	try {
		const model = await useCaseService.addModel(req.body);

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

router.get('/use-case/:id', async (req, res, next) => {
	try {
		const id = req.params.id;
		const version = +req.query.version;

		const classModel = await useCaseService.getModel(id, version);

		res.status(200)
			.json(Response.success(classModel))
			.end();
	} catch (err) {
		next(err);
	}
});

router.delete('/use-case/:id', async (req, res, next) => {
	try {
		const id = req.params.id;

		await useCaseService.deleteModel(id);

		res.status(200)
			.json(Response.success())
			.end();
	} catch (err) {
		next(err);
	}
});

module.exports = router;
