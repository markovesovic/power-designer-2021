const tableName = 'models';

exports.up = function (knex) {
	return knex.schema.createTable(tableName, function (table) {
		table.uuid('project_id').notNullable();
		table.foreign('project_id').references('id').inTable('projects').onDelete('CASCADE');

		table.uuid('model_id').primary().defaultTo(knex.raw('(UUID())'));

        table.string('model_type').notNullable();
	});
};

exports.down = function (knex) {
	return knex.schema.dropTable(tableName);
};
