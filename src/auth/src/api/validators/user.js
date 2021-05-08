const { body } = require('express-validator');
const custom = require('./custom');

const register = [
	body('first_name')
		.trim()
		.not().isEmpty().withMessage('first_name can\'t be empty'),
	body('last_name')
		.trim()
		.not().isEmpty().withMessage('last_name can\'t be empty'),
	body('email')
		.trim()
		.not().isEmpty().withMessage('email can\'t be empty')
		.isEmail().withMessage('invalid email'),
	body('password')
		.not().isEmpty().withMessage('password_req can\'t be empty')
		.isLength(6).withMessage('password must have more then 6 characters'),
	body('password_confirm')
		.not().isEmpty().withMessage('password_confirm can\'t be empty')
		.custom(custom.isEqual('password')).withMessage('passwords not equal')
];

const login = [
	body('email')
		.trim()
		.not().isEmpty().withMessage('email can\'t be empty')
		.isEmail().withMessage('invalid email'),
	body('password')
		.not().isEmpty().withMessage('password_req can\'t be empty')
		.isLength(6).withMessage('password must have more then 6 characters'),
];

module.exports = {
	register,
	login
};
