package study.cache_stampede_test.service;

public interface MemberService {
    String getData(String key);
    void saveData(String key, String value);
}
