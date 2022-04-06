### JPA 동시성 이슈를 다루는 프로젝트

### 요구사항
- [x] 하루에 최대 두명까지 예약을 할 수 있음 !
- [ ] 여러개의 WAS 서버에서도 위의 요구사항을 만족할 수 있는가?

### 해결할 수 있을 것 같은 방법 
1. jpa lock 
2. redis
3. 넌블록킹


### 참고 코드 
`ReservationServiceConcurrencyTest.java`



### 자료조사

* https://hyojabal.tistory.com/m/3


### 느낀점
DATABASE LOCK 이랑 관련있을 줄 알았는데,
<br>
자바환경에서 여러스레드를 줄세울 수 있는 동기화로 해결했음.  


