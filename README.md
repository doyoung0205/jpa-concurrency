### JPA 동시성 이슈를 다루는 프로젝트

### 요구사항
- [x] 하루에 최대 두명까지 예약을 할 수 있음 !
- [ ] 여러개의 WAS 서버에서도 위의 요구사항을 만족할 수 있는가?

### 해결할 수 있을 것 같은 방법 
1. jpa lock 
2. redis
3. 넌블록킹


 ### 내용 정리

- 낙관적 잠금 (Optimistic locking)
  - 각 트랜잭션이 같은 레코드를 변경할 가능성은 상당히 희박할 것이라고 가정한다.
  - 우선 변경 작업을 수행하고 마지막에 잠금 충돌이 있었는지 확인해 문게자 있다면 ROLLBACK 처리하는 방식
- 비관적 잠금 (Pessimistic locking)
  - 현재 트랜잭션에서 변경하고자 하는 레코드에 대해 잠금을 획득하고 변경 작업을 처리하는 방식
  - 현재 변경하고자 하는 레코드를 다른 트랜잭션에서도 변경할 수 있다라는 비판적인 가정을 하기 때문에 먼저 잠금을 획득.
  - 그래서 비관적 잠금이라고 부른다.
  - 일반적으로 높은 동시성 처리에는 비관적 잠금이 유리하다고 알려져 있으며 InnoDB는 비관적 잠금 방식을 채택하고 있다.

- Open session in view


```

@Lock

/**
 *  Synonymous with <code>OPTIMISTIC</code>.
 *  <code>OPTIMISTIC</code> is to be preferred for new
 *  applications.
 *
 */
READ,

/**
 *  Synonymous with <code>OPTIMISTIC_FORCE_INCREMENT</code>.
 *  <code>OPTIMISTIC_FORCE_IMCREMENT</code> is to be preferred for new
 *  applications.
 *
 */
WRITE,

/**
 * Optimistic lock.
 *
 * @since 2.0
 */
OPTIMISTIC,

/**
 * Optimistic lock, with version update.
 *
 * @since 2.0
 */
OPTIMISTIC_FORCE_INCREMENT,

/**
 *
 * Pessimistic read lock.
 *
 * @since 2.0
 */
PESSIMISTIC_READ,

/**
 * Pessimistic write lock.
 *
 * @since 2.0
 */
PESSIMISTIC_WRITE,

/**
 * Pessimistic write lock, with version update.
 *
 * @since 2.0
 */
PESSIMISTIC_FORCE_INCREMENT,

/**
 * No lock.
 *
 * @since 2.0
 */
NONE
```


### 텍스트 코드 
`ReservationServiceConcurrencyTest.java`

### 자료조사

* https://hyojabal.tistory.com/m/3
* RealMysql5.7


