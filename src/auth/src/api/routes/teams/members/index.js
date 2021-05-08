const router = require('express').Router({ mergeParams: true });

const Response = require('@api/utils/response');
const { teamService } = require('@common/services');

router.post('/:target', async (req, res, next) => {
	try {
		const teamID = req.params.team_id;
		const targetID = req.params.target;
		const roleID = req.body.role_id;

		await teamService.addTeamMember(req.user_id, teamID, targetID, roleID);

		res.status(200)
			.json(Response.success())
			.end();
	} catch (err) {
		next(err);
	}
});

router.patch('/:target', async (req, res, next) => {
	try {
		const teamID = req.params.team_id;
		const targetID = req.params.target;
		const roleID = req.body.role_id;

		await teamService.updateTeamMemberRole(req.user_id, teamID, targetID, roleID);

		res.status(200)
			.json(Response.success())
			.end();
	} catch (err) {
		next(err);
	}
});

router.delete('/:target', async (req, res, next) => {
	try {
		const teamID = req.params.team_id;
		const targetID = req.params.target;

		await teamService.deleteTeamMember(req.user_id, teamID, targetID);

		res.status(200)
			.json(Response.success())
			.end();
	} catch (err) {
		next(err);
	}
});

module.exports = router;
