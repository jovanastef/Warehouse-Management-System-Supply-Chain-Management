package poslovne.aplikacije;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import poslovne.aplikacije.messaging.MessagingReportingService;

@Configuration
public class RabbitMQConfigurator {
    public static final String PROIZVODI_TOPIC_EXCHANGE_NAME = "proizvodi-events-exchange";
    public static final String PROIZVODI_SERVICE_QUEUE = "proizvodi-service-queue";
    
    @Bean
    Queue queue() {
        return new Queue(PROIZVODI_SERVICE_QUEUE, false);
    }
    
    @Bean
    TopicExchange exchange() {
        return new TopicExchange(PROIZVODI_TOPIC_EXCHANGE_NAME);
    }
    
    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("proizvodi.events.#");
    }
    
    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(PROIZVODI_SERVICE_QUEUE);
        container.setMessageListener(listenerAdapter);
        return container;
    }
    
    @Bean
    MessageListenerAdapter listenerAdapter(MessagingReportingService receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }
}