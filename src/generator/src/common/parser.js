module.exports = {
	formatArrayToObject,
	formatArrayToObjectsArray
};

function formatArrayToObject (_array, keyName) {
	const array = JSON.parse(JSON.stringify(_array));

	let object = { ...array };

	if (keyName) {
		object = {};
		keyName = keyName.split('.');

		array.forEach(element => {
			let key = undefined;

			for (let i = 0; i < keyName.length; i++) {
				if (key === undefined) {
					key = element[keyName[i]];
				} else {
					key = key[keyName[i]];
				}
			}

			object[key] = element;
		});
	}

	return object;
}

function formatArrayToObjectsArray (_array, keyName, valueName) {
	const array = JSON.parse(JSON.stringify(_array));

	const object = {};

	array.forEach(element => {
		if (object[element[keyName]] === undefined) {
			object[element[keyName]] = [];
		}

		const value = valueName ? element[valueName] : element;

		object[element[keyName]].push(value);
	});

	return object;
}
