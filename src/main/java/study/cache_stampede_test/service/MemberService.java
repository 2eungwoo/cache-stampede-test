package study.cache_stampede_test.service;

import study.cache_stampede_test.controller.MemberSaveRequest;
import study.cache_stampede_test.repository.MemberEntity;

public interface MemberService {
    MemberEntity save(MemberSaveRequest request);
}
