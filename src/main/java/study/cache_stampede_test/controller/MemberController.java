package study.cache_stampede_test.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.cache_stampede_test.repository.MemberEntity;
import study.cache_stampede_test.service.MemberService;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/members")
    public ResponseEntity<MemberEntity> createMember(@RequestBody MemberSaveRequest request) {
        MemberEntity savedMember = memberService.save(request);
        return ResponseEntity.ok(savedMember);
    }
}
