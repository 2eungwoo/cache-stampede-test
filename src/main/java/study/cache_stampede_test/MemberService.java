package study.cache_stampede_test;

public interface MemberService {
    String getData(String key);
    void saveData(String key, String value);
}
