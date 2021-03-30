const Response = require('@api/utils/response');
const errors = require('@common/errors');
const logger = require('@common/logger');

module.exports = {
	error,
	notImplemented,
	notFound,
	unknown
};

function error (err, req, res, next) {
	if (err instanceof errors.AuthenticationError || err.name === 'AuthenticationError') {
		res.status(401).json(Response.error(401, 'Unauthorized', err.name));
	}
	else if (err instanceof errors.AuthorizationError || err.name === 'AuthorizationError') {
		res.status(403).json(Response.error(403, 'Forbidden', err.name));
	}
	else if (err instanceof errors.ValidationError || err.name === 'ValidationError') {
		res.status(422).json(Response.error(422, 'Validation error', err.name, err.errors()));
	}
	else if (err instanceof errors.NotFoundError || err.name === 'NotFoundError') {
		res.status(404).json(Response.error(404, 'Not found', err.name));
	}
	else if (err instanceof errors.ConflictError || err.name === 'ConflictError') {
		res.status(409).json(Response.error(409, 'Conflict', err.name));
	}
	else {
		next(err);
	}
}

function notImplemented (_req, res) {
	res.send(501).end();
}

function notFound (req, _res, next) {
	if (!req.route) {
		next(new errors.NotFoundError());
	}
}

function unknown (err, _req, res, next) {
	logger.error(err.stack);

	res.status(500).json(Response.error(500, 'Internal Server Error', 'InternalServerError'));

	const hack = false;
	if (hack) { next(); }
}
