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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Map;

@Controller
public class ProfileController {
    @Autowired
    private UsersRepo usersRepo;
    @Autowired
    private PostsRepo postsRepo;
    @Autowired
    private SubscribesRepo subscribesRepo;
    private static final String path = "C:\\Users\\Daniil\\IdeaProjects\\connection\\src\\main\\resources\\static\\";

    @GetMapping("/profile")
    public String profile(Map<String, Object> model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        String photo = usersRepo.findByUsername(name).getImg();
        Iterable<Subscribes> subs = subscribesRepo.findAllByWho(name);
        for (Subscribes s:subs
        ) {
            Users u = usersRepo.findByUsername(s.getWhom());
            s.setPh(u.getImg());
        }
        Iterable<Subscribes> subs1 = subscribesRepo.findAllByWhom(name);
        for (Subscribes s:subs1
        ) {
            Users u = usersRepo.findByUsername(s.getWho());
            s.setPh(u.getImg());
        }

        model.put("subs",subs);
        model.put("subs1",subs1);
        model.put("photo", photo);
        model.put("login", name);
        Iterable<Posts> posts = postsRepo.findAllByAuthorOrderByPostdateDesc(name);
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
        return "profile";
    }

    @PostMapping("/profile")
    public String changePhoto(@RequestParam("file") MultipartFile file) throws IOException, InterruptedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        if (!file.isEmpty()) {
            String path1 = path;
            byte[] bytes = new byte[0];
            try {
                bytes = file.getBytes();
            } catch (IOException e) {
                e.printStackTrace();
            }
            path1 += "img\\";
            File f = new File(path1);
            if (!f.exists()) {
                f.mkdir();
            }
            path1 += name;
            path1 += "\\";
            f = new File(path1);
            if (!f.exists()) {
                f.mkdir();
            }
            path1 += "profile\\";
            f = new File(path1);
            if (!f.exists()) {
                f.mkdir();
            }
            f = new File((path1 + "profile.jpg"));
            if (f.exists()) {
                f.delete();
            }
            BufferedOutputStream stream =
                    new BufferedOutputStream(new FileOutputStream(new File(path1 + "profile.jpg")));
            stream.write(bytes);
            stream.close();
            Users user = usersRepo.findByUsername(name);
            user.setImg("img" + "/" +  user.getUsername() + "/" + "profile/" +"profile.jpg");
            usersRepo.save(user);
        }
        return "redirect:/profile";
    }

    @PostMapping("/profile1")
    public String changePass(@RequestParam String oldpass, @RequestParam String newpass, @RequestParam String returnnewpass) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Users user = usersRepo.findByUsername(name);
        if (new BCryptPasswordEncoder(12).matches(oldpass,user.getPassword())) {
            if (newpass.equals(returnnewpass)) {
                newpass = new BCryptPasswordEncoder(12).encode(newpass);
                user.setPassword(newpass);
                usersRepo.save(user);
            }
        }else{
            System.out.println("no");
        }
        return "redirect:/profile";

    }
    @PostMapping("/deletepost/{postdate}")
    @Transactional
    public String deletePost(@PathVariable("postdate") String date1){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        postsRepo.deleteByAuthorAndPostdate(name,date1);
        return "redirect:/profile";
    }
    @PostMapping("/deletesubs/{whom}")
    @Transactional
    public String deleteSubs(@PathVariable("whom") String whom){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        subscribesRepo.deleteByWhomAndWho(whom,name);
        return "redirect:/profile";
    }
    @PostMapping("/deletesubs1/{who}")
    @Transactional
    public String deleteSubs1(@PathVariable("who") String who){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        subscribesRepo.deleteByWhomAndWho(name,who);
        return "redirect:/profile";
    }
}
