const { v4: uuidv4 } = require('uuid');

exports.seed = (knex) => {
	// Deletes ALL existing entries
	return knex('roles').del()
		.then(function () {
			// Inserts seed entries
			return knex('roles').insert([
				{id: uuidv4(), name: 'admin'},
				{id: uuidv4(), name: 'maintainer'},
				{id: uuidv4(), name: 'member'}
			]);
		});
};
