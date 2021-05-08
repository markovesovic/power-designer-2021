const { v4: uuidv4 } = require('uuid');

const db = require('@common/db');

module.exports = {
	createTeam,
	getTeam,
	getTeamByName,
	deleteTeam,
	getTeamMember,
	getTeamMembers,
	getTeamMembersDetails,
	addTeamMember,
	updateTeamMemberRole,
	deleteTeamMember,
	findRoleByName
};

async function createTeam (name) {
	const id = uuidv4();

	await db('teams')
		.insert({
			id,
			name
		});

	return { id };
}

async function getTeam (teamID) {
	const team = await db('teams')
		.select('*')
		.where({ id: teamID });

	return team[0];
}

async function getTeamByName (name) {
	const team = await db('teams')
		.select('*')
		.where({ name });

	return team[0];
}

async function deleteTeam (teamID) {
	await db('teams')
		.where({ id: teamID })
		.del();
}

async function getTeamMember (userID, teamID) {
	const member = await db('members')
		.select('*')
		.where({
			user_id: userID,
			team_id: teamID
		});

	return member[0];
}

async function getTeamMembers (teamID) {
	return db('members')
		.select('*')
		.where({ team_id: teamID });
}

async function getTeamMembersDetails (teamID) {
	return db
		.select([
			'users.first_name AS first_name',
			'users.last_name AS last_name',
			'users.email AS email',
			'roles.name AS role'
		])
		.from('members')
		.join('users', {'users.id': 'members.user_id'})
		.join('roles', {'roles.id': 'members.role_id'})
		.where({ team_id: teamID });
}

async function addTeamMember (teamID, userID, roleID) {
	await db('members')
		.insert({
			id: uuidv4(),
			team_id: teamID,
			user_id: userID,
			role_id: roleID
		});
}

async function updateTeamMemberRole (teamID, userID, roleID) {
	await db('members')
		.where({
			team_id: teamID,
			user_id: userID,
		})
		.update({ role_id: roleID });
}

async function deleteTeamMember (teamID, userID) {
	await db('members')
		.where({
			team_id: teamID,
			user_id: userID,
		})
		.del();
}

async function findRoleByName (name) {
	const role = await db('roles')
		.select('*')
		.where({ name });

	return role[0];
}
