### JPA 동시성 이슈를 다루는 프로젝트

### 요구사항
- [x] 하루에 최대 두명까지 예약을 할 수 있음 !

### 참고 코드 
`ReservationServiceConcurrencyTest.java`

DATABASE LOCK 이랑 관련있을 줄 알았는데,
<br>
자바환경에서 여러스레드를 줄세울 수 있는 동기화로 해결했음.  


