module.exports = {
   NODE_ENV: 'development',
	// DATABASE_URL: 'mysql://admin:admin@localhost:3306',
	DATABASE_URL: process.env.DATABASE_URL,
	RQM_SERVICE_URL: process.env.RQM_SERVICE_URL
};
