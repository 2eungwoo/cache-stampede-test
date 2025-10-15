package study.cache_stampede_test.service

import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import spock.lang.Specification
import study.cache_stampede_test.repository.MemberEntity
import study.cache_stampede_test.repository.MemberRepository
import study.cache_stampede_test.controller.MemberResponse

import static org.mockito.Mockito.when

class GetMemberListSpec extends Specification {

    @Mock
    MemberRepository memberRepository

    @InjectMocks
    MemberServiceImpl memberService

    def setup() {
        MockitoAnnotations.openMocks(this)
    }

    def "회원 목록 조회 - 정상 반환"() {
        given:
        def members = [
                new MemberEntity("a", "20"),
                new MemberEntity("b", "25")
        ]
        when(memberRepository.findAll()).thenReturn(members)

        when:
        def result = memberService.findAllMembers()

        then:
        result.size() == 2
        result[0].name() == "a"
        result[1].age() == "25"
    }
}