package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity.CacheInvalidateEvent;

@Service
@RequiredArgsConstructor
public class RabbitMQSender {

    private final AmqpTemplate amqpTemplate;

    private static final String QUEUE_NAME="cache.invalidate.queue";

    public void sendCacheInvalidate(String key) {
        CacheInvalidateEvent event= new CacheInvalidateEvent(key);
        amqpTemplate.convertAndSend(QUEUE_NAME,event);
    }
}
