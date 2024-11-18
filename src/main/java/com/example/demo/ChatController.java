package com.example.demo;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class ChatController {

    private final List<ChatMessage> messageList = new ArrayList<>();

    @GetMapping("/")
    public String redirectToChat() {
        return "redirect:/chat";
    }

    @GetMapping("/chat")
    public String chatPage(Model model) {
        model.addAttribute("messages", messageList); // Pass the list of messages to Thymeleaf
        return "chat";
    }

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message) {
        messageList.add(message);
        return message;
    }
}

