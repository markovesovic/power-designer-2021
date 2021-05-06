const config = require('@config');

const MongoClient = require('mongodb').MongoClient;

const mongoConnection = {
	collections: {},

	_init: (collectionName) => {
		if (mongoConnection.collections[collectionName] === undefined) {
			return new Promise((resolve, reject) => {
				MongoClient.connect(config.get('MONGODB_URL'), {
					useNewUrlParser: true,
					useUnifiedTopology: true
				}, function (err, client) {
					if (err) {
						return reject(err);
					}

					const db = client.db(config.get('MONGODB_URL').split('/').pop());
					mongoConnection.collections[collectionName] = db.collection(collectionName);

					resolve(mongoConnection.collections[collectionName]);
				});
			});
		} else {
			return Promise.resolve(mongoConnection.collections[collectionName]);
		}
	}
};

module.exports = {
	client: (collectionName) => {
		const collectionPromise = new Promise ((resolve, reject) => {
			mongoConnection._init(collectionName).then(collection => {
				resolve(collection);
			}).catch(err => {
				reject(err);
			});
		});

		return {
			find: (filter, sort) => {
				return new Promise((resolve, reject) => {
					collectionPromise.then(collection => {
						if (sort) {
							collection.find(filter).sort(sort).toArray((err, results) => {
								if (err) {
									return reject(err);
								}

								resolve(results);
							});
						} else {
							collection.find(filter).toArray((err, results) => {
								if (err) {
									return reject(err);
								}

								resolve(results);
							});
						}
					});
				});
			},

			findOne: (filter) => {
				return new Promise((resolve, reject) => {
					collectionPromise.then(collection => {
						collection.find(filter).limit(1).toArray((err, results) => {
							if (err) {
								return reject(err);
							}

							if (results.length === 1) {
								resolve(results[0]);
							} else {
								resolve(null);
							}
						});
					});
				});
			},

			del: (filter) => {
				return new Promise((resolve, reject) => {
					collectionPromise.then(collection => {
						collection.deleteMany(filter, (err, results) => {
							if (err) {
								return reject(err);
							}

							resolve(results);
						});
					});
				});
			},

			insert: (object) => {
				return new Promise((resolve, reject) => {
					collectionPromise.then(collection => {
						collection.insertOne(object, (err, results) => {
							if (err) {
								return reject(err);
							}

							resolve(results);
						});
					});
				});
			}
		};
	}
};
