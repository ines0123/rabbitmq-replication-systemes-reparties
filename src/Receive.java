import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

public class Receive {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        // Establishing connection factory
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        // Creating connection and channel
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // Declaring the queue
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        // Creating a callback to handle incoming messages
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [x] Received '" + message + "'");
        };

        // Starting a consumer and consuming messages from the queue
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
        });
    }
}
