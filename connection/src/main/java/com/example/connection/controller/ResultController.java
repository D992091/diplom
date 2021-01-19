package com.example.connection.controller;

import com.example.connection.model.Posts;
import com.example.connection.model.Subscribes;
import com.example.connection.model.Users;
import com.example.connection.repository.PostsRepo;
import com.example.connection.repository.SubscribesRepo;
import com.example.connection.repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class ResultController {
        @Autowired
        private UsersRepo usersRepo;
        @Autowired
        private PostsRepo postsRepo;
        @Autowired
        private SubscribesRepo subscribesRepo;
        private static final String path = "C:\\Users\\Daniil\\IdeaProjects\\connection\\src\\main\\resources\\static\\";
        @GetMapping("/results")
        public String results(@RequestParam("search") String sub, Map<String, Object> model) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String name = auth.getName();
            String photo = usersRepo.findByUsername(name).getImg();
            Iterable<Users> subs = usersRepo.findTop10ByUsername(sub);
            model.put("subs",subs);
            model.put("photo", photo);
            model.put("login", name);
            Iterable<Posts> posts = postsRepo.findTop10ByHeaderIsContaining(sub);
            model.put("posts", posts);
            Iterable<Subscribes> subscribes = subscribesRepo.findTop5ByWho(name);
            for (Subscribes s:subscribes
            ) {
                Users u = usersRepo.findByUsername(s.getWhom());
                s.setPh(u.getImg());
            }
            model.put("subscribes", subscribes);
            int postnumb = postsRepo.findCount(name);
            model.put("postnumb", postnumb);
            int whonumb = subscribesRepo.findCountWho(name);
            model.put("whonumb", whonumb);
            int whomnumb = subscribesRepo.findCountWhom(name);
            model.put("whomnumb", whomnumb);
            return "results";
        }
}
