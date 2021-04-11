const knexConfig = {
	client: 'mysql',
	connection: process.env.DATABASE_URL,
	migrations: {
		directory: '../src/common/db/migrations'
	}
};

module.exports = {
   NODE_ENV: 'development',
	DATABASE_URL: 'mysql://admin:admin@localhost:3306',
	// DATABASE_URL: 'mysql://root:password@localhost:3306/auth',
	production: knexConfig,
	staging: knexConfig,
	development: knexConfig
};
