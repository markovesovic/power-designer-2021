const router = require('express').Router({ mergeParams: true });
const got = require('got');

const config = require('@config/index');
const auth = require('@api/auth');
const val = require('@api/validators');
const { TeamNotFoundError } = require('@common/errors');
const Response = require('@api/utils/response');
const { userService, teamService } = require('@common/services');

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

router.use(auth.isAuth, (req, res, next) => {
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

// Proxy
router.use(auth.isAuth, async (req, res, next) => {
	try {
		const teamID = req.headers.team_id;

		if (teamID) {
			const exist = await teamService.getTeamMember(req.user_id, teamID);

			if (!exist) {
				throw new TeamNotFoundError();
			}
		}

		const headers = JSON.parse(JSON.stringify(req.headers));
		headers.user_id = req.user_id;

		if (req.method !== 'GET') {
			headers['content-type'] = 'application/json';
			headers['content-length'] = '' + JSON.stringify(req.body).length;
		}

		delete headers.host;

		let promise;

		if (req.method === 'GET') {
			promise = got.get(config.get('GLUE_HOST') + req.originalUrl, {
				headers: headers
			});
		}
		else if (req.method === 'POST') {
			promise = got.post(config.get('GLUE_HOST') + req.originalUrl, {
				headers: headers,
				body: JSON.stringify(req.body)
			});
		}
		else if (req.method === 'PUT') {
			promise = got.put(config.get('GLUE_HOST') + req.originalUrl, {
				headers: headers,
				body: JSON.stringify(req.body)
			});
		}
		else if (req.method === 'PATCH') {
			promise = got.patch(config.get('GLUE_HOST') + req.originalUrl, {
				headers: headers,
				body: JSON.stringify(req.body)
			});
		}
		else if (req.method === 'DELETE') {
			promise = got.delete(config.get('GLUE_HOST') + req.originalUrl, {
				headers: headers,
				body: JSON.stringify(req.body)
			});
		} else {
			next();
			return;
		}

		try {
			const response = await promise;

			res.status(response.statusCode)
				.end(response.body);
		} catch (err) {
			res.status(err.response.statusCode)
				.end(err.response.body);
		}
	} catch (err) {
		next(err);
	}
});

module.exports = router;
