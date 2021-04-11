const { v4: uuidv4 } = require('uuid');

const { ProjectNotFoundError } = require('../../errors');
const db = require('../../db');

module.exports = {
	getProjectsByTeamID,
	getProjectsByUserID,
	getProjectByID,
	createProject,
	deleteProjectByID,
	updateProjectByID,

	getAllModelsByProjectID,
	createModel,
	deleteModel,

	checkProjectsByUser,
};

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

async function createProject (userID, teamID) {
	const id = uuidv4();

	await db('projects')
		.insert({
			id,
			user_id: userID,
			team_id: teamID || null
		});

	return { id };
}

async function getProjectByID (id) {
	const project = await db('projects')
		.select('*')
		.where({ id });

	if (!project[0]) {
		throw new ProjectNotFoundError();
	}

	return project[0];
}

async function deleteProjectByID (projectID) {
	await db('projects')
		.where({ id: projectID })
		.del();
}

async function updateProjectByID (id, changes) {
	await db('projects')
		.where({ id: id })
		.update(changes);
}

async function getAllModelsByProjectID (projectID) {
	const IDs = await db('models')
		.select('model_id AS id')
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
	await db('models')
		.where({ model_id: modelID })
		.del();
}

async function checkProjectsByUser (req, res, next) {
	const userID = req.headers.user_id;
	const projectID = req.params.project_id;

	const project = await db('projects')
		.select('user_id')
		.where({ id: projectID });

	if (project[0] && project[0].user_id === userID) {
		next();
	} else {
		res.status(400).send({
			message: 'User doesn\'t have access to given project',
		});
	}
}
