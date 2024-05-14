import com.rabbitmq.client.*;

import java.nio.charset.StandardCharsets;

public class ReceiveLogsDirect {
    private static final String EXCHANGE_NAME = "direct_logs";
    public static void main(String[] argv) throws Exception {
        // Establishing connection factory
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        // Creating connection and channel
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        // Declaring the exchange
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        // Declaring a queue with a random name
        String queueName = channel.queueDeclare().getQueue();
        if(argv.length < 1){
            System.err.println("Usage: ReceiveLogsDirect [info] [warning] [error]");
            System.exit(1);
        }
        for (String severity : argv) {
            // Binding the queue to the exchange with the routing key
            channel.queueBind(queueName, EXCHANGE_NAME, severity);
        }
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        // Creating a callback to handle incoming messages
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [x] Received '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };
        // Starting a consumer and consuming messages from the queue
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }
}
