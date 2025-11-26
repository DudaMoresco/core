package com.duda.core.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

@Configuration
public class RabbitMQConfig {

    // Filas
    public static final String FILA_LEITURAS_BALANCA = "balanca.leituras";
    public static final String FILA_BALANCA_ESTABILIZADA = "balanca.estabilizada";
    public static final String FILA_PESAGEM_CONCLUIDA = "pesagem.concluida";

    // Exchange
    public static final String EXCHANGE_BALANCA = "balanca.exchange";

    // Routing Keys
    public static final String ROUTING_KEY_LEITURAS = "balanca.leituras";
    public static final String ROUTING_KEY_ESTABILIZADA = "balanca.estabilizada";
    public static final String ROUTING_KEY_CONCLUIDA = "balanca.concluida";


    // ========== FILAS ==========
    @Bean
    public Queue filaLeiturasBalanca() {
        return QueueBuilder
                .durable(FILA_LEITURAS_BALANCA)
                .withArgument("x-message-ttl", 60000) // TTL de 60s
                .build();
    }

    @Bean
    public Queue filaBalancaEstabilizada() {
        return QueueBuilder
                .durable(FILA_BALANCA_ESTABILIZADA)
                .build();
    }

    @Bean
    public Queue filaPesagemConcluida() {
        return QueueBuilder
                .durable(FILA_PESAGEM_CONCLUIDA)
                .build();
    }


    // ========== EXCHANGE ==========
    @Bean
    public TopicExchange exchangeBalanca() {
        return new TopicExchange(EXCHANGE_BALANCA);
    }


    // ========== BINDINGS ==========
    @Bean
    public Binding bindingLeituras(Queue filaLeiturasBalanca, TopicExchange exchangeBalanca) {
        return BindingBuilder
                .bind(filaLeiturasBalanca)
                .to(exchangeBalanca)
                .with(ROUTING_KEY_LEITURAS);
    }

    @Bean
    public Binding bindingEstabilizada(Queue filaBalancaEstabilizada, TopicExchange exchangeBalanca) {
        return BindingBuilder
                .bind(filaBalancaEstabilizada)
                .to(exchangeBalanca)
                .with(ROUTING_KEY_ESTABILIZADA);
    }

    @Bean
    public Binding bindingConcluida(Queue filaPesagemConcluida, TopicExchange exchangeBalanca) {
        return BindingBuilder
                .bind(filaPesagemConcluida)
                .to(exchangeBalanca)
                .with(ROUTING_KEY_CONCLUIDA);
    }


    // ========== CONVERSOR JSON ==========
    @Bean
    public MessageConverter conversorMensagemJson() {
        return new Jackson2JsonMessageConverter();
    }


    // ========== RABBIT TEMPLATE ==========
    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory,
            MessageConverter conversorMensagemJson
    ) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(conversorMensagemJson);
        return template;
    }
}
