package me.doyoung.jpaconcurrency.treatment.application;

import lombok.RequiredArgsConstructor;
import me.doyoung.jpaconcurrency.treatment.domain.Treatment;
import me.doyoung.jpaconcurrency.treatment.dto.TreatmentDtos;
import me.doyoung.jpaconcurrency.treatment.infra.TreatmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class TreatmentService {
    private final TreatmentRepository treatmentRepository;

    public TreatmentDtos.Response save(TreatmentDtos.Request request) {
        final Treatment savedTreatment = treatmentRepository.save(new Treatment(request.getName()));
        return new TreatmentDtos.Response(savedTreatment);
    }

    public Treatment getByIdWithOptimisticForceIncrement(Long id) {
        return treatmentRepository.getByIdWithOptimisticForceIncrement(id);
    }
}
