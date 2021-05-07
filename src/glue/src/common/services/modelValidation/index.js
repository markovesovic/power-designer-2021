const { InvalidModelError } = require('../../errors');
const { validateClassModel } = require('./class');
const { validateUseCaseModel } = require('./useCase');

module.exports = {
   validateModel
}


async function validateModel(req, _res, next) {
   
   try {
      let valid = false;
      let message = 'asd';
      
      if(req.body.model_type == 'rqm') {
         next();
         
      } else if(req.body.model_type == 'use_case') {
         valid = await validateUseCaseModel(req.body);
         
      } else if(req.body.model_type == 'class_model') {
         ({ valid, message } = await validateClassModel(req.body));
         
      }
      if(!valid) {
         throw new InvalidModelError(message);
      }
      next();
   } catch (err) {
      next(err);
   }
}
