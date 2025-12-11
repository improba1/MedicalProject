package com.example.demo.service.disease;

import com.example.demo.model.Disease;
import com.example.demo.repository.DiseaseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DiseaseServiceImpl implements DiseaseService {

    private final DiseaseRepository diseaseRepository;

    @Override
    public List<Disease> searchByCode(String codePart) {
        return diseaseRepository.findByDiseaseCodeContainingIgnoreCase(codePart);
    }

    @Override
    public List<Disease> searchByName(String namePart) {
        return diseaseRepository.findByNameContainingIgnoreCase(namePart);
    }

    @Override
    public List<Disease> search(String query) {
        List<Disease> byCode = diseaseRepository.findByDiseaseCodeContainingIgnoreCase(query);
        List<Disease> byName = diseaseRepository.findByNameContainingIgnoreCase(query);

        byName.addAll(byCode.stream()
                .filter(d -> !byName.contains(d))
                .toList());

        return byName;
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