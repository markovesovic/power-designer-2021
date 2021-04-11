require('dotenv').config();

const app = require('./app');
const logger = require('./common/logger');

app.set('PORT', process.env.PORT || 5000);

app.listen(app.get('PORT'), () => {
	logger.info(`Running on the ${app.get('PORT')} port.`);
});

module.exports = {};