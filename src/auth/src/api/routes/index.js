const router = require('express').Router({ mergeParams: true });

const auth = require('@api/auth');
const Response = require('@api/utils/response');
const { userService } = require('@common/services');

router.post('/register', async (req, res, next) => {
	try {
		const token = await userService.register(req.body);

		res.status(200)
			.json(Response.success({
				token
			})).end();
	} catch (err) {
		next(err);
	}
});

router.post('/login', async (req, res, next) => {
	try {
		const { email, password } = req.body;

		const token = await userService.login(email, password);

		res.status(200)
			.json(Response.success({
				token
			})).end();
	} catch (err) {
		next(err);
	}
});

router.post('/logout', async (req, res, next) => {
	try {
		await auth.blacklistToken(req);

		res.status(200)
			.json(Response.success())
			.end();
	} catch (err) {
		next(err);
	}
});

module.exports = router;
