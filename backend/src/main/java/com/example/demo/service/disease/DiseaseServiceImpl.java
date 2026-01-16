package com.example.demo.service.disease;

import com.example.demo.model.Disease;
import com.example.demo.repository.DiseaseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DiseaseServiceImpl implements DiseaseService {

    private final DiseaseRepository diseaseRepository;

    @Override
    public List<Disease> search(String query) {

        List<Disease> byCode = diseaseRepository.findByDiseaseCodeContainingIgnoreCase(query);
        List<Disease> byName = diseaseRepository.findByNameContainingIgnoreCase(query);
        Set<Disease> result = new LinkedHashSet<>();
        result.addAll(byName);
        result.addAll(byCode);
        return new ArrayList<>(result);
    }


    @Override
    public Disease getById(UUID id) {
        return diseaseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Disease not found"));
    }

    @Override
    public Disease addDisease(Disease disease) {
        return diseaseRepository.save(disease);
    }

    @Override
    public Disease update(Disease disease) {
        return diseaseRepository.save(disease);
    }

    @Override
    public void deleteDisease(UUID id) {
        diseaseRepository.deleteById(id);
    }
}