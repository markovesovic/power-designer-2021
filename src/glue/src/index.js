require('dotenv').config();
require('../config/knexfile');

const app = require('./app');
const logger = require('./common/logger');

app.set('PORT', process.env.PORT || 5000);
logger.info(process.env.DATABASE_URL);

app.listen(app.get('PORT'), () => {
	logger.info(`Running on the ${app.get('PORT')} port.`);
});

module.exports = {};
