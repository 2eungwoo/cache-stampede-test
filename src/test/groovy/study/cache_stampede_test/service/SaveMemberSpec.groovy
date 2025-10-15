package study.cache_stampede_test.service

import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import spock.lang.Specification
import study.cache_stampede_test.controller.MemberSaveRequest
import study.cache_stampede_test.repository.MemberEntity
import study.cache_stampede_test.repository.MemberRepository

import static org.mockito.ArgumentMatchers.any
import static org.mockito.Mockito.when

class SaveMemberSpec extends Specification {

    @Mock
    MemberRepository memberRepository

    @InjectMocks
    MemberServiceImpl memberService

    def setup() {
        MockitoAnnotations.openMocks(this)
    }

    def "회원 저장 로직 - 정상 저장"() {
        given:
        def request = new MemberSaveRequest("a", "20")
        def savedEntity = new MemberEntity("a", "20")

        when(memberRepository.save(any(MemberEntity))).thenReturn(savedEntity)

        when:
        def response = memberService.saveMember(request)

        then:
        response != null
        response.name() == "a"
        response.age() == "20"
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