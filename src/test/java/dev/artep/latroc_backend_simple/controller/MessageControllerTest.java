package dev.artep.latroc_backend_simple.controller;

import dev.artep.latroc_backend_simple.dto.MessageBody;
import dev.artep.latroc_backend_simple.model.Message;
import dev.artep.latroc_backend_simple.service.JWTService;
import dev.artep.latroc_backend_simple.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MessageControllerTest {

    private MockMvc mockMvc;
    private MessageService messageService;
    private JWTService jwtService;

    @BeforeEach
    void setUp() {
        messageService = mock(MessageService.class);
        jwtService = mock(JWTService.class);
        MessageController messageController = new MessageController(messageService, jwtService);
        mockMvc = MockMvcBuilders.standaloneSetup(messageController).build();
    }

    @Test
    void testSendMessage() throws Exception {
        MessageBody messageBody = new MessageBody();
        messageBody.setReceiverUsername("receiver");
        messageBody.setContent("Hello");

        Message message = new Message();
        message.setContent("Hello");

        when(jwtService.getUsername(anyString())).thenReturn("sender");
        when(messageService.sendMessage(anyString(), anyString(), anyString())).thenReturn(message);

        mockMvc.perform(post("/messages/send")
                        .header("Authorization", "Bearer testToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"receiverUsername\":\"receiver\",\"content\":\"Hello\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("Hello"));

        verify(messageService, times(1)).sendMessage(anyString(), anyString(), anyString());
    }

    @Test
    void testGetMessages() throws Exception {
        Message message1 = new Message();
        Message message2 = new Message();
        List<Message> messages = Arrays.asList(message1, message2);

        when(jwtService.getUsername(anyString())).thenReturn("user");
        when(messageService.getMessages(anyString())).thenReturn(messages);

        mockMvc.perform(get("/messages")
                        .header("Authorization", "Bearer testToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{}, {}]"));

        verify(messageService, times(1)).getMessages("user");
    }

    @Test
    void testMarkAsRead() throws Exception {
        mockMvc.perform(put("/messages/1/read"))
                .andExpect(status().isNoContent());

        verify(messageService, times(1)).markAsRead(1L);
    }
}
