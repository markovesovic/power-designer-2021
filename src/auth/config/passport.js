const JwtStrategy = require('passport-jwt').Strategy;
const ExtractJwt = require('passport-jwt').ExtractJwt;

const config = require('@config/index');
const { userService } = require('@common/services');

// load up the user model

module.exports = (passport) => {
	const opts = {
		jwtFromRequest: ExtractJwt.fromAuthHeaderWithScheme('JWT'),
		secretOrKey: config.getEnv('JWT_SECRET')
	};

	passport.use('jwt', new JwtStrategy(opts, async (jwtPayload, done) => {
		try {
			const user = await userService.getUser(jwtPayload.id);

			return done(null, {
				id: user.id,
				email: user.email
			});
		} catch (err) {
			return done(err, false);
		}
	}));
};
