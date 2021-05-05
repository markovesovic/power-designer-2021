const { validationResult, matchedData } = require('express-validator');
const { ValidationError } = require('@common/errors');

const user = require('./user');

// Append validationError middleware to each validator array
function appendValidationError (validators) {
	let newValidators = {};
	Object.keys(validators).map(function (valName) {
		newValidators[valName] = [ validators[valName], validationError ];
	});
	return newValidators;
}

// Catch validation errors
function validationError (req, res, next) {
	const errors = validationResult(req);

	if (!errors.isEmpty()) {
		next(new ValidationError(errors.mapped()));
	} else {
		req.query = matchedData(req, { locations: ['query'] });
		req.body = matchedData(req, { locations: ['body'] });
		next();
	}
}

module.exports = {
	user: appendValidationError(user)
};
