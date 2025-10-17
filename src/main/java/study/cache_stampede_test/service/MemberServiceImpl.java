package study.cache_stampede_test.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.cache_stampede_test.controller.MemberResponse;
import study.cache_stampede_test.controller.MemberSaveRequest;
import study.cache_stampede_test.repository.MemberEntity;
import study.cache_stampede_test.repository.MemberRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberCacheService memberCacheService;

    @Override
    @Transactional
    public MemberResponse saveMember(MemberSaveRequest request) {
        MemberEntity saved = memberRepository.save(MemberEntity.builder()
            .name(request.name())
            .age(request.age())
            .build());

        memberCacheService.deleteMembers();
        log.info("멤버 생성 -> 멤버리스트 변경 -> 캐시 무효화");

        return MemberResponse.from(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberResponse> findAllMembers() {
        List<MemberResponse> cachedMembers = memberCacheService.getMembers();
        if (cachedMembers != null) {
            log.info("캐시 데이터 조회");
            return cachedMembers;
        }

        log.info("DB 데이터 조회");
        List<MemberResponse> membersFromDb = memberRepository.findAll().stream()
            .map(MemberResponse::from)
            .collect(Collectors.toList());

        memberCacheService.setMembers(membersFromDb);
        log.info("캐시 저장");

        return membersFromDb;
    }
}
