package com.petcare.PetCare.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class NotificacaoTeste {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping("/notify")
    public ResponseEntity<Void> sendNotification(@RequestBody String message) {
        messagingTemplate.convertAndSend("/topic/notifications", message);
        System.out.println(message);
        return ResponseEntity.ok().build();
    }
}
