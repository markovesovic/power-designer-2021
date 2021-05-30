class AuthenticationError extends Error {
	constructor() {
		super();
		this.name = 'AuthenticationError';
	}
}

class AuthorizationError extends Error {
	constructor() {
		super();
		this.name = 'AuthorizationError';
	}
}

class ValidationError extends Error {
	constructor (mapped) {
		super();

		const val = {};
		Object.keys(mapped).forEach(key => {
			val[key] = {
				location: mapped[key].location,
				value: mapped[key].value,
				message: mapped[key].msg
			};
		});

		this.mapped = val;
		this.name = 'ValidationError';
	}

	errors () {
		return this.mapped;
	}
}

class NotFoundError extends Error {
	constructor() {
		super();
		this.name = 'NotFoundError';
	}
}


class ConflictError extends Error {
	constructor() {
		super();
		this.name = 'ConflictError';
	}
}

module.exports = {
	AuthenticationError,
	AuthorizationError,
	ValidationError,
	NotFoundError,
	ConflictError,
};
