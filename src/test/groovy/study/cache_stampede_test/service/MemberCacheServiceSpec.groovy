package study.cache_stampede_test.service

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import spock.lang.Specification
import study.cache_stampede_test.controller.MemberResponse

import java.util.concurrent.TimeUnit

class MemberCacheServiceSpec extends Specification {

    def redisTemplate = Mock(RedisTemplate)
    def valueOperations = Mock(ValueOperations)
    def memberCacheService

    def setup() {
        redisTemplate.opsForValue() >> valueOperations
        memberCacheService = new MemberCacheService(redisTemplate)
    }

    def "getMembers는 캐시 키로 데이터 조회"() {
        given:
        def expectedMembers = [new MemberResponse(1L, "test-user", "20")]
        valueOperations.get("MEMBER_LIST") >> expectedMembers

        when:
        def members = memberCacheService.getMembers()

        then:
        1 * valueOperations.get("MEMBER_LIST")
        members == expectedMembers
    }

    def "setMembers는 올바른 키, 값, 만료 시간으로 데이터 저장"() {
        given:
        def membersToCache = [new MemberResponse(1L, "test-user", "20")]

        when:
        memberCacheService.setMembers(membersToCache)

        then:
        1 * valueOperations.set("MEMBER_LIST", membersToCache, 10, TimeUnit.SECONDS)
    }

    def "deleteMembers가 캐시 키로 데이터 삭제"() {
        when:
        memberCacheService.deleteMembers()

        then:
        1 * redisTemplate.delete("MEMBER_LIST")
    }
}
