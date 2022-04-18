package me.doyoung.jpaconcurrency.reservation.infra;

import me.doyoung.jpaconcurrency.reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query(value = "select count(r.id) from Reservation r where r.createdAt >= :startDateTime and r.createdAt < :endDateTime")
    int countByCreatedAtBetweenStartAndEndDateTime(@Param("startDateTime") LocalDateTime startDateTime,
                                                   @Param("endDateTime") LocalDateTime endDateTime);

    @Lock(value = LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    @Query(value = "select r from Reservation r where r.createdAt >= :startDateTime and r.createdAt < :endDateTime")
    List<Reservation> findByCreatedAtBetweenStartAndEndDateTimeWithLock(@Param("startDateTime") LocalDateTime startDateTime,
                                                                        @Param("endDateTime") LocalDateTime endDateTime);

}
