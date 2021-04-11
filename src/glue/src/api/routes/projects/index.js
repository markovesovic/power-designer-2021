const { projectService } = require('../../../common/services');
const { RQM_SERVICE_URL } = require('../../../../config');

const router = require('express').Router({ mergeParams: true });

/**
 * Projects
 */
router.get('/', async (req, res, next) => {
   try {
      const userID = req.headers.user_id;
      const teamID = req.headers.team_id;
      if(team_id == null) {
         const projects = projectService.getProjectsByUserID(userID);
         res.status(200).send({
            projects: projects,
         });
      } else {
         const projects = projectService.getProjectsByTeamAndUserID(teamID, userID);
         res.status(200).send({
            projects: projects,
         });
      }

   } catch (err) {
      next(err);
   }
});

router.get('/:id', projectService.checkProjectsByUser, async (req, res, next) => {
   try {
      const project = projectService.getProjectByID(req.params.id);
      const modelIDs = projectService.getAllModelsByProjectID(req.params.id);

      let promises = [];
      modelIDs.foreach(modelID => {
         const promise = axios.get(`${RQM_SERVICE_URL}/rqm/${modelID}`, );
         promises.push(promise);
      });
      const models = Promise.all(promises);

      res.send({
         project: project,
         models: models,
      });

   } catch (err) {
      next(err);
   }
});

router.post('/', async (req, res, next) => {
   try {
      const project = await projectService.createProject(req.body);

      res.send({
         project: project,
      });
   } catch (err) {
      next(err);
   }
});

router.delete('/:id', projectService.checkProjectsByUser, async (req, res, next) => {
   try {
      let promises = [];
      const promise = projectService.deleteProjectById(req.params.id);
      promises.push(promise);
      const ids = projectService.getAllModelsByProjectID(req.params.id);
      ids.foreach(id => {
         const p = await axios.delete(`${RQM_SERVICE_URL}/rqm/${id}`, );
         promises.push(p);
      });
      Promise.all(promises);
      res.status(200);

   } catch (err) {
      next(err);
   }
});

router.patch('/:id', projectService.checkProjectsByUser, async (req, res, next) => {
   try {
      const count = await projectService.updateProjectByID({
         id: req.params.id,
         changes: req.body,
      });
      if(count) {
         res.send(200);
      } else {
         res.send({
            message: 'Project not found',
         });
      }
   } catch (err) {
      next(err);
   }
}); 

/**
 * Models
 */

router.get('/:project_id/models/:model_id', projectService.checkProjectsByUser, async (req, res, next) => {
   try {
      const model = await axios.get(`${RQM_SERVICE_URL}/rqm/${req.params.model_id}`, );
      res.send({
         model: model,
      });
   } catch (err) {
      next(err);
   }
});

router.post('/:project_id/models', projectService.checkProjectsByUser, async (req, res, next) => {
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

router.delete('/:project_id/models/:model_id', projectService.checkProjectsByUser, async (req, res, next) => {
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

router.patch('/:project_id/models/:model_id', async (req, res, next) => {
   try {
      //
   } catch (err) {
      next(err);
   }
});
