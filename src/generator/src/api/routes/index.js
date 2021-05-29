const router = require('express').Router({ mergeParams: true });

const Response = require('@api/utils/response');
const { transformerService } = require('@common/services');

router.post('/rqm/to/use-case', async (req, res, next) => {
	try {
		const rqmToUseCase = await transformerService.rqmToUseCase(req.body);

		res.status(200)
			.json(Response.success({
				generated_code: rqmToUseCase
			}))
			.end();
	} catch (err) {
		next(err);
	}
});

router.post('/class-model/to/java', async (req, res, next) => {
	try {
		const classModelToJava = await transformerService.classModelToJava(req.body.class_model);

		res.status(200)
			.json(Response.success({
				files: classModelToJava
			}))
			.end();
	} catch (err) {
		next(err);
	}
});

module.exports = router;
