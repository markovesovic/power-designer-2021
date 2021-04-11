const Knex = require('knex');

const config = require('../../../config');

const knex = Knex({
	client: 'mysql',
	connection: config.DATABASE_URL,
	pool: {
		min: 3,
		max: 10
	}
});

module.exports = knex;
