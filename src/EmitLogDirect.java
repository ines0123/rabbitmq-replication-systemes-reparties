import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

public class EmitLogDirect {
    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] argv) throws Exception {
        // Establishing connection factory
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        // Creating connection and channel
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            // Declaring the exchange
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

            // Extract severity and message from command line arguments
            String severity = getSeverity(argv);
            String message = getMessage(argv);

            // Publishing the message to the exchange with routing key
            channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + severity + "':'" + message + "'");
        }
    }

    private static String getSeverity(String[] args) {
        if (args.length < 1)
            return "info"; // Default severity if not provided
        return args[0];
    }

    private static String getMessage(String[] args) {
        if (args.length < 2)
            return "Hello World!"; // Default message if not provided
        return String.join(" ", args).substring(args[0].length() + 1);
    }
}
