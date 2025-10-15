package study.cache_stampede_test

import spock.lang.Specification

class TestSpec extends Specification {

    def "1더하기1은2다"() {
        given:
        int a = 1
        int b = 1

        when:
        int result = a + b

        then:
        result == 2
    }
}