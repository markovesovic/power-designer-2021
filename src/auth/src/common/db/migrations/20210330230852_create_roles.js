const tableName = 'roles';

exports.up = function (knex) {
	return knex.schema.createTable(tableName, function (table) {
		// default UUID without trigger is only possible on MySQL > 8.0.
		table.uuid('id').primary().defaultTo(knex.raw('(UUID())'));

		table.string('name').notNullable();

		table.dateTime('created_at').notNullable().defaultTo(knex.raw('CURRENT_TIMESTAMP'));
		table.dateTime('updated_at').notNullable().defaultTo(knex.raw('CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP'));
	});
};

exports.down = function (knex) {
	return knex.schema.dropTable(tableName);
};
