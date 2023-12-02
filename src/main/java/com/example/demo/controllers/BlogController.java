package com.example.demo.controllers;

import com.example.demo.models.Post;
import com.example.demo.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class BlogController {

    private final PostRepository postRepository;

    @Autowired
    public BlogController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping("/blog")
    public String viewBlog(Model model){
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "blog";
    }

    @GetMapping("/blog/{id}")
    public String viewPost(@PathVariable("id") long id, Model model){
        List<Post> posts = new ArrayList<>();
        Optional<Post> uniquePost = postRepository.findById(id);
        uniquePost.ifPresent(posts::add);
        model.addAttribute("posts", posts);
        return "post";
    }

    @GetMapping("/blog/create")
    public String createPost(){
        return "create";
    }

    @PostMapping("/blog/create")
    public String setPost(@RequestParam String title,@RequestParam String announce,@RequestParam String text){
        Post post = new Post(title, announce, text);
        postRepository.save(post);
        return "redirect:/blog";
    }

    @GetMapping("/blog/{id}/update")
    public String updatePost(Model model, @PathVariable("id") Long id){
        List<Post> posts = new ArrayList<>();
        Optional<Post> uniquePost = postRepository.findById(id);
        uniquePost.ifPresent(posts::add);
        model.addAttribute("posts", posts);
        return "update";
    }

    @PostMapping("/blog/{id}/update")
    public String updatePost(@PathVariable("id") Long id,
                          @RequestParam String title,
                          @RequestParam String announce,
                          @RequestParam String text,
                          Model model){
        Post post = postRepository.findById(id).orElseThrow();
        model.addAttribute("posts", postRepository.findById(id));
        post.setTitle(title);
        post.setAnnounce(announce);
        post.setText(text);
        postRepository.save(post);
        return "redirect:/blog/{id}";
    }

    @GetMapping("/blog/{id}/delete")
    public String deletePost(@PathVariable("id") Long id)
    {
        postRepository.deleteById(id);
        return "redirect:/blog";
    }
}
