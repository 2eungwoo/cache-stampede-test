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

class MemberServiceSpec extends Specification {

    @Mock
    MemberRepository memberRepository

    @InjectMocks
    MemberServiceImpl memberService

    def setup() {
        MockitoAnnotations.openMocks(this)
    }

    def "회원 저장 로직 - 정상 저장"() {
        given:
        def request = new MemberSaveRequest("john", "20")
        def savedMember = new MemberEntity("john", "20")

        when(memberRepository.save(any(MemberEntity))).thenReturn(savedMember)

        when:
        def saved = memberService.save(request)

        then:
        saved != null
        saved.name == "john"
        saved.age == "20"
    }
}