const router = require('express').Router({ mergeParams: true });

const { projectService } = require('../../../../common/services');
const { RQM_SERVICE_URL } = require('../../../../../config');

/**
 * Models
 */
router.post('/', projectService.checkProjectsByUser, async (req, res, next) => {
    try {
        const modelID = await axios.post(`${RQM_SERVICE_URL}/rqm`, {rqm: req.body.rqm});
        const model = await projectService.createModel(modelID, req.params.project_id, req.body.model_type);

        res.send({
            model: model,
        });
    } catch (err) {
        next(err);
    }
});

router.get('/:model_id', projectService.checkProjectsByUser, async (req, res, next) => {
    try {
        const model = await axios.get(`${RQM_SERVICE_URL}/rqm/${req.params.model_id}`, );
        res.send({
            model: model,
        });
    } catch (err) {
        next(err);
    }
});

router.patch('/:model_id', async (req, res, next) => {
    try {
        //
    } catch (err) {
        next(err);
    }
});
 
router.delete('/:model_id', projectService.checkProjectsByUser, async (req, res, next) => {
    try {
        await projectService.deleteModel(req.params.model_id);
        const response = await axios.delete(`${RQM_SERVICE_URL}/rqm/${req.params.model_id}`, );

        res.send({
            response: response,
        });
    } catch (err) {
        next(err);
    }
});

module.exports = router;
