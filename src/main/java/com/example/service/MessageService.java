package com.example.service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService 
{
    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> getMessagesByAccountId(Integer accountId) {
        return messageRepository.findByPostedBy(accountId);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Optional<Message> getMessageById(Integer messageId) {
        return messageRepository.findById(messageId);
    }

    public ResponseEntity<Message> createMessage(Message message) {
        if(message.getMessageText().isEmpty() || message.getMessageText() == null || message.getMessageText().length() > 255) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Message newMessage = messageRepository.save(message);

            return ResponseEntity.ok().body(newMessage);
        } catch (Exception e) {
            // Handle any exceptions, e.g., database constraint violation
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    public ResponseEntity<Integer> deleteMessage(Integer messageId) {

        if(messageRepository.existsById(messageId) == false) {
            return ResponseEntity.ok().build();
        }
         
        messageRepository.deleteById(messageId);
        return ResponseEntity.ok().body(1);
        
    }
    public ResponseEntity<Integer> updateMessage(Integer messageId, Message replacement) {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        
        
        if (replacement.getMessageText() == null || replacement.getMessageText().isEmpty() || replacement.getMessageText().length() > 255) {
            return ResponseEntity.badRequest().build();
        }
        if(optionalMessage.isPresent()) {
            Message message = optionalMessage.get();
            message.setMessageText(replacement.getMessageText());
            messageRepository.save(message);
            return ResponseEntity.ok(1);
        }
        return ResponseEntity.badRequest().build();
    } 
}

