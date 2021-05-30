const router = require('express').Router({ mergeParams: true });

const Response = require('@api/utils/response');
const { transformerService } = require('@common/services');

router.post('/rqm/to/use-case', (req, res, next) => {
	try {
		const rqmToUseCase = transformerService.rqmToUseCase(req.body.subRequests);

		res.status(200)
			.json(Response.success({
				cases: rqmToUseCase
			}))
			.end();
	} catch (err) {
		next(err);
	}
});

router.post('/class-model/to/java', (req, res, next) => {
	try {
		const classModelToJava = transformerService.classModelToJava(req.body.class_model);

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
