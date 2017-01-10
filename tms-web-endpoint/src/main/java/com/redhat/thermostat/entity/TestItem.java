package com.redhat.thermostat.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Type;

@Entity
public class TestItem {

    @Id
    @Type(type = "objectid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String name;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
