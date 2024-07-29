package dev.artep.latroc_backend_simple.service;

import dev.artep.latroc_backend_simple.model.LocalUser;
import dev.artep.latroc_backend_simple.model.Message;
import dev.artep.latroc_backend_simple.repository.LocalMessageDAO;
import dev.artep.latroc_backend_simple.repository.LocalUserDAO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {
    private LocalMessageDAO localMessageDAO;
    private LocalUserDAO localUserDAO;

    public MessageService(LocalMessageDAO localMessageDAO, LocalUserDAO localUserDAO) {
        this.localMessageDAO = localMessageDAO;
        this.localUserDAO = localUserDAO;
    }

    public Message sendMessage(String senderUsername, String receiverUsername, String content) {
        LocalUser sender = localUserDAO.findByUsernameIgnoreCase(senderUsername)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        LocalUser receiver = localUserDAO.findByUsernameIgnoreCase(receiverUsername)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        message.setRead(false);

        return localMessageDAO.save(message);
    }

    public List<Message> getMessages(String username) {
        LocalUser user = localUserDAO.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return localMessageDAO.findBySenderOrReceiver(user, user);
    }

    public void markAsRead(Long messageId) {
        Message message = localMessageDAO.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        message.setRead(true);
        localMessageDAO.save(message);
    }
}
