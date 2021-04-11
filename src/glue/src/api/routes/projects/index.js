const { projectService } = require('../../../common/services');

const router = require('express').Router({ mergeParams: true });

/**
 * Projects
 */
router.get('/', async (req, res, next) => {
   try {
      // const userID = req.headers.user_id;
      // const teamID = req.headers.team_id;

   } catch (err) {
      next(err);
   }
});

router.get('/:id', async (req, res, next) => {
   try {
      // const response = await axios.get(`/zovi_nineta/${req.params.id}`, );

   } catch (err) {
      next(err);
   }
});

router.post('/', async (req, res, next) => {
   try {
      // const project = await projectService.createProject(req.body);
      
   } catch (err) {
      next(err);
   }
});


router.delete('/:id', async (req, res, next) => {
   try {
      await projectService.deleteProjectById(req.params.id);
      await axios.delete(`zovi_nineta/${req.params.id}`, );
   } catch (err) {
      next(err);
   }
});

router.patch('/:id', async (req, res, next) => {
   try {
      // await axios.put(`/zovi_nineta/${req.params.id}`, req.body);
   } catch (err) {
      next(err);
   }
}); 

/**
 * Models
 */

router.get('/:project_id/models/:model_id', async (req, res, next) => {
   try {
      // await axios.get(`ninetov_servis/rqm/${req.params.model_id}`, );
   } catch (err) {
      next(err);
   }
});

router.post('/:project_id/models', async (req, res, next) => {
   try {
         //
   } catch (err) {
      next(err);
   }
});

router.delete('/:project_id/models/:model_id', async (req, res, next) => {
   try {
      //
   } catch (err) {
      next(err);
   }
});

router.patch('/:project_id/models/:model_id', async (req, res, next) => {
   try {
      // 
   } catch (err) {
      next(err);
   }
});