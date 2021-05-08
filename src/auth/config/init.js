require('dotenv').config();

// Init bluebird
global.Promise = require('bluebird');

Promise.config({
	warnings: false
});
