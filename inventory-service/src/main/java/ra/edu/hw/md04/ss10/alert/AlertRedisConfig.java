package ra.edu.hw.md04.ss10.alert;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
public class AlertRedisConfig {
    @Bean
    public ChannelTopic pharmacyAlertsTopic() {
        return new ChannelTopic(AlertChannel.PHARMACY_ALERTS);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
        RedisConnectionFactory connectionFactory,
        AlertSubscriber alertSubscriber,
        ChannelTopic pharmacyAlertsTopic
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(alertSubscriber, pharmacyAlertsTopic);
        return container;
    }
}
