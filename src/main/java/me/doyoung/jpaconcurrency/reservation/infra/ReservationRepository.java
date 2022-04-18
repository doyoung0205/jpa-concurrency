package me.doyoung.jpaconcurrency.reservation.infra;

import me.doyoung.jpaconcurrency.reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.time.LocalDateTime;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Lock(value = LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    @Query(value = "select count(r.id) from Reservation r where r.treatmentId = :treatmentId and  r.reservationDate >= :startDateTime and r.reservationDate < :endDateTime")
    int countByTreatmentIdAndTodayWithLock(@Param("treatmentId") Long treatmentId,
                                           @Param("startDateTime") LocalDateTime startDateTime,
                                           @Param("endDateTime") LocalDateTime endDateTime);

}
