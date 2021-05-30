const express = require('express');
const bodyParser = require('body-parser');

const handler = require('@api/handlers');

const app = express();

app.use(bodyParser.json());

app.use('/api/v1', require('@api/routes'));

// error handling
app.use(handler.notFound);
app.use(handler.error);

app.use(handler.unknown);

module.exports = app;
