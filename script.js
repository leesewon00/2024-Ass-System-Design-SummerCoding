import http from 'k6/http';
import {check, sleep} from 'k6';
import {Rate} from 'k6/metrics';

export let errorRate = new Rate('errors');
export let status200Rate = new Rate('status_200');
export let status429Rate = new Rate('status_429');
export let status400Rate = new Rate('status_400');

export let options = {
    insecureSkipTLSVerify: true,
    noConnectionReuse: false,
    summaryTrendStats: ["avg", "min", "med", "max", "p(95)", "p(99)"], // 99 직접 명시 위함

    stages: [
        {duration: '30s', target: 10},
        {duration: '3m', target: 10},
        {duration: '30s', target: 100},
        {duration: '3m', target: 100},
        {duration: '30s', target: 300},
        {duration: '3m', target: 300},
        {duration: '1m', target: 0},
    ],

    thresholds: {
        http_req_failed: ['rate<0.01'], // http errors should be less than 1%
        http_req_duration: ["p(99)<1000"], // 99% of requests should be below 1s
    },
};

export default function () {
    const endpoint = { url: `http://localhost/book`, method: 'GET' };

    // HTTP 요청 실행
    let res;
    if (endpoint.method === 'GET') {
        res = http.get(endpoint.url);
    }

    // 응답 체크
    const check200 = check(res, {
        'status is 200': (r) => r.status === 200,
    });
    const check429 = check(res, {
        'status is 429': (r) => r.status === 429,
    });
    const check400 = check(res, {
        'status is 400': (r) => r.status === 400,
    });

    // 메트릭 기록
    if (check200) {
        status200Rate.add(1);
    } else if (check429) {
        status429Rate.add(1);
    } else if (check400) {
        status400Rate.add(1);
    } else {
        errorRate.add(1);
    }

    sleep(1); // 실제 요청 모방
}
