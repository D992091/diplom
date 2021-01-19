package com.example.connection.model;


import javax.persistence.*;


@Entity
@Table(name="users")
public class Users {
    @Id
    private String username ;
    private String password;
    private boolean enabled;
    private String img;

    public Users(){

    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}