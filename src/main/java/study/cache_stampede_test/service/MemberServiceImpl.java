package study.cache_stampede_test.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.cache_stampede_test.controller.MemberSaveRequest;
import study.cache_stampede_test.repository.MemberEntity;
import study.cache_stampede_test.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public MemberEntity save(MemberSaveRequest request) {
        MemberEntity member = new MemberEntity(request.name(), request.age());
        return memberRepository.save(member);
    }
}
