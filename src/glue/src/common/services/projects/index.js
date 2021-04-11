const db = require('../../db');

module.exports = {
   getProjectsByTeamID,
   getProjectsByUserID,
   getProjectsByTeamAndUserID,
   getProjectByID,
   createProject,
   deleteProjectByID,
   updateProjectByID,

   getAllModelsByProjectID,
   createModel,
   deleteModel,

   checkProjectsByUser,
}

async function getProjectsByTeamID (teamID) {
   const projects = await db('projects')
         .select('id')
         .where({ team_id: teamID });
   return projects;
}

async function getProjectsByUserID (userID) {
   const projects = await db('projects')
         .select('*')
         .where({ user_id: userID });
   return projects;
}

async function getProjectsByTeamAndUserID (teamID, userID) {
   const projects = await db('projects')
            .select('id')
            .where({ team_id: teamID, user_id: userID });
   return projects;
}

async function createProject (project) {
   const insertedProject = await db('projects')
         .insert({
            team_id: project.team_id,
            user_id: project.user_id,
         });
   return insertedProject;
}

async function getProjectByID (id) {
   const project = await db('projects')
         .select('*')
         .where({ id: id });
   return project;
}

async function deleteProjectByID (projectID) {
   await db('projects').del().where( { id: projectID } );
}

async function updateProjectByID (project) {
   const id = project.id;
   const changes = project.changes;
   await db('projects').where({ id: id }).update(changes);
}

async function getAllModelsByProjectID (projectID) {

   const IDs = await db('models')
               .select('model_id')
               .where({ project_id: projectID });
   return IDs;
}

async function createModel (modelID, projectID, modelType) {
   await db('models').insert({
      model_id: modelID,
      project_id: projectID,
      model_type: modelType,
   });
}

async function deleteModel (modelID) {
   await db('models').del().where({ model_id: modelID });

}

async function checkProjectsByUser (req, res, next) {
   const userID = req.headers.user_id;
   const projectID = req.params.id;

   const user_id = db('projects')
            .select('user_id')
            .where({id: projectID});

   if(user_id === userID) {
      next();
   } else {
      res.send({
         message: 'User doesn\'t have access to given project',
      });
   }
}
