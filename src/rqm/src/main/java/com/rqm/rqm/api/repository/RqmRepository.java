package com.rqm.rqm.api.repository;

import com.rqm.rqm.api.model.RqmResponse;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RqmRepository extends MongoRepository<RqmResponse, Integer> {

}
