const jwt = require('jsonwebtoken');
const passport = require('passport');
require('@config/passport')(passport);

const config = require('@config/index');
const { AuthenticationError } = require('@common/errors');

module.exports = {
	isAuth,
	getToken,
	generateToken
};

function isAuth (req, res, next) {
	try {
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
