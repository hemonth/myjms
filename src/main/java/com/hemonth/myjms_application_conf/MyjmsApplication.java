package com.hemonth.myjms_application_conf;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.jms.support.destination.DynamicDestinationResolver;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.jms.ConnectionFactory;

@SpringBootApplication
@EnableJms
@EnableScheduling
public class MyjmsApplication {

    @Value("${clientId:default_clientId}")
    String clientId;

    public static void main(String[] args) {
        SpringApplication.run(MyjmsApplication.class, args);
    }

    @Bean
    public ActiveMQProperties setActiveMQProperties(){
        ActiveMQProperties activeMQProperties = new ActiveMQProperties();
        activeMQProperties.setBrokerUrl("${spring.activemq.broker-url}");
        activeMQProperties.setUser("${spring.activemq.user}");
        activeMQProperties.setPassword("${spring.activemq.password}");
        return activeMQProperties;
    }
    
    @Bean
    @Primary
    public ConnectionFactory activeMqConnectionFactory(@Qualifier("setActiveMQProperties") ActiveMQProperties properties) {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(properties.getBrokerUrl());
        activeMQConnectionFactory.getPrefetchPolicy().setQueuePrefetch(1);
        RedeliveryPolicy redeliveryPolicy = activeMQConnectionFactory.getRedeliveryPolicy();
        redeliveryPolicy.setUseExponentialBackOff(true);
        redeliveryPolicy.setMaximumRedeliveries(4);
        return activeMQConnectionFactory;
    }

    @Bean
    public CachingConnectionFactory cachingConnectionFactory(@Qualifier("activeMqConnectionFactory") ConnectionFactory activeMqConnectionFactory) {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(activeMqConnectionFactory);
        cachingConnectionFactory.setCacheConsumers(true);
        cachingConnectionFactory.setCacheProducers(true);
        cachingConnectionFactory.setReconnectOnException(true);
        return cachingConnectionFactory;
    }

    @Bean
    public MessageConverter jacksonMessageConverter(){
        org.springframework.jms.support.converter.MappingJackson2MessageConverter converter = new org.springframework.jms.support.converter.MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_messageType");
        return converter;
    }

    @Bean
    @Primary
    public JmsTemplate queueJmsTemplate(@Qualifier("cachingConnectionFactory") CachingConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setDestinationResolver(new DynamicDestinationResolver());
        jmsTemplate.setMessageConverter(jacksonMessageConverter());
        return jmsTemplate;
    }

    @Bean
    public JmsTemplate topicJmsTemplate(@Qualifier("cachingConnectionFactory") CachingConnectionFactory connectionFactory){
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setPubSubDomain(true);
        jmsTemplate.setDestinationResolver(new DynamicDestinationResolver());
        jmsTemplate.setMessageConverter(jacksonMessageConverter());
        return jmsTemplate;
    }

    @Bean(name = "topicListnerConnectionFactory")
    public DefaultJmsListenerContainerFactory topicListnerConnectionFactory(@Qualifier("activeMqConnectionFactory") ConnectionFactory connectionFactory){
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setDestinationResolver(new DynamicDestinationResolver());
        factory.setConcurrency("1-1");
        factory.setClientId(clientId);
        factory.setSubscriptionDurable(true);
        factory.setCacheLevelName(String.valueOf(DefaultMessageListenerContainer.CACHE_AUTO));
        factory.setMessageConverter(jacksonMessageConverter());
        factory.setPubSubDomain(true);
        return factory;
    }



}
