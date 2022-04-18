package me.doyoung.jpaconcurrency.treatment.infra;

import me.doyoung.jpaconcurrency.treatment.domain.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TreatmentRepository extends JpaRepository<Treatment, Long> {

}
