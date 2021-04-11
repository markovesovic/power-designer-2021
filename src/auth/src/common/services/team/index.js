const {
	TeamNotFoundError,
	CreateTeamConflict,
	TeamMemberNotFoundError
} = require('@common/errors');

const query = require('./query');
const userService = require('../user');

module.exports = {
	createTeam,
	getTeam,
	deleteTeam,
	addTeamMember,
	getTeamMember,
	updateTeamMemberRole,
	deleteTeamMember
};

async function createTeam (userID, name) {
	const exist = await query.getTeamByName(name);

	if (exist) {
		throw new CreateTeamConflict();
	}

	const team = await query.createTeam(name);

	// Add creator as admin
	const role = await query.findRoleByName('admin');
	await query.addTeamMember(team.id, userID, role.id);

	return team;
}

async function getTeam (userID, teamID) {
	const [ team, teamMembers ] = await Promise.all([
		query.getTeam(teamID),
		query.getTeamMembers(teamID)
	]);

	const exist = teamMembers.filter(teamMember => teamMember.user_id === userID)[0];
	if (!team || !exist) {
		throw new TeamNotFoundError();
	}

	const members = await query.getTeamMembersDetails(teamID);

	return {
		team,
		members
	};
}

async function deleteTeam (userID, teamID) {
	const teamMembers = await query.getTeamMembers(teamID);

	const exist = teamMembers.filter(teamMember => teamMember.user_id === userID)[0];
	if (!exist) {
		throw new TeamNotFoundError();
	}

	return query.deleteTeam(teamID);
}

async function addTeamMember (userID, teamID, targetID, roleID) {
	const teamMembers = await query.getTeamMembers(teamID);

	const exist = teamMembers.filter(teamMember => teamMember.user_id === userID)[0];
	if (!exist) {
		throw new TeamNotFoundError();
	}

	const targetExist = userService.getUser(targetID);
	if (!targetExist) {
		throw new TeamMemberNotFoundError();
	}

	return query.addTeamMember(teamID, targetID, roleID);
}

function getTeamMember (userID, teamID) {
	return query.getTeamMember(userID, teamID);
}

async function updateTeamMemberRole (userID, teamID, targetID, roleID) {
	const teamMembers = await query.getTeamMembers(teamID);

	const exist = teamMembers.filter(teamMember =>
		teamMember.user_id === userID ||
		teamMember.user_id === targetID
	);

	if (!exist[0]) {
		throw new TeamNotFoundError();
	} else if (userID !== targetID && !exist[1]) {
		throw new TeamMemberNotFoundError();
	}

	await query.updateTeamMemberRole(teamID, targetID, roleID);
}

async function deleteTeamMember (userID, teamID, targetID) {
	const teamMembers = await query.getTeamMembers(teamID);

	const exist = teamMembers.filter(teamMember =>
		teamMember.user_id === userID ||
		teamMember.user_id === targetID
	);

	if (!exist[0]) {
		throw new TeamNotFoundError();
	} else if (userID !== targetID && !exist[1]) {
		throw new TeamMemberNotFoundError();
	}

	return query.deleteTeamMember(teamID, targetID);
}
