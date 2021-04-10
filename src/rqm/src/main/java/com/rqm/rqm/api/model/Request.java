package com.rqm.rqm.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString

@Document(collection = "rqm")
public class Request {


    private int id;
    private String number;
    private String title;
    private String desc;
    private Type type;
    private int priority;
    private Risk risk;
    private ArrayList<Request> subRequests;
}
