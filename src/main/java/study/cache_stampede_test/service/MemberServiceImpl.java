package study.cache_stampede_test.service;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.cache_stampede_test.controller.MemberResponse;
import study.cache_stampede_test.controller.MemberSaveRequest;
import study.cache_stampede_test.repository.MemberEntity;
import study.cache_stampede_test.repository.MemberRepository;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private static final String LOCK_KEY = "MEMBER_LIST_LOCK";
    private final MemberRepository memberRepository;
    private final MemberCacheService memberCacheService;
    private final MeterRegistry meterRegistry;
    private final RedissonClient redissonClient;

    @Override
    @Transactional
    public MemberResponse saveMember(MemberSaveRequest request) {
        MemberEntity saved = memberRepository.save(MemberEntity.builder()
            .name(request.name())
            .age(request.age())
            .build());

        memberCacheService.deleteMembers();
        log.info("캐시가 무효화되었음");

        return MemberResponse.from(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberResponse> findAllMembers() {
        List<MemberResponse> cachedMembers = memberCacheService.getMembers();
        if (cachedMembers != null) {
            log.info("[v1] 캐시 데이터 조회");
            meterRegistry.counter("cache.gets", "name", "members", "result", "hit").increment();
            return cachedMembers;
        }

        log.info("[v1] DB 데이터 조회");
        meterRegistry.counter("cache.gets", "name", "members", "result", "miss").increment();

        List<MemberResponse> membersFromDb = memberRepository.findAll().stream()
            .map(MemberResponse::from)
            .collect(Collectors.toList());

        memberCacheService.setMembers(membersFromDb);
        meterRegistry.counter("cache.puts", "name", "members").increment();
        log.info("[v1] 캐시 데이터 저장");

        return membersFromDb;
    }

    @Override
    public List<MemberResponse> findAllMembersV2() {
        List<MemberResponse> cachedMembers = memberCacheService.getMembers();
        if (cachedMembers != null) {
            log.info("[v2] 캐시 데이터 조회");
            meterRegistry.counter("cache.gets", "name", "members.v2", "result", "hit").increment();
            return cachedMembers;
        }

        RLock lock = redissonClient.getLock(LOCK_KEY);
        try {
            boolean isLocked = lock.tryLock(10, 5, TimeUnit.SECONDS);
            if (!isLocked) {
                log.info("[v2] 락 획득 실패");
                Thread.sleep(100); // 잠시 후 다른 스레드가 채운 캐시를 읽도록 유도
                return memberCacheService.getMembers();
            }

            // 락 획득 후 캐시 재확인 (더블 체킹)
            cachedMembers = memberCacheService.getMembers();
            if (cachedMembers != null) {
                log.info("[v2] 캐시 데이터 조회");
                meterRegistry.counter("cache.gets", "name", "members.v2", "result", "hit").increment();
                return cachedMembers;
            }

            log.info("[v2] DB 데이터 조회");
            meterRegistry.counter("cache.gets", "name", "members.v2", "result", "miss").increment();
            List<MemberResponse> membersFromDb = memberRepository.findAll().stream()
                .map(MemberResponse::from)
                .collect(Collectors.toList());

            memberCacheService.setMembers(membersFromDb);
            meterRegistry.counter("cache.puts", "name", "members.v2").increment();
            log.info("[v2] 캐시 데이터 저장");
            return membersFromDb;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
