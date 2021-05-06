const { InvalidModelError } = require('../../errors');
const Joi = require('joi');

module.exports = {
   validateModel
}


async function validateModel(req, _res, next) {
   
   try {
      let valid;
      
      if(req.body.hasOwnProperty('rqm')) {
         next();
         
      } else if(req.body.hasOwnProperty('use_case')) {
         valid = await validateUseCaseModel(req.body);
         
      } else if(req.body.hasOwnProperty('class_model')) {
         valid = await validateClassModel(req.body);
         
      }
      if(!valid) {
         throw new InvalidModelError();
      }
      next();
   } catch (err) {
      next(err);
   }
}

async function validateUseCaseModel(model) {
   return false;
}

/*
{
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
                }
            ],
            "from": [], // array element is id from another class_model element
            "to": [] // array element is id from another class_model element
        }
    ]
}
*/

async function validateClassModel(model) {

   const schema = Joi.object({
      id: Joi.string().guid({ version: 'uuidv4'}).required(),
      version: Joi.number(),
      class_model: Joi.number()
   });

   // console.log(model);

   const { error } = schema.validate(model);
   if(error) {
      return false;
   }
   return true;
}