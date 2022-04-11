### JPA 동시성 이슈를 다루는 프로젝트

### 요구사항

- [x] 하루에 최대 두명까지 예약을 할 수 있음 !
- [ ] 여러개의 WAS 서버에서도 위의 요구사항을 만족할 수 있는가?

### 해결할 수 있을 것 같은 방법

1. jpa lock
2. redis
3. 넌블록킹

### 내용 정리

#### Locking 이란

- Locking: RDB 에서 잠금은 데이터를 읽는 시간과 사용하는 시간 사이에 데이터가 변경되는 것을 방지하기 위해 취하는 조치.

#### Optimistic (낙관적)

- 각 트랜잭션이 같은 레코드를 변경할 가능성은 상당히 희박할 것이라고 가정한다.
- 우선 변경 작업을 수행하고 마지막에 잠금 충돌이 있었는지 확인해 문게자 있다면 ROLLBACK 처리하는 방식

애플리케이션이 긴 트랜잭션이나 여러 DB 트랜잭션에 걸쳐 있는 요청을 하는 경우 버전 관리 데이터를 저장할 수 있으므로 동일한 엔티티가 두 개의 요청에 의해 업데이트되는 경우 변경 사항을 커밋한 마지막 요청에게
알리고 다른 요청의 무시하지 않도록 할 수 있습니다.

이 접근 방식은 약간의 격리를 보장하지만 확장성이 뛰어나고 `read-often-write-somtimes` 상황에서 특히 잘 작동합니다.

`Hibernate`는 버전 정보를 저장하기 위한 두 가지 다른 매커니즘을 제공한다.

Entity 클래스 프로퍼티의 다음중 하나를 추가한다.

1. Version (순차 번호)
2. Timestamp

- `int` or `Integer`
- `short` or `Short`
- `long` or `Long`
- `java.sql.Timestamp`
- `Instant`

순차번호를 변경하기 위해서는 `LockModeType.OPTIMISTIC_FORCE_INCREMENT` or `LockModeType.PESSIMISTIC_FORCE_INCREMENT` 속성을 사용하면 된다.

혹은 `@OptimisticLocking` 를 통해서 Entity 클래스 구조를 변경하지 않고 해결할 수 있는 방법도 있다.

```
@OptimisticLocking(type = OptimisticLockType.ALL) // 모든 속성 고려
@DynamicUpdate // 모든 필드를 Optimistic locking의 조건으로 걸게되면 어떤게 dirty 필드인지 체크해서 where조건을 만들 필요가 있기 때문
public class entity { ... }


@OptimisticLocking(type = OptimisticLockType.DIRTY) // 변경된 엔티티 속성만 고려
@DynamicUpdate
@SelectBeforeUpdate
public class entity { ... }

OptimisticLockType.DIRTY는 현재 실행 중인 지속성 컨텍스트에서 엔터티가 로드된 이후 변경된 엔터티 속성만 고려한다는 점에서 OptimisticLockType.ALL과 다릅니다.
```


##### 낙관적 락에서 발생하는 예외
- `javax.persistence.OptimisticLockException` (jpa 예외)
- `org.hibernate.StaleObjectStateException` (하이버네이트 예외)
- `org.springframework.orm.ObjectOptimisticLockingFailureException` (스프링 예외 추상화)


##### OPTIMISTIC



```
em.find(Board.class, id, LockModeType.OPTIMISTIC); 
```


#### Pessimistic (비관적)

- 현재 트랜잭션에서 변경하고자 하는 레코드에 대해 잠금을 획득하고 변경 작업을 처리하는 방식
- 현재 변경하고자 하는 레코드를 다른 트랜잭션에서도 변경할 수 있다라는 비판적인 가정을 하기 때문에 먼저 잠금을 획득.
- 그래서 비관적 잠금이라고 부른다.
- 일반적으로 높은 동시성 처리에는 비관적 잠금이 유리하다고 알려져 있으며 InnoDB는 비관적 잠금 방식을 채택하고 있다.

#### LOCK MODE TYPE

- `NONE` : LOCK 이 없다. 트랜잭션이 끝나면 모든 개체가 이 잠금 모드로 전환. update() 또는 saveOrUpdate() 호출을 통해 세션과 연결된 개체도 이 잠금 모드에서 시작.
- `READ` and `OPTIMISTIC` : 엔티티 버전은 현재 실행 중인 트랜잭션이 끝날 때 확인합니다.
- `OPTIMISTIC_FORCE_INCREMENT` :  엔티티가 변경되지 않은 경우에도 엔티티 버전이 자동으로 증가합니다.
- `PESSIMISTIC_FOUCE_INCREMENT` :  엔티티는 비관적으로 잠겨 있으며 엔티티가 변경되지 않는 경우에도 해당 버전이 자동으로 증가합니다.
- `PESSIMISTIC_READ` : DB 에서 지원하는 경우 엔티티는 공유 잠금을 사용하여 비관적으로 잠깁니다. 그렇지 않으면 명시적 잠금이 사용됩니다.
- `PESSIMISTIC_WRITE` : 명시적 잠금을 사용하여 잠깁니다.
- `PESSIMISTIC_WRITE with a javax.persistence.lock.timeout setting of 0` : 행이 이미 잠겨 있으면 잠금 획득 요청이 빠르게 실패합니다.
- `PESSIMISTIC_WRITE with a javax.persistence.lock.timeout setting of -2` : 잠금 획득 요청이 이미 잠긴 행을 건너뜁니다. Oracle 및
  PostgreSQL 9.5에서 SELECT ... FOR UPDATE SKIP LOCKED를 사용하거나 SQL Server에서 (rowlock, updlock, readpast)와 함께 SELECT ...를
  사용합니다.

```

entityManager.find(
	Person.class, id, LockModeType.PESSIMISTIC_WRITE,
	Collections.singletonMap( "javax.persistence.lock.timeout", 200 )
);

```

#### 명시적 잠금

프로그램을 통해 의도적으로 잠금을 실행하는 것이 명시적 잠금입니다. JPA에서 엔터티를 조회할 때 LockMode를 지정하거나 select for update 쿼리를 통해서 직접 잠금을 지정할 수 있습니다.

#### 묵시적 잠금

암시적 잠금은 프로그램 코드상에 명시적으로 지정하지 않아도 잠금이 발생하는 것을 의미합니다. JPA에서는 엔터티에 @Version이 붙은 필드가 존재하거나 @OptimisticLocking 어노테이션이 설정되어 있을
경우 자동적으로 충돌감지를 위한 잠금이 실행됩니다. 그리고 데이터베이스의 경우에는 일반적으로 사용하는 대부분의 데이터베이스가 업데이트, 삭제 쿼리 발행시에 암시적으로 해당 로우에 대한 행 배타잠금(Row
Exclusive Lock)이 실행됩니다. JPA의 충돌감지가 역할을 할 수 있는 것도 이와 같은 데이터베이스의 암시적 잠금이 존재하기 때문입니다. 데이터베이스의 암시적 잠금이 없다면 충돌감지를 통과한 후 커밋(
Commit)이 실행되는 사이에 틈이 생기므로 충돌감지를 하더라도 정합성을 보증할 수 없을 것입니다.

- Open session in view

### 동시성 테스트 코드

`ReservationServiceConcurrencyTest.java`

### 출처

* RealMysql5.7
* https://docs.jboss.org/hibernate/orm/5.6/userguide/html_single/Hibernate_User_Guide.html#locking
* 자바 ORM 표준 JPA 프로그래밍
* https://www.logicbig.com/tutorials/spring-framework/spring-data/pessimistic-locking-and-lock-annotation.html
* https://reiphiel.tistory.com/entry/understanding-jpa-lock [레이피엘의 블로그]
