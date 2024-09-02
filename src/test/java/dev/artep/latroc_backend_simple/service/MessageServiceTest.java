package dev.artep.latroc_backend_simple.service;

import dev.artep.latroc_backend_simple.model.LocalUser;
import dev.artep.latroc_backend_simple.model.Message;
import dev.artep.latroc_backend_simple.repository.LocalMessageDAO;
import dev.artep.latroc_backend_simple.repository.LocalUserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MessageServiceTest {

    private MessageService messageService;
    private LocalMessageDAO mockMessageDAO;
    private LocalUserDAO mockUserDAO;

    @BeforeEach
    void setUp() {
        mockMessageDAO = mock(LocalMessageDAO.class);
        mockUserDAO = mock(LocalUserDAO.class);
        messageService = new MessageService(mockMessageDAO, mockUserDAO);
    }

    @Test
    void testSendMessage() {
        LocalUser sender = new LocalUser();
        sender.setUsername("sender");
        LocalUser receiver = new LocalUser();
        receiver.setUsername("receiver");
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent("Hello");
        message.setTimestamp(LocalDateTime.now());
        message.setRead(false);

        when(mockUserDAO.findByUsernameIgnoreCase("sender")).thenReturn(Optional.of(sender));
        when(mockUserDAO.findByUsernameIgnoreCase("receiver")).thenReturn(Optional.of(receiver));
        when(mockMessageDAO.save(any(Message.class))).thenReturn(message);

        Message result = messageService.sendMessage("sender", "receiver", "Hello");

        assertNotNull(result);
        assertEquals("Hello", result.getContent());
        assertEquals(sender, result.getSender());
        assertEquals(receiver, result.getReceiver());
        assertFalse(result.isRead());

        verify(mockUserDAO, times(1)).findByUsernameIgnoreCase("sender");
        verify(mockUserDAO, times(1)).findByUsernameIgnoreCase("receiver");
        verify(mockMessageDAO, times(1)).save(any(Message.class));
    }

    @Test
    void testGetMessages() {
        LocalUser user = new LocalUser();
        user.setUsername("user");
        Message message1 = new Message();
        message1.setSender(user);
        Message message2 = new Message();
        message2.setReceiver(user);

        when(mockUserDAO.findByUsernameIgnoreCase("user")).thenReturn(Optional.of(user));
        when(mockMessageDAO.findBySenderOrReceiver(user, user)).thenReturn(Arrays.asList(message1, message2));

        List<Message> messages = messageService.getMessages("user");

        assertNotNull(messages);
        assertEquals(2, messages.size());
        assertTrue(messages.contains(message1));
        assertTrue(messages.contains(message2));

        verify(mockUserDAO, times(1)).findByUsernameIgnoreCase("user");
        verify(mockMessageDAO, times(1)).findBySenderOrReceiver(user, user);
    }

    @Test
    void testMarkAsRead() {
        Message message = new Message();
        message.setId(1L);
        message.setRead(false);

        when(mockMessageDAO.findById(1L)).thenReturn(Optional.of(message));

        messageService.markAsRead(1L);

        assertTrue(message.isRead());
        verify(mockMessageDAO, times(1)).findById(1L);
        verify(mockMessageDAO, times(1)).save(message);
    }
}
