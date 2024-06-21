package com.example.artemis.jms;

import jakarta.jms.ConnectionFactory;
import org.apache.activemq.artemis.api.core.DiscoveryGroupConfiguration;
import org.apache.activemq.artemis.api.core.UDPBroadcastEndpointFactory;
import org.apache.activemq.artemis.api.jms.ActiveMQJMSClient;
import org.apache.activemq.artemis.api.jms.JMSFactoryType;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@Profile("!local")
@Configuration
@EnableJms
public class JmsClusteredConfig {
    @Bean("udpConnectionFactory")
    public ConnectionFactory udpConnectionFactory() {
        UDPBroadcastEndpointFactory factory = new UDPBroadcastEndpointFactory();
        factory.setGroupAddress("231.7.7.7").setGroupPort(9876);
        DiscoveryGroupConfiguration configuration = new DiscoveryGroupConfiguration();
        configuration.setBroadcastEndpointFactory(factory);
        ActiveMQConnectionFactory connectionFactory = ActiveMQJMSClient.createConnectionFactoryWithoutHA(configuration, JMSFactoryType.QUEUE_XA_CF);
        return connectionFactory;
    }

    @Bean("jacksonJmsMessageConverter")// Serialize message content to json using TextMessage
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

    @Bean("pubSubFactory")
    public JmsListenerContainerFactory<?> pubSubFactory(@Qualifier("udpConnectionFactory") ConnectionFactory connectionFactory,
                                                        @Qualifier("jacksonJmsMessageConverter") MessageConverter jacksonJmsMessageConverter,
                                                        DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setPubSubDomain(false);
        factory.setConcurrency("3-5");
        factory.setMessageConverter(jacksonJmsMessageConverter);
        configurer.configure(factory, connectionFactory);
        return factory;
    }

}
