const jwt = require('jsonwebtoken');
const passport = require('passport');
require('@config/passport')(passport);

const config = require('@config/index');
const registry = require('@common/registry');
const { AuthenticationError } = require('@common/errors');

module.exports = {
	isAuth,
	getToken,
	getJWTPeyload,
	generateToken,
	blacklistToken
};

async function isAuth (req, res, next) {
	try {
		const token = getToken(req.headers);

		const blacklisted = await registry.IsBlacklisted(token);

		if (blacklisted) {
			next(new AuthenticationError());

			return;
		}

		const authorization = passport.authenticate('jwt', { session: false });

		return authorization(req, res, next);
	} catch (err) {
		next(new AuthenticationError());
	}
}

function getToken (headers) {
	if (headers && headers.authorization) {
		const parted = headers.authorization.split('JWT ');

		if (parted.length === 2) {
			return parted[1];
		}
	}
}

function getJWTPeyload (token) {
	try {
		const decoded = jwt.verify(token, config.getEnv('JWT_SECRET'));

		return decoded;
	} catch (err) {
		throw new AuthenticationError();
	}
}

function generateToken (payload) {
	const token = jwt.sign(
		{
			id: payload.id,
			email: payload.email
		},
		config.getEnv('JWT_SECRET'),
		{
			expiresIn: '1d',
		}
	);

	return token;
}

function blacklistToken (req) {
	const token = getToken(req.headers);

	return registry.blacklistToken(token);
}
