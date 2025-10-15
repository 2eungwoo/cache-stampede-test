package study.cache_stampede_test.config;

import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RedissonConfig {

    private final RedissonProperties redissonProperties;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
            .setAddress(redissonProperties.getAddress())
            .setConnectionPoolSize(redissonProperties.getConnectionPoolSize())
            .setConnectionMinimumIdleSize(redissonProperties.getConnectionMinimumIdleSize());
        return Redisson.create(config);
    }
}