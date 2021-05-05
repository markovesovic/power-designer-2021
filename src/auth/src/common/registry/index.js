const dataman = require('dataman');

const config = require('@config/index');
const storage = dataman.storage(config.get('REGISTRY_URL'));

const registry = {
	blacklistToken: async (token) => {
		await storage.primitive.set(`blacklisted:${token}`, 1);
		await storage.primitive.expire(`blacklisted:${token}`, 24 * 60 * 60);
	},

	IsBlacklisted: async (token) => {
		const exist = await storage.primitive.get(`blacklisted:${token}`, 1);

		return !!exist;
	}
};

module.exports = registry;

