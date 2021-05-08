require('module-alias/register');
require('@config/init');

const logger = require('@common/logger');
const app = require('./app');

app.set('PORT', process.env.PORT || 5000);

app.listen(app.get('PORT'), () => {
	logger.info(`🚀  Running on the ${app.get('PORT')} port.`);
});
