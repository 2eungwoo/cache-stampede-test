package study.cache_stampede_test.controller;

import study.cache_stampede_test.repository.MemberEntity;

public record MemberResponse(
    Long id,
    String name,
    String age
) {
    public static MemberResponse from(MemberEntity memberEntity) {
        return new MemberResponse(
            memberEntity.getId(),
            memberEntity.getName(),
            memberEntity.getAge()
        );
    }
}
