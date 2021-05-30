module.exports = {
   NODE_ENV: 'development',
	// DATABASE_URL: 'mysql://admin:admin@localhost:3306',
	DATABASE_URL: process.env.DATABASE_URL,
	RQM_SERVICE_URL: process.env.RQM_SERVICE_URL,
	USE_CASE_SERVICE_URL: process.env.USE_CASE_SERVICE_URL,
	CLASS_MODEL_SERVICE_URL: process.env.CLASS_MODEL_SERVICE_URL,
	GENERATOR_SERVICE_URL: process.env.GENERATOR_SERVICE_URL
};
