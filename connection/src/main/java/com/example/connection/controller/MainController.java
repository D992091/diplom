package com.example.connection.controller;

import com.example.connection.model.Posts;
import com.example.connection.model.Subscribes;
import com.example.connection.model.Users;
import com.example.connection.repository.PostsRepo;
import com.example.connection.repository.SubscribesRepo;
import com.example.connection.repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class MainController {
    @Autowired
    private UsersRepo usersRepo;
    @Autowired
    private PostsRepo postsRepo;
    @Autowired
    private SubscribesRepo subscribesRepo;
    private static final String path = "C:\\Users\\Daniil\\IdeaProjects\\connection\\src\\main\\resources\\static\\";
    @GetMapping("/")
    public String greeting(Map<String,Object> model) throws InterruptedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        String photo = usersRepo.findByUsername(name).getImg();
        model.put("photo", photo);
        model.put("login",name);
        Iterable<Posts> posts = postsRepo.findAllByAuthorOrderByPostdateDesc(name);
        model.put("posts",posts);
        Iterable<Subscribes> subscribes = subscribesRepo.findTop5ByWho(name);
        for (Subscribes s:subscribes
        ) {
            Users u = usersRepo.findByUsername(s.getWhom());
            s.setPh(u.getImg());
        }
        model.put("subscribes",subscribes);
        int postnumb = postsRepo.findCount(name);
        model.put("postnumb",postnumb);
        int whonumb = subscribesRepo.findCountWho(name);
        model.put("whonumb",whonumb);
        int whomnumb = subscribesRepo.findCountWhom(name);
        model.put("whomnumb",whomnumb);
        return "greeting1";
    }
    @GetMapping("/greeting1")
    public String greeting1(Map<String,Object> model) {
        Iterable<Posts> posts = postsRepo.findAll();
        model.put("posts",posts);
        return "greeting1";
    }

    @PostMapping("/")
    public String addPost(String header, String post, Map<String,Object> model, @RequestParam("file") MultipartFile file){

        if (file.isEmpty()){
            Posts post1 = new Posts();
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            int id = 1;
            if(postsRepo.findMax() !=null){
                id = postsRepo.findMax();
                id++;
            }
            String name = auth.getName();
            post1.setId(id);
            post1.setAuthor(name);
            post1.setHeader(header);
            post1.setPost(post);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            post1.setPostdate(dateFormat.format(date).toString());
            post1.setPhoto("none");
            postsRepo.save(post1);
            Iterable<Posts> posts = postsRepo.findAll();
            model.put("posts", posts);
        }else {
            Posts post1 = new Posts();
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String name = auth.getName();
            int id = 1;
            if(postsRepo.findMax() !=null){
                id = postsRepo.findMax();
                id++;
            }
            post1.setId(id);
            post1.setAuthor(name);
            post1.setHeader(header);
            post1.setPost(post);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            post1.setPostdate(dateFormat.format(date).toString());
            try {
                String path1 = path;
                byte[] bytes = file.getBytes();
                path1+="img\\";
                File f = new File(path1);
                if(!f.exists()){
                    f.mkdir();
                }
                path1+=name;
                path1+="\\";
                f = new File(path1);
                if(!f.exists()){
                    f.mkdir();
                }
                path1+="posts\\";
                f = new File(path1);
                if(!f.exists()){
                    f.mkdir();
                }
                id = f.listFiles().length;
                id++;
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File ( path1 + Integer.toString(id)  + ".jpg")));
                stream.write(bytes);
                stream.close();
                String address = "img" + "/" +  name + "/" + "posts/" +  Integer.toString(id) + ".jpg";
                post1.setPhoto(address);
                postsRepo.save(post1);
                Iterable<Posts> posts = postsRepo.findAll();
                model.put("posts", posts);

            } catch (Exception e) {
                System.out.println("Вам не удалось загрузить");
            }
        }
        return "redirect:/";
    }
    @PostMapping("/search")
    public String search(String search){
        return "redirect:/results?search="+search;
    }
}