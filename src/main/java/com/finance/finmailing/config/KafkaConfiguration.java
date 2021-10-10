package com.finance.finmailing.config;

import com.finance.finmailing.dto.RegisterDTO;
import com.finance.finmailing.props.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaConfiguration {

    private final ApplicationProperties appProperties;

    @Bean
    public KafkaAdmin kafkaAdminBean() {
        return new KafkaAdmin(Map.of(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, appProperties.getBootstrapServer()));
    }

    public ConsumerFactory<String, RegisterDTO> consumerFactoryBean() {
        return new DefaultKafkaConsumerFactory<>(
                Map.of(
                        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, appProperties.getBootstrapServer(),
                        ConsumerConfig.GROUP_ID_CONFIG, appProperties.getConsumerGroupId(),
                        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, MessageDeserializer.class
                )
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, RegisterDTO> consumerListenerContainerFactoryBean() {
        ConcurrentKafkaListenerContainerFactory<String, RegisterDTO> containerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        containerFactory.setConsumerFactory(consumerFactoryBean());
        return containerFactory;
    }
}
