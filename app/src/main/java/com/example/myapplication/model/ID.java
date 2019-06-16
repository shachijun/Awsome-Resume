package com.example.myapplication.model;

import java.util.UUID;

public class ID {
    public String id;
    public ID(){
        id = UUID.randomUUID().toString();
    }
}
