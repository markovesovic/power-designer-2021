const router = require('express').Router({ mergeParams: true });

const auth = require('@api/auth');
const val = require('@api/validators');
const Response = require('@api/utils/response');
const { userService } = require('@common/services');

router.post('/register', val.user.register, async (req, res, next) => {
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

router.post('/login', val.user.login, async (req, res, next) => {
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

router.post('/logout', auth.isAuth, async (req, res, next) => {
	try {
		await auth.blacklistToken(req);

		res.status(200)
			.json(Response.success())
			.end();
	} catch (err) {
		next(err);
	}
});

router.use((req, res, next) => {
	try {
		const token = auth.getToken(req.headers);
		const payload = auth.getJWTPeyload(token);

		req.user_id = payload.id;
		req.email = payload.email;

		next();
	} catch (err) {
		next(err);
	}
});

router.use('/teams', auth.isAuth, require('./teams'));

module.exports = router;
