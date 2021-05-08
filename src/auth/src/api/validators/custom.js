'use strict';

function isEqual (paramName) {
	async function equal (value, { req }) {
		if (value !== req.body[paramName]) {
			throw new Error();
		}
	}
	return equal;
}

module.exports = {
	isEqual
};
