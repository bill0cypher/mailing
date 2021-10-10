package com.finance.finmailing.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.finmailing.dto.MessageDTO;
import com.finance.finmailing.dto.RegisterDTO;
import com.finance.finmailing.props.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailingService {

    private static final String GREETINGS_TEMPLATE_SUBJECT="Welcome to Finance stat loader";
    private static final String GREETINGS_TEMPLATE_TEXT="Hi %s, you're welcome to Finance stat loader. \nFrom now, you're able to track companies info.";

    private final JavaMailSender javaMailSender;
    private final ApplicationProperties appProperties;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "signup", groupId = "signup-mail")
    public void signupMessageMailing(GenericMessage<String> message) {
        try {
            log.info("Converting incoming message ....");
            RegisterDTO registerDTO = objectMapper.readValue(message.getPayload(), RegisterDTO.class);
            Optional.of(registerDTO).ifPresent(msg -> sendMessage(msg.getEmail(), msg.getName()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String to, String fullName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(appProperties.getSender());
        message.setTo(to);
        message.setSubject(GREETINGS_TEMPLATE_SUBJECT);
        message.setText(String.format(GREETINGS_TEMPLATE_TEXT, fullName));
        javaMailSender.send(message);
        log.info("Message sent: {}", message);
    }
}
