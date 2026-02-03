import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    stages: [
        { duration: '30s', target: 20 }, // 점진적으로 트래픽 증가
        { duration: '1m', target: 20 },  // 부하 유지
        { duration: '30s', target: 0 },  // 점진적으로 트래픽 감소
    ],
    thresholds: {
        http_req_duration: ['p(95)<2000'], // 95%의 요청이 2초 이내 처리되어야 함
        http_req_failed: ['rate<0.01'],    // 1% 미만의 실패율 허용
    },
};

const BASE_URL = 'http://host.docker.internal:8080/board';
const CONTENT_TYPE = 'application/json';

export default function () {
    // 1. Create (POST) 테스트
    const createPayload = JSON.stringify({
        title: `Performance Test Title ${Date.now()}`,
        content: `Performance Test Content ${Date.now()}`,
        name: "test"
    });

    const createRes = http.post(`${BASE_URL}`, createPayload, {
        headers: { 'Content-Type': CONTENT_TYPE },
    });

    check(createRes, {
        'Create status is 200': (r) => r.status === 200,
    });

    // 생성된 ID 추출 (응답에서 ID를 반환한다고 가정)
    const createdId = createRes.json('id');

    sleep(1);

    // 2. Read (GET) 테스트
    const readRes = http.get(`${BASE_URL}/${createdId}`);

    check(readRes, {
        'Read status is 200': (r) => r.status === 200,
        'Read returns correct data': (r) => r.json('title') != null,
    });

    sleep(1);

    // 3. Read All (GET) 테스트
    const readAllRes = http.get(`${BASE_URL}`);

    check(readAllRes, {
        'Read All status is 200': (r) => r.status === 200,
        'Read All returns array': (r) => Array.isArray(r.json()),
    });

    sleep(1);

    // 4. Update (PUT) 테스트
    const updatePayload = JSON.stringify({
        id: createdId,
        title: `Updated Title ${Date.now()}`,
        content: `Updated Content ${Date.now()}`
    });

    const updateRes = http.patch(`${BASE_URL}`, updatePayload, {
        headers: { 'Content-Type': CONTENT_TYPE },
    });

    check(updateRes, {
        'Update status is 200': (r) => r.status === 200,
    });

    sleep(1);

    // 5. Delete (DELETE) 테스트
    const deleteRes = http.del(`${BASE_URL}/${createdId}`);

    check(deleteRes, {
        'Delete status is 200': (r) => r.status === 200,
    });

    sleep(1);
}