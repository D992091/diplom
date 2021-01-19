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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class UserController {
    @Autowired
    private UsersRepo usersRepo;
    @Autowired
    private PostsRepo postsRepo;
    @Autowired
    private SubscribesRepo subscribesRepo;
   private static final String path = "C:\\Users\\Daniil\\IdeaProjects\\connection\\src\\main\\resources\\static\\";
    @GetMapping("/users")
    public String users(@RequestParam("sub") String sub, Map<String, Object> model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        if(sub.equals(name)){
            return "redirect:/profile";
        }
        String photo = usersRepo.findByUsername(name).getImg();
        Iterable<Subscribes> subs = subscribesRepo.findAllByWho(name);
        Iterable<Subscribes> subs1 = subscribesRepo.findAllByWhom(name);
        model.put("subs",subs);
        model.put("subs1",subs1);
        model.put("photo", photo);
        model.put("login", name);
        model.put("sub",sub);
        Users usub = usersRepo.findByUsername(sub);
        String photosub = usub.getImg();
        model.put("photosub",photosub);
        Iterable<Posts> posts = postsRepo.findAllByAuthorOrderByPostdateDesc(sub);
        int count=0;
        count = subscribesRepo.findCountWhom1(name,sub);
        if(count>0){
            model.put("btn","Unsubscribe");
        }else{
            model.put("btn","Subscribe");
        }
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
        return "users";
    }
    @PostMapping("/makesub/{sub}")
    @Transactional
    public String deleteSubs(@PathVariable("sub") String sub, Map<String, Object> model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        int count=0;
        count = subscribesRepo.findCountWhom1(name,sub);
        if(count>0){
            subscribesRepo.deleteByWhomAndWho(sub,name);
        }else{
            if(subscribesRepo.findMax() != null) {
                int id = subscribesRepo.findMax();
                id++;
                Subscribes subs = new Subscribes(id,name,sub);
                subscribesRepo.save(subs);
            }else{
                int id = 1;
                Subscribes subs = new Subscribes(id,name,sub);
                subscribesRepo.save(subs);
            }

        }
        return "redirect:/users?sub="+sub;
    }
}
