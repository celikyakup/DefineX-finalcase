package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity.CacheInvalidateEvent;

@Service
@RequiredArgsConstructor
public class RabbitMQListener {
    private final RedisTemplate<String,Object> redisTemplate;

    @RabbitListener(queues = "cache.invalidate.queue")
    public void onMessage(CacheInvalidateEvent event) {
        redisTemplate.delete(event.getCacheKey());
    }
}
