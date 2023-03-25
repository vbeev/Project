package com.project.summoners_beta.web;

import com.project.summoners_beta.model.dto.PostCreationDTO;
import com.project.summoners_beta.service.PostService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ChatController {

    private final PostService postService;

    public ChatController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/chat/posts")
    public String forumPosts(Model model) {

        model.addAttribute("posts", this.postService.getAll());

        return "chat-page";
    }

    @PostMapping("/chat/posts")
    public String createPost(@AuthenticationPrincipal User user, PostCreationDTO postCreationDTO) {

        this.postService.create(postCreationDTO, user.getUsername());

        return "redirect:/chat/posts";
    }
}
