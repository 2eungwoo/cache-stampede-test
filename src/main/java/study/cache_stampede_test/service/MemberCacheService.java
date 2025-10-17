package study.cache_stampede_test.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import study.cache_stampede_test.controller.MemberResponse;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MemberCacheService {

    private static final String MEMBER_CACHE_KEY = "MEMBER_LIST";
    private final RedisTemplate<String, Object> redisTemplate;

    public List<MemberResponse> getMembers() {
        return (List<MemberResponse>) redisTemplate.opsForValue().get(MEMBER_CACHE_KEY);
    }

    public void setMembers(List<MemberResponse> members) {
        redisTemplate.opsForValue().set(MEMBER_CACHE_KEY, members, 3, TimeUnit.SECONDS);
    }

    public void deleteMembers() {
        redisTemplate.delete(MEMBER_CACHE_KEY);
    }
}
