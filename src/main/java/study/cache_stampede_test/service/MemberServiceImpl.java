package study.cache_stampede_test.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

    private final RedissonClient redissonClient;
    private final ConcurrentHashMap<String, String> inMemoryDb = new ConcurrentHashMap<>();

    public MemberServiceImpl(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
        inMemoryDb.put("initialKey", "initialValue");
    }

    @Override
    public String getData(String key) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        String cachedData = bucket.get();

        if (cachedData != null) {
            return cachedData;
        }

        String dataFromDb = inMemoryDb.get(key);
        if (dataFromDb != null) {
            bucket.set(dataFromDb, 5, TimeUnit.MINUTES);
            return dataFromDb;
        }

        return null;
    }

    @Override
    public void saveData(String key, String value) {
        // Save to "in-memory DB"
        inMemoryDb.put(key, value);

        // Update cache
        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.set(value, 5, TimeUnit.MINUTES); // Update with new value and TTL
    }
}
