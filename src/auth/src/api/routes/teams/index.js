const router = require('express').Router({ mergeParams: true });

const Response = require('@api/utils/response');
const { teamService } = require('@common/services');

router.post('/', async (req, res, next) => {
	try {
		const team = await teamService.createTeam(req.user_id, req.body.name);

		res.status(200)
			.json(Response.success({
				team_id: team.id
			})).end();
	} catch (err) {
		next(err);
	}
});

router.get('/:team_id', async (req, res, next) => {
	try {
		const teamID = req.params.team_id;

		const team = await teamService.getTeam(req.user_id, teamID);

		res.status(200)
			.json(Response.success(team))
			.end();
	} catch (err) {
		next(err);
	}
});

router.delete('/:team_id', async (req, res, next) => {
	try {
		const teamID = req.params.team_id;

		await teamService.deleteTeam(req.user_id, teamID);

		res.status(200)
			.json(Response.success())
			.end();
	} catch (err) {
		next(err);
	}
});

router.use('/:team_id/members', require('./members'));

module.exports = router;
