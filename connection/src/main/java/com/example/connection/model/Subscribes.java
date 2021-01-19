package com.example.connection.model;

import javax.persistence.*;

@Entity
public class Subscribes {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String who;
    private String whom;
    private String ph;

    Subscribes(){}

    public Subscribes(Integer id, String who, String whom) {
        this.id = id;
        this.who = who;
        this.whom = whom;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public String getWhom() {
        return whom;
    }

    public void setWhom(String whom) {
        this.whom = whom;
    }

    public String getPh() {
        return ph;
    }

    public void setPh(String ph) {
        this.ph = ph;
    }
}
