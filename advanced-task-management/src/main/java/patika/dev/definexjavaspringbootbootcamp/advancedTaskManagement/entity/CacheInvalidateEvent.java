package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CacheInvalidateEvent {
    private String cacheKey;

}
