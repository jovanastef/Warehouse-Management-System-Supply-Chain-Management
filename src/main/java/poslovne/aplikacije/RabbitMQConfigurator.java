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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import poslovne.aplikacije.inventory.InventoryMessageListener;
import poslovne.aplikacije.messaging.MessagingReportingService;
import poslovne.aplikacije.orders.OrdersMessageListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

@Configuration
public class RabbitMQConfigurator {
    public static final String APP_EVENTS_EXCHANGE = "app-events-exchange";
    public static final String PROIZVODI_QUEUE = "proizvodi-queue";
    public static final String ORDERS_QUEUE = "orders-queue";
    public static final String INVENTORY_QUEUE = "inventory-queue";
    public static final String BILLING_QUEUE = "billing-queue";
    
    @Bean
    public TopicExchange appEventsExchange() {
        return new TopicExchange(APP_EVENTS_EXCHANGE);
    }
    
    @Bean
    public Queue proizvodiQueue() { return new Queue(PROIZVODI_QUEUE, false); }
    
    @Bean
    public Queue ordersQueue() { return new Queue(ORDERS_QUEUE, false); }
    
    @Bean
    public Queue inventoryQueue() { return new Queue(INVENTORY_QUEUE, false); }
    
    @Bean
    public Queue billingQueue() { return new Queue(BILLING_QUEUE, false); }
    
    @Bean
    public Binding proizvodiBinding(Queue proizvodiQueue, TopicExchange appEventsExchange) {
        return BindingBuilder.bind(proizvodiQueue).to(appEventsExchange).with("proizvodi.events.#");
    }
    
    @Bean
    public Binding ordersBinding(Queue ordersQueue, TopicExchange appEventsExchange) {
        return BindingBuilder.bind(ordersQueue).to(appEventsExchange).with("orders.events.#");
    }
    
    @Bean
    public Binding ordersPaymentBinding(Queue ordersQueue, TopicExchange appEventsExchange) {
        return BindingBuilder.bind(ordersQueue).to(appEventsExchange).with("billing.events.paymentConfirmed");
    }
    
    @Bean
    public Binding inventoryBinding(Queue inventoryQueue, TopicExchange appEventsExchange) {
        return BindingBuilder.bind(inventoryQueue).to(appEventsExchange).with("inventory.events.#");
    }
    
    @Bean
    public Binding inventoryGoodsSoldBinding(Queue inventoryQueue, TopicExchange appEventsExchange) {
        return BindingBuilder.bind(inventoryQueue).to(appEventsExchange).with("orders.events.goodsSold");
    }
    
    @Bean
    public Binding inventoryCancelBinding(Queue inventoryQueue, TopicExchange appEventsExchange) {
        return BindingBuilder.bind(inventoryQueue).to(appEventsExchange).with("orders.events.cancelReservation");
    }
    
    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(PROIZVODI_QUEUE, ORDERS_QUEUE, INVENTORY_QUEUE, BILLING_QUEUE);
        container.setMessageListener(listenerAdapter);
        return container;
    }
    
    @Bean
    public MessageListenerAdapter listenerAdapter(MessagingReportingService receiver) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(receiver, "receiveMessage");
        adapter.setMessageConverter(jsonMessageConverter());
        return adapter;
    }
    
    @Bean
    public MessageListenerAdapter ordersListenerAdapter(OrdersMessageListener receiver) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(receiver, "receiveMessage");
        adapter.setMessageConverter(jsonMessageConverter());
        return adapter;
    }
    
    @Bean
    public MessageListenerAdapter inventoryListenerAdapter(InventoryMessageListener receiver) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(receiver, "receiveMessage");
        adapter.setMessageConverter(jsonMessageConverter());
        return adapter;
    }
    
    @Bean
    public MessageConverter jsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}