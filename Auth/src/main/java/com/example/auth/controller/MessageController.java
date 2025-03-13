package com.example.auth.controller;

import com.example.auth.entity.MessageEntity;
import com.example.auth.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/v1/message")
@RequiredArgsConstructor
public class MessageController {
    private final MessageRepository messageRepository;
    @GetMapping
    public List<MessageEntity> getAllMessages() {
        return messageRepository.findAll();
    }

    @PostMapping
    public MessageEntity createMessage(@RequestBody MessageEntity message) {
        return messageRepository.save(message);
    }
}
