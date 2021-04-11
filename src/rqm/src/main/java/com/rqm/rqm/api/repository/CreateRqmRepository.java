package com.rqm.rqm.api.repository;

import com.rqm.rqm.api.model.CreateRqmResponse;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CreateRqmRepository extends MongoRepository<CreateRqmResponse, Integer> {

}
