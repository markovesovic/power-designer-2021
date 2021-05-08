const { v4: uuidV4 } = require('uuid');

const db = require('@common/db');

module.exports = {
	addModel,
	getModel,
	deleteModel
};

async function addModel (model) {
	if (!model.id) {
		model.id = uuidV4();
		model.version = 1;
	} else {
		const lastModel = await getModel(model.id);

		if (lastModel) {
			model.version = lastModel.version + 1;
		} else {
			model.version = 1;
		}
	}

	await db.client('use_case').insert(model);

	return model;
}

async function getModel (id, version) {
	if (version) {
		return db.client('use_case').find({ id, version });
	}

	const modelVersions = await db.client('use_case').find({ id }, { version: -1 });

	return modelVersions[0];
}

function deleteModel (id) {
	return db.client('use_case').del({ id });
}
