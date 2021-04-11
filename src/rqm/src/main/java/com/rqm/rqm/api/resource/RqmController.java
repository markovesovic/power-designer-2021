package com.rqm.rqm.api.resource;


import com.rqm.rqm.api.model.CreateRqmResponse;
import com.rqm.rqm.api.model.RqmResponse;
import com.rqm.rqm.api.repository.CreateRqmRepository;
import com.rqm.rqm.api.repository.RqmRepository;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
public class RqmController {

    @Autowired
    private RqmRepository repository;

    @Autowired
    private CreateRqmRepository repositoryCreate;


    @PostMapping("/rqm")
    public CreateRqmResponse addRqm(@RequestBody RqmResponse rqm) {
        UUID uuid = UUID.randomUUID();
        rqm.setId(uuid.toString());
        repository.save(rqm);
        CreateRqmResponse createRqmResponse = new CreateRqmResponse();
        createRqmResponse.setId(rqm.getId());
        createRqmResponse.setStatus("Ok");
        repositoryCreate.save(createRqmResponse);

        for(CreateRqmResponse r : repositoryCreate.findAll()){
            if(r.getId().equals(rqm.getId()))
                return r;
        }
        return null;
    }

    @DeleteMapping("/rqm/{id}")
    public String deleteRqm(@PathVariable String id){
        for(RqmResponse r : repository.findAll()){
            if(r.getId().equals(id)){
                repository.delete(r);
                break;
            }
        }
        return "Rqm deleted with id: " + id;
    }

    @GetMapping("/rqm/{id}")
    public RqmResponse getRqm(@PathVariable String id){
        for(RqmResponse r : repository.findAll()){
            if(r.getId().equals(id)){
                return r;
            }
        }
        return null;
    }
}
