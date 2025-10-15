package study.cache_stampede_test;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.cachestampede.service.MemberService;

@RestController
@RequestMapping("/data")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/{key}")
    public ResponseEntity<String> getData(@PathVariable String key) {
        String data = memberService.getData(key);
        if (data != null) {
            return ResponseEntity.ok(data);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Void> saveData(@RequestBody DataRequest request) {
        memberService.saveData(request.getKey(), request.getValue());
        return ResponseEntity.ok().build();
    }
}
