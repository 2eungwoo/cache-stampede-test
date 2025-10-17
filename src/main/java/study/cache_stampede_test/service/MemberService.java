package study.cache_stampede_test.service;

import study.cache_stampede_test.controller.MemberResponse;
import study.cache_stampede_test.controller.MemberSaveRequest;

import java.util.List;

public interface MemberService {

    MemberResponse saveMember(MemberSaveRequest request);

    List<MemberResponse> findAllMembers();

    List<MemberResponse> findAllMembersV2();
}
