package com.example.connection.controller;

import com.example.connection.model.Roles;
import com.example.connection.model.Users;
import com.example.connection.repository.RolesRepo;
import com.example.connection.repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.io.File;
import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {
    @Autowired
    UsersRepo usersRepo;
    @Autowired
    RolesRepo rolesRepo;
    private static final String path = "C:\\Users\\Daniil\\IdeaProjects\\connection\\src\\main\\resources\\static\\";

    @GetMapping("/registration")
    public String registration(Map<String,Object> model) {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(Users user, Map<String,Object> model){
        Users user1 = usersRepo.findByUsername(user.getUsername());
        if(user1!=null){
            model.put("message","User already exists");
            return "registration";
        }else{
            String photo = "connection.jpg";
            user.setImg(photo);
            user.setEnabled(true);
            String pass = user.getPassword();
            pass = new BCryptPasswordEncoder(12).encode(pass);
            user.setPassword(pass);
            Iterable<Roles> allRoles = rolesRepo.findAll();
            int counter = rolesRepo.findMinimum();
            Roles role = new Roles(++counter,user.getUsername(),"ROLE_USER");
            rolesRepo.save(role);
            usersRepo.save(user);
            return "redirect:/login";
        }

    }

}
