const Joi = require('joi');

/*
{
    "model_type": "class_model",
    "id": "541687fb-0372-4ce7-96c0-39b4524dbfcb", // uuid
    "version": 1, // number
    "class_model": [
        {
            "id": "145290ec-9d21-4cbe-a2fa-c7c110b007a2" // uuid
            "type": "class", // class, interface
            "name": "Test", // String
            "is_abstract": false, // true, false
            "attributes": [
                {
                    "type": "int", // elementary types, class, interface
                    "name": "count", // String
                    "is_function": false, // true, false
                    "is_private": false, // true, false
                }
            ],
            "from": [
               {
                  "id": "",
                  "connection_type": "generalization"
               }
            ], // array element is id from another class_model element
            "to": [
               {
                  "id": "",
                  "connection_type": "aggregation"
               }
            ] // array element is id from another class_model element
        }
    ]
}
*/

module.exports.validateClassModel = async (model) => {

   const schema = Joi.object({

      model_type: Joi.string().valid('class_model').required(),

      id: Joi.string().guid({ version: 'uuidv4' }).optional(),

      version: Joi.number().optional(),

      class_model: Joi.array().items(
         Joi.object({

            id: Joi.string().guid({ version: 'uuidv4' }).required(),
            type: Joi.string().valid('class', 'interface').required(),
            name: Joi.string().required(),
            is_abstract: Joi.boolean().required(),

            attributes: Joi.array().items(
               Joi.object({

                  name: Joi.string().required(),
                  type: Joi.string().required(),
                  is_function: Joi.boolean().required(),
                  is_private: Joi.boolean().required()

            })).unique('name'),

            from: Joi.array().items(
               Joi.object({
                  id: Joi.string().guid({ version: 'uuidv4' }),
                  connection_type: Joi.string().valid('generalization', 'association', 'realization')
               })
            ),
            to: Joi.array().items(
               Joi.object({
                  id: Joi.string().guid({ version: 'uuidv4' }),
                  connection_type: Joi.string().valid('generalization', 'association', 'realization')
               })
            )

      })).unique('name')
   });

   let classIDs = [];
   let classInterfaceNames = [];
   let classInterfaceIDs = [];
   
   let invalid = false;
   let message;

   model.class_model.forEach(class_model => {
      if(class_model.type == 'class') {
         classIDs.push(class_model.id);
      }
      classInterfaceNames.push(class_model.name);
      classInterfaceIDs.push(class_model.id);
      // Check if class extends itself (??)
      if(class_model.from.some( elem => elem.id === class_model.id) ||
         class_model.to.some( elem => elem.id === class_model.id)) {
         invalid = true;
         message = 'class id in its connections';
      }
   });

   // Check if attr types are valid
   model.class_model.forEach(class_model => {
      class_model.attributes.forEach(attribute => {

         if(attribute.type != 'int' &&
            attribute.type != 'String' &&
            !classInterfaceNames.some(className => className == attribute.type)) {
               message = 'param of undefined type';
               invalid = true;
            }
      });
   });
   console.log(classIDs);

   // Check if class extends more than one class and if interface extends class (??)
   model.class_model.forEach(class_model => {
      let classNum = 0;
      class_model.from.forEach(connection => {
         if(classIDs.some(id => id == connection.id) &&
             connection.connection_type == 'generalization') {
            classNum++;
         }
      });
      if(class_model.type == 'class') {
         if(classNum > 1) {
            invalid = true;
            message = 'multiple inheritance';
         }
      } else {
         if(classNum > 0) {
            invalid = true;
            message = 'interface inherits class';
         }
      }

      // Check if there is same class in from and to arr
      if(class_model.to.filter(value => class_model.from.includes(value)).length != 0) {
         invalid = true;
         message = 'circular implementation';
      }

      // Check if implementing unknown id
      class_model.to.forEach(element => {
         if(!classInterfaceIDs.some(id => id == element.id)) {
            invalid = true;
            message = 'referenced unknown class or interface';
         }
      });
      class_model.from.forEach(element => {
         if(!classInterfaceIDs.some(id => id == element.id)) {
            invalid = true;
            message = 'referenced unknown class or interface';
         }
      })
   });

   if(invalid) {
      console.log(message);
      return { valid: false, message: message };
   }

   const { error } = schema.validate(model);

   if(error) {
      console.log(error);
      return { valid: false, message: error.message };
   }
   return { valid: true, message: null};
}