package study.cache_stampede_test.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.cache_stampede_test.service.MemberService;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MemberResponse> saveMember(@RequestBody MemberSaveRequest request) {
        MemberResponse response = memberService.saveMember(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> getMembers() {
        List<MemberResponse> members = memberService.findAllMembers();
        return ResponseEntity.ok(members);
    }

    @GetMapping("/v2")
    public ResponseEntity<List<MemberResponse>> getMembersV2() {
        List<MemberResponse> members = memberService.findAllMembersV2();
        return ResponseEntity.ok(members);
    }
}
