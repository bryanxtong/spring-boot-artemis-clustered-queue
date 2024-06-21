package com.example.artemis.jms;

import jakarta.jms.ConnectionFactory;
import jakarta.jms.DeliveryMode;
import org.apache.activemq.artemis.api.core.DiscoveryGroupConfiguration;
import org.apache.activemq.artemis.api.core.UDPBroadcastEndpointFactory;
import org.apache.activemq.artemis.api.jms.ActiveMQJMSClient;
import org.apache.activemq.artemis.api.jms.JMSFactoryType;
import org.apache.activemq.artemis.core.client.impl.TopologyMemberImpl;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import java.util.Collection;

@Profile("clustered-queue")
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
        //String connectionLoadBalancingPolicyClassName = connectionFactory.getConnectionLoadBalancingPolicyClassName();
        return connectionFactory;
    }

    @Bean("jacksonJmsMessageConverter")// Serialize message content to json using TextMessage
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

    @Bean
    public JmsTemplate jmsTemplate(@Qualifier("udpConnectionFactory") ConnectionFactory connectionFactory, @Qualifier("jacksonJmsMessageConverter")  MessageConverter jacksonJmsMessageConverter){
        JmsTemplate jmsTemplate = new JmsTemplate();
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(connectionFactory);
        template.setMessageConverter(jacksonJmsMessageConverter);
        template.setPubSubDomain(false);
        template.setDeliveryMode(DeliveryMode.PERSISTENT);
        return template;
    }
}
