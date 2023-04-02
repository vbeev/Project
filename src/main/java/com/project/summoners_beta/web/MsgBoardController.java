package com.project.summoners_beta.web;

import com.project.summoners_beta.model.dto.PostCreationDTO;
import com.project.summoners_beta.service.PostService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MsgBoardController {

    private final PostService postService;

    public MsgBoardController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/board/posts")
    public String getPosts(Model model) {

        model.addAttribute("posts", this.postService.getAll());

        return "chat-page";
    }

    @PostMapping("/board/posts")
    public String createPost(@AuthenticationPrincipal User user, PostCreationDTO postCreationDTO) {

        this.postService.create(postCreationDTO, user.getUsername());

        return "redirect:/board/posts";
    }

    @DeleteMapping("/board/posts")
    public String deletePost(@RequestParam(name = "postId") Long postId) {

        this.postService.removePost(postId);

        return "redirect:/board/posts";
    }
}
