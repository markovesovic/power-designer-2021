const config = {
	_defaults: undefined,
	_config: {},

	set: (name, value) => {
		return config[name] = value;
	},

	get: (name) => {
		return config.set(name, config._config[name] || config.getEnv(name) || config.getDefault(name));
	},

	setDefaults: (defaults) => {
		if (!config._defaults) {
			config._defaults = defaults || require('./defaults');
		}

		return config._defaults;
	},

	getDefault: (name) => {
		return config.setDefaults()[name];
	},

	getEnv: (name) => {
		return process.env[name];
	}
};

module.exports = config;
