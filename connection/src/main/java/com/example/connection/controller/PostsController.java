package com.example.connection.controller;

import com.example.connection.model.Comments;
import com.example.connection.model.Posts;
import com.example.connection.model.Subscribes;
import com.example.connection.model.Users;
import com.example.connection.repository.CommentsRepo;
import com.example.connection.repository.PostsRepo;
import com.example.connection.repository.SubscribesRepo;
import com.example.connection.repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.criteria.CriteriaBuilder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Controller
public class PostsController {
    @Autowired
    private UsersRepo usersRepo;
    @Autowired
    private PostsRepo postsRepo;
    @Autowired
    private SubscribesRepo subscribesRepo;
    @Autowired
    private CommentsRepo commentsRepo;
    private static final String path = "C:\\Users\\Daniil\\IdeaProjects\\connection\\src\\main\\resources\\static\\";
    @GetMapping("/posts")
    public String users(@RequestParam("id") Integer id, Map<String, Object> model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        String photo = usersRepo.findByUsername(name).getImg();
        model.put("photo", photo);
        model.put("login",name);
        Iterable<Posts> posts = postsRepo.findAllById(id);
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
        Iterable<Comments> comments = commentsRepo.findAllByPostOrderByCommentdateDesc(id);
        model.put("comments",comments);
        return "post";

    }
    @PostMapping("/comment/{id}")
    public String addcomment(@PathVariable("id") Integer id,String comment){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Comments comments = new Comments();
        comments.setAuthor(name);
        comments.setCommenttext(comment);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        comments.setCommentdate(dateFormat.format(date).toString());
        comments.setPost(id);
        int id1 = 1;
        if(commentsRepo.findMinimum() !=null){
            id1 = commentsRepo.findMinimum();
            id1++;
        }
        comments.setId(id1);
        commentsRepo.save(comments);
        return "redirect:/posts?id="+id.toString();
    }
}
