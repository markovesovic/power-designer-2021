const tableName = 'members';

exports.up = function (knex) {
	return knex.schema.createTable(tableName, function (table) {
		// default UUID without trigger is only possible on MySQL > 8.0.
		table.uuid('id').primary().defaultTo(knex.raw('(UUID())'));

		table.uuid('team_id').notNullable();
		table.foreign('team_id').references('id').inTable('teams').onDelete('CASCADE');

		table.uuid('user_id').notNullable();
		table.foreign('user_id').references('id').inTable('users').onDelete('CASCADE');

		table.uuid('role_id').notNullable();
		table.foreign('role_id').references('id').inTable('roles').onDelete('CASCADE');
	});
};

exports.down = function (knex) {
	return knex.schema.dropTable(tableName);
};
