const fs = require('fs');
const { v4: uuidv4 } = require('uuid');

const parser = require('@common/parser');

module.exports = {
	rqmToUseCase,
	classModelToJava
};

function rqmToUseCase (useCases) {
	const result = [];

	rqmToUseCaseRecursive(useCases, result);

	return { use_case: result };
}

function rqmToUseCaseRecursive (useCases, result) {
	if (!useCases || useCases.length <= 0) {
		return;
	}

	for (const useCase of useCases) {
		if (useCase.type === 'funcional') {
			result.push({
				id: uuidv4(),
				name: useCase.title,
				type: 'use_case',
				from: [],
				to: []
			});
		}

		rqmToUseCaseRecursive(useCase.subRequests);
	}
}

function classModelToJava (classModel) {
	const files = [];
	const classModelMap = parser.formatArrayToObject(classModel, 'id');

	classModel.forEach(model => {
		let generatedCode = '';

		if (model.is_abstract) {
			generatedCode += 'abstract ';
		}

		generatedCode += `${model.type} ${model.name} `;

		const extendsClasses = [];
		const implementsClasses = [];
		for (const connection of model.from) {
			if (classModelMap[connection.id].type === 'class') {
				extendsClasses.push(classModelMap[connection.id]);
			}
			else if (classModelMap[connection.id].type === 'interface') {
				implementsClasses.push(classModelMap[connection.id]);
			}
		}

		// Generate inheritance
		if (extendsClasses.length > 0) {
			generatedCode += 'extends ';
		}

		for (const extendsClass of extendsClasses) {
			generatedCode += extendsClass.name + ' ';
		}

		if (implementsClasses.length > 0) {
			generatedCode += 'implements ';
		}

		for (const implementsClass of implementsClasses) {
			generatedCode += implementsClass.name + ' ';
		}

		generatedCode += ' {\n';

		// Generate attributes
		for (const attribute of model.attributes) {
			if (!attribute.is_function) {
				generatedCode += `\t${attribute.modifier} ${attribute.type} ${attribute.name};\n`;
			}
		}

		// Generate constructor
		if (model.type === 'class') {
			let constructorSuperAttributes;
			if (extendsClasses.length > 0) {
				constructorSuperAttributes = extendsClasses[0].attributes.map(attribute => !attribute.is_function ? ` ${attribute.type} ${attribute.name}` : '');

				if (constructorSuperAttributes.length > 0) {
					constructorSuperAttributes = constructorSuperAttributes.filter(constructorAttribute => constructorAttribute.length > 0);

					if (constructorSuperAttributes[0]) {
						constructorSuperAttributes[0] = constructorSuperAttributes[0].substring(1);
					}
				}
			}

			let constructorAttributes = model.attributes.map(attribute => !attribute.is_function ? ` ${attribute.type} ${attribute.name}` : '');
			if (constructorAttributes.length > 0) {
				constructorAttributes = constructorAttributes.filter(constructorAttribute => constructorAttribute.length > 0);

				if (constructorAttributes[0]) {
					constructorAttributes[0] = constructorAttributes[0].substring(1);
				}
			}

			generatedCode += `\n\tpublic ${model.name}(`;

			if (constructorAttributes && constructorAttributes.length > 0) {
				generatedCode += `${constructorAttributes}`;

				if (constructorSuperAttributes && constructorSuperAttributes.length > 0) {
					generatedCode += `, ${constructorSuperAttributes}`;
				}
			} else if (constructorSuperAttributes && constructorSuperAttributes.length > 0) {
				generatedCode += `${constructorSuperAttributes}`;
			}

			generatedCode += ') {\n';

			if (constructorSuperAttributes && constructorSuperAttributes.length > 0) {
				let superAttributes = extendsClasses[0].attributes.map(attribute => !attribute.is_function ? ` ${attribute.name}` : '');

				superAttributes = superAttributes.filter(superAttribute => superAttribute.length > 0);
				superAttributes[0] = superAttributes[0].substring(1);

				generatedCode += `\t\tsuper(${superAttributes});\n`;
			}

			if (constructorAttributes && constructorAttributes.length > 0) {
				for (const attribute of model.attributes) {
					if (!attribute.is_function) {
						generatedCode += `\t\tthis.${attribute.name} = ${attribute.name};\n`;
					}
				}
			}

			generatedCode += '\t}\n\n';
		}


		// Generate methods
		if (model.type === 'class' && !model.is_abstract) {
			for (const attribute of model.attributes) {
				if (attribute.is_function) {
					generatedCode += `\n\t${attribute.modifier} ${model.is_abstract ? 'abstract ' : ''}${attribute.type} ${attribute.name}() {\n\t\t// TO-DO: Implement ${attribute.name}\n\t}\n`;
				}
			}
		} else {
			for (const attribute of model.attributes) {
				if (attribute.is_function) {
					generatedCode += `\t${attribute.modifier} ${model.is_abstract ? 'abstract ' : ''}${attribute.type} ${attribute.name}();\n`;
				}
			}
		}

		generatedCode += '}';

		files.push({
			file_name: `${model.name}.java`,
			code: generatedCode
		});
	});

	files.forEach(file => {
		fs.writeFileSync(`${__dirname}/generated-files/${file.file_name}`, file.code, 'utf8');
	});

	return files;
}
