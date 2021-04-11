const path = require('path');

require('module-alias/register');
require('dotenv').config({
	path: path.resolve(path.join(process.cwd(), '..'), '.env')
});

const knexConfig = {
	client: 'mysql',
	connection: process.env.DATABASE_URL,
	migrations: {
		directory: '../src/common/db/migrations'
	},
	seeds: {
		directory: '../src/common/db/seeds'
	}
};

module.exports = {
	production: knexConfig,
	staging: knexConfig,
	development: knexConfig
};
