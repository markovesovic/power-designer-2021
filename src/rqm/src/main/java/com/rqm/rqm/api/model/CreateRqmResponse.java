package com.rqm.rqm.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString

@Document(collection = "id")
public class CreateRqmResponse {

    private String id;
    private String status;
}
