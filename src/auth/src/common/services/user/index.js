const bcrypt = require('bcrypt');

const { generateToken } = require('@api/auth');
const db = require('@common/db');
const {
	AuthenticationError,
	RegisterEmailConflict,
	UserNotFoundError
} = require('@common/errors');

module.exports = {
	getUser,
	createUser,
	login,
	register
};

async function getUser (id) {
	const user = await db('users')
		.select('*')
		.where({ id });

	return user[0];
}

async function getUserByEmail (email) {
	const user = await db('users')
		.select('*')
		.where({ email });

	return user[0];
}

async function createUser (user) {
	const hash = await bcrypt.hash(user.password, 10);

	await db('users')
		.insert({
			first_name: user.first_name,
			last_name: user.last_name,
			email: user.email,
			password: hash
		});
}

async function register (user) {
	const exist = await getUserByEmail(user.email);

	console.log(exist);

	if (!exist) {
		await createUser(user);

		const newUser = await getUserByEmail(user.email);

		// Generate new JWT token
		return generateToken({
			id: newUser.id,
			email: newUser.email
		});
	} else {
		throw new RegisterEmailConflict();
	}
}

async function login (email, password) {
	const user = await getUserByEmail(email);

	if (!user) {
		throw new UserNotFoundError();
	}

	const compare = await bcrypt.compare(password, user.password);

	if (!compare) {
		throw new AuthenticationError();
	}

	// Generate new JWT token
	return generateToken({
		id: user.id,
		email: user.email
	});
}
