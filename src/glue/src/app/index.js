const express = require('express');
const router = require('../api/routes');
const handler = require('../api/handlers');

const app = express();

app.use(express.json());

app.use('/api/v1', router);

app.use(handler.notFound);
app.use(handler.error);

app.use(handler.unknown);

module.exports = app;
