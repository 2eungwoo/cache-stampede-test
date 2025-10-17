import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  vus: 100, // 100명의 가상 유저
  duration: '30s', // 30초 동안 테스트 수행
};

export default function () {
  const res = http.get('http://localhost:8080/api/members/v2');
  check(res, { 'status was 200': (r) => r.status == 200 });
  sleep(1);
}
