package study.cache_stampede_test.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.cache_stampede_test.controller.MemberResponse;
import study.cache_stampede_test.controller.MemberSaveRequest;
import study.cache_stampede_test.repository.MemberEntity;
import study.cache_stampede_test.repository.MemberRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public MemberResponse saveMember(MemberSaveRequest request) {
        MemberEntity saved = memberRepository.save(MemberEntity.builder()
            .name(request.name())
            .age(request.age())
            .build());
        return MemberResponse.from(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberResponse> findAllMembers() {
        return memberRepository.findAll().stream()
            .map(MemberResponse::from)
            .collect(Collectors.toList());
    }
}
