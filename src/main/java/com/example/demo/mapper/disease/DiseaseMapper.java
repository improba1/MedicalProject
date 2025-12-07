package com.example.demo.mapper.disease;

import com.example.demo.dto.request.disease.AddDiseaseRequest;
import com.example.demo.dto.request.disease.UpdateDiseaseRequest;
import com.example.demo.dto.response.DiseaseResponse;
import com.example.demo.model.Disease;
import org.springframework.stereotype.Component;

@Component
public class DiseaseMapper {

    public Disease toEntity(AddDiseaseRequest request) {
        return Disease.builder()
                .name(request.getName())
                .diseaseCode(request.getDiseaseCode())
                .build();
    }

    public void updateEntity(Disease disease, UpdateDiseaseRequest request) {
        if (request.getName() != null) {
            disease.setName(request.getName());
        }
        if (request.getDiseaseCode() != null) {
            disease.setDiseaseCode(request.getDiseaseCode());
        }
    }

    public DiseaseResponse toResponse(Disease disease) {
        return DiseaseResponse.builder()
                .id(disease.getId())
                .name(disease.getName())
                .diseaseCode(disease.getDiseaseCode())
                .build();
    }
}