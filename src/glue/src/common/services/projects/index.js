const db = require('../../db');

module.exports = {
   getProjectsByTeamID,
   getProjectByUserID,
   createProject,
   deleteProjectByID,
   getAllModelsByProjectID,
}

async function getProjectsByTeamID (teamID) {
   const projects = await db('projects')
         .select('id')
         .where({ 'team_id': teamID });
   return projects;
}

async function getProjectByUserID (userID) {
   const projects = await db('projects')
         .select('id')
         .where({ 'user_id': userID });
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

async function deleteProjectByID (projectID) {
   await db('projects').del().where( { 'id': projectID } );

}

async function getAllModelsByProjectID (projectID) {

   const IDs = await db('models').select('model_id').where({ 'project_id': projectID });
   return IDs;
}
