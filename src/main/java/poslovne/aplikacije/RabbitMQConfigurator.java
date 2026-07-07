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
import poslovne.aplikacije.inventory.InventoryMessageListener;
import poslovne.aplikacije.messaging.MessagingReportingService;
import poslovne.aplikacije.orders.OrdersMessageListener;

@Configuration
public class RabbitMQConfigurator {

    // Exchange
    public static final String APP_EVENTS_EXCHANGE = "app-events-exchange";

    // Queues
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

    // Bindings
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

    // Listener Containers
    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(PROIZVODI_QUEUE, ORDERS_QUEUE, INVENTORY_QUEUE, BILLING_QUEUE);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    // Adapter za Proizvod events
    @Bean
    public MessageListenerAdapter listenerAdapter(MessagingReportingService receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    // Adapter za Orders events
    @Bean
    public MessageListenerAdapter ordersListenerAdapter(OrdersMessageListener receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    // Adapter za Inventory events
    @Bean
    public MessageListenerAdapter inventoryListenerAdapter(InventoryMessageListener receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }
}