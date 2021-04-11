const router = require('express').Router();
const { projectService } = require('../../common/services');

router.use('/projects', require('./projects'));


module.exports = router;