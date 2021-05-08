const tableName = 'projects';

exports.up = function (knex) {
	return knex.schema.createTable(tableName, function (table) {
		table.uuid('id').primary().defaultTo(knex.raw('(UUID())'));

		table.uuid('user_id').notNullable();
		table.uuid('team_id').nullable();

		table.dateTime('created_at').notNullable().defaultTo(knex.raw('CURRENT_TIMESTAMP'));
		table.dateTime('updated_at').notNullable().defaultTo(knex.raw('CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP'));
	});
};

exports.down = function (knex) {
	return knex.schema.dropTable(tableName);
};

/*
    models
    project_id
    model_id
    model_type
*/