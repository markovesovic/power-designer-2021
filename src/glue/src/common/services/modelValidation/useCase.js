/*
{
    "id": "2ef78850-e3af-4bcd-8dd7-37b9be0f940e", // optional
    "version": 1, // optional
    "use_case": [
        {
            "id": "145290ec-9d21-4cbe-a2fa-c7c110b007a2",
            "type": "actor",
            "name": "Korisnik",
            "from": [],
            "to": [
                {
                    "id": "e401386a-ada3-4a25-a125-9062b73c8aa0",
                    "link": "extend" // extend, include
                }
            ]
        },
        {
            "id": "e401386a-ada3-4a25-a125-9062b73c8aa0",
            "type": "use_case",
            "name": "Login je uspesan",
            "from": [
                {
                    "id": "145290ec-9d21-4cbe-a2fa-c7c110b007a2",
                    "link": "extend" // extend, include
                }
            ],
            "to": []
        }
    ]
}
*/

const Joi = require('joi');

module.exports.validateUseCaseModel = async (model) => {

   const schema = Joi.object({

      model_type: Joi.string().valid('use_case').required(),

      id: Joi.string().guid({ version: 'uuidv4' }).optional(),

      version: Joi.number().optional(),

      use_case: Joi.array().items(
         Joi.object({
            id: Joi.string().guid({ version: 'uuidv4' }).required(),
            type: Joi.string().valid('use_case', 'actor').required(),
            name: Joi.string().max(255).required(),
            from: Joi.array().items(
               Joi.object({
                  id: Joi.string().guid({ version: 'uuidv4' }).required(),
                  link: Joi.string().valid('extends', 'include', '').required()
               })
            ),
            to: Joi.array().items(
               Joi.object({
                  id: Joi.string().guid({ version: 'uuidv4' }).required(),
                  link: Joi.string().valid('extends', 'include', '').required()
               })
            )
         })
      )
   });

   let invalid = false;
   let useCaseIDs = [];

   model.use_case.forEach(use_case => {
      useCaseIDs.push(use_case.id);

      if(use_case.type == 'actor' && use_case.to.length != 0) {
         invalid = true;
      }

      // Check if use case is related to itself
      if(use_case.from.some( elem => elem.id === use_case.id) ||
         use_case.to.some( elem => elem.id === use_case.id)) {
            invalid = true;
         }
   });

   model.use_case.forEach(use_case => {
      use_case.from.forEach(({ id }) => {
         if(!useCaseIDs.some(useCaseID => useCaseID == id)) {
            invalid = true;
         }
      });
   })
   
   if(invalid) {
      return false;
   }

   const { error } = schema.validate(model);

   if(error) {
      console.log(error)
      return false;
   }

   return true;
}