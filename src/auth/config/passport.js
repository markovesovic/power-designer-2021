const JwtStrategy = require('passport-jwt').Strategy;
const ExtractJwt = require('passport-jwt').ExtractJwt;

const config = require('@config/index');
const db = require('@common/db');

module.exports = (passport) => {
	const opts = {
		jwtFromRequest: ExtractJwt.fromAuthHeaderWithScheme('JWT'),
		secretOrKey: config.getEnv('JWT_SECRET')
	};

	passport.use('jwt', new JwtStrategy(opts, async (jwtPayload, done) => {
		try {
			const user = await db('users')
				.select('*')
				.where({ id: jwtPayload.id });

			return done(null, {
				id: user[0].id,
				email: user[0].email
			});
		} catch (err) {
			return done(err, false);
		}
	}));
};
