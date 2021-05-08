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

{
   "model_type":"class_model",
   
   "class_model":[
      {
         "id":"145290ec-9d21-4cbe-a2fa-c7c110b007a1",
         "type":"class",
         "name":"Test1",
         "is_abstract":false,
         "attributes":[
            {
               "type":"Test1",
               "name":"count1",
               "is_function":false,
               "modifier": "public"
            }
         ],
         "to":[
            
         ],
         "from":[
            
         ]
      },
      {
         "id":"145290ec-9d21-4cbe-a2fa-c7c110b007a2",
         "type":"class",
         "name":"Test2",
         "is_abstract":false,
         "attributes":[
            {
               "type":"int",
               "name":"count2",
               "is_function":false,
               "modifier": "public"
            }
         ],
         "to":[
            {
               "id":"145290ec-9d21-4cbe-a2fa-c7c110b007a4",
               "connection_type":"generalization"
            }
         ],
         "from":[
            {
               "id":"145290ec-9d21-4cbe-a2fa-c7c110b007a1",
               "connection_type":"generalization"
            },
            {
               "id":"145290ec-9d21-4cbe-a2fa-c7c110b007a3",
               "connection_type":"association"
            }
         ]
      },
      {
         "id":"145290ec-9d21-4cbe-a2fa-c7c110b007a3",
         "type":"interface",
         "name":"Test3",
         "is_abstract":false,
         "attributes":[
            {
               "type":"int",
               "name":"count2",
               "is_function":false,
               "modifier": "public"
            }
         ],
         "to":[
            {
               "id":"145290ec-9d21-4cbe-a2fa-c7c110b007a2",
               "connection_type":"generalization"
            }
         ],
         "from":[
            {
               "id":"145290ec-9d21-4cbe-a2fa-c7c110b007a4",
               "connection_type":"generalization"
            },
            {
               "id":"145290ec-9d21-4cbe-a2fa-c7c110b007a4",
               "connection_type":"generalization"
            }
         ]
      },
      {
         "id":"145290ec-9d21-4cbe-a2fa-c7c110b007a4",
         "type":"interface",
         "name":"Test4",
         "is_abstract":false,
         "attributes":[
            {
               "type":"int",
               "name":"count2",
               "is_function":false,
               "modifier": "public"
            }
         ],
         "to":[
            {
               "id":"145290ec-9d21-4cbe-a2fa-c7c110b007a2",
               "connection_type":"generalization"
            }
         ],
         "from":[
            {
               "id":"145290ec-9d21-4cbe-a2fa-c7c110b007a3",
               "connection_type":"generalization"
            },
            {
               "id":"145290ec-9d21-4cbe-a2fa-c7c110b007a3",
               "connection_type":"generalization"
            }
         ]
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
            x_coordinate: Joi.number().optional(),
            y_coordinate: Joi.number().optional(),

            attributes: Joi.array().items(
               Joi.object({

                  name: Joi.string().required(),
                  type: Joi.string().required(),
                  is_function: Joi.boolean().required(),
                  modifier: Joi.string().valid('private', 'public', 'protected', 'default'),

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
      } else {
         if(class_model.attributes.length != 0) {
            invalid = true;
            message = 'interface cant have attributes';
            return;
         }
      }
      classInterfaceNames.push(class_model.name);
      classInterfaceIDs.push(class_model.id);
      // Check if class extends itself (??)

      if(class_model.from == undefined || class_model.to == undefined) {
         invalid = true;
         message = 'bad json form, no from or to arrays'; 
         return;
      }

      if(class_model.from.some( elem => elem.id === class_model.id) ||
         class_model.to.some( elem => elem.id === class_model.id)) {
         invalid = true;
         message = 'class id in its connections';
         return;
      }
   });

   if(invalid) {
      console.log(message);
      return { valid: false, message: message };
   }


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