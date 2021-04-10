package com.rqm.rqm.api.resource;


import com.rqm.rqm.api.model.Request;
import com.rqm.rqm.api.repository.RqmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class RqmController {

    @Autowired
    private RqmRepository repository;



    @PostMapping("/rqm")
    public String addRqm(@RequestBody Request rqm){
        repository.save(rqm);
        return "Added rqm with id: " + rqm.getId();
    }

    @DeleteMapping("/rqm/{id}")
    public String deleteRqm(@PathVariable int id){
        repository.deleteById(id);
        return "Rqm deleted with id: " + id;
    }

    @GetMapping("/rqm/{id}")
    public Request getRqm(@PathVariable int id){
        return repository.findById(id).get();
    }
}
