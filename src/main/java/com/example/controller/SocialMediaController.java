package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.*;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

@RestController
public class SocialMediaController {

    private final MessageService messageService;
    private final AccountService accountService;

    @Autowired
    public SocialMediaController(MessageService messageService, AccountService accountService) {
        this.messageService = messageService;
        this.accountService = accountService;
    }

    // Endpoint to get messages by account ID
    @GetMapping("/accounts/{accountId}/messages")
    public List<Message> getMessages(@PathVariable Integer accountId) {
        return messageService.getMessagesByAccountId(accountId);
    }

    // Endpoint to get all messages 
    @GetMapping("/messages")
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }
    
    // Endpoint to get message by message id

    @GetMapping("/messages/{message_id}")
    public ResponseEntity<Message> getMessageById(@PathVariable("message_id") Integer messageId) {
        return messageService.getMessageById(messageId)
                .map(ResponseEntity::ok) // Message found, return 200 OK with message
                .orElse(ResponseEntity.ok().build()); // Message not found, return 404 Not Found
    }

    // Endpoint to create new message
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) { 
        return messageService.createMessage(message);

    }
    
    //delete message by id
    @DeleteMapping("/messages/{message_id}")
    public ResponseEntity<Object> deleteMessageById(@PathVariable("message_id") Integer messageId) {
        ResponseEntity<Integer> responseEntity = messageService.deleteMessage(messageId);
        return ResponseEntity.ok().body(responseEntity.getBody());

    }

    //Update a message
    @PatchMapping("/messages/{message_id}")
    public ResponseEntity<Integer> updateMessageById(@PathVariable("message_id") Integer messageId, @RequestBody Message replacement ) {
        return messageService.updateMessage(messageId, replacement);
    }

    // Endpoint to create a new account
    @PostMapping("/register")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        return accountService.registration(account);
    }
    @PostMapping("/login") 
    public ResponseEntity<Account> login(@RequestBody Account account) {
        return accountService.login(account);
    }

    

}
