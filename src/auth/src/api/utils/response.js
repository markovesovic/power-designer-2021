module.exports = {
	success: (data) => {
		if (!data) {
			data = {};
		}

		data.status = 'ok';

		return data;
	},

	error: (code, message, type, errors = null) => {
		const obj = {
			error: {
				code,
				type,
				message
			},
			status: 'fail'
		};

		if (errors != null) { // allow anything here, not just Arrays
			obj.errors = errors;
		}

		return obj;
	}
};
