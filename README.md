### OptimisticLocking 을 이용한 최대인원 예약 동시성 처리하기

출처: https://doyoung.tistory.com/34 [귀찮다고 망설이지 말자.]

[블로그 링크](https://doyoung.tistory.com/34)

| 영어          | 한글  |
|-------------|-----|
| Treatment   | 진료  |
| Reservation | 예약  |

### 요구사항

- [x] 진료를 등록할 수 있다.
- [x] 해당 진료에 예약을 할 수 있다.
- [x] 하루에 진료의 최대 인원 까지 예약을 할 수 있다.
    - [x] 동시성 이슈가 나지 않도록 예약할 수 있다.
    - [x] 여러개의 WAS 서버에서도 위의 요구사항을 만족할 수 있는가?
