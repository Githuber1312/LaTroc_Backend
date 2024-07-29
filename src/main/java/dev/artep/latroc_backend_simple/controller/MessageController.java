package dev.artep.latroc_backend_simple.controller;

import dev.artep.latroc_backend_simple.dto.MessageBody;
import dev.artep.latroc_backend_simple.model.Message;
import dev.artep.latroc_backend_simple.service.JWTService;
import dev.artep.latroc_backend_simple.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
@CrossOrigin(origins = "http://localhost:3000")
public class MessageController {

    private final MessageService messageService;
    private final JWTService jwtService;

    public MessageController(MessageService messageService, JWTService jwtService) {
        this.messageService = messageService;
        this.jwtService = jwtService;
    }

    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(@RequestHeader("Authorization") String token,
                                               @RequestBody @Valid MessageBody messageBody) {
        String parsedToken = token.replace("Bearer ", "");
        String senderUsername = jwtService.getUsername(parsedToken);

        // Show message in console
        System.out.println("Message SENT: " + senderUsername + " to " + messageBody.getReceiverUsername());
        System.out.println("Message: " + messageBody.getContent());

        Message message = messageService.sendMessage(senderUsername, messageBody.getReceiverUsername(), messageBody.getContent());
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Message>> getMessages(@RequestHeader("Authorization") String token) {
        String username = jwtService.getUsername(token.replace("Bearer ", ""));
        List<Message> messages = messageService.getMessages(username);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @PutMapping("/{messageId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long messageId) {
        messageService.markAsRead(messageId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
