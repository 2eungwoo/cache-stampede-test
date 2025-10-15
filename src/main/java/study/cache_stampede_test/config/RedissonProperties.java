package study.cache_stampede_test.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("redisson")
public class RedissonProperties {
    private String address;
    private int connectionMinimumIdleSize;
    private int connectionPoolSize;
}
