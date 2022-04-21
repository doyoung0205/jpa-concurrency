package me.doyoung.jpaconcurrency.treatment.infra;

import me.doyoung.jpaconcurrency.treatment.domain.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;

public interface TreatmentRepository extends JpaRepository<Treatment, Long> {

    @Query("SELECT t FROM Treatment t WHERE t.id = :id")
    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    Treatment getByIdWithOptimisticForceIncrement(@Param("id") Long id);

    @Query("SELECT t FROM Treatment t WHERE t.id = :id")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "5000")})
    Treatment getByIdWithPessimistic(@Param("id") Long id);
    
}
