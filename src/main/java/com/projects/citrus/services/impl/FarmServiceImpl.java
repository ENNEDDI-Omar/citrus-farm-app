package com.projects.citrus.services.impl;

import com.projects.citrus.domain.entities.Farm;
import com.projects.citrus.dto.requests.FarmRequest;
import com.projects.citrus.dto.responses.FarmResponse;
import com.projects.citrus.exceptions.ResourceNotFoundException;
import com.projects.citrus.exceptions.ValidationException;
import com.projects.citrus.mapper.FarmMapper;
import com.projects.citrus.repositories.FarmRepository;
import com.projects.citrus.services.interfaces.IFarmService;
import com.projects.citrus.util.ValidationUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FarmServiceImpl implements IFarmService {
    private final FarmRepository farmRepository;
    private final FarmMapper farmMapper;

    @Override
    public FarmResponse create(FarmRequest request) {
        ValidationUtil.validateFarmArea(request.getArea());
        Farm farm = farmMapper.toEntity(request);
        return farmMapper.toResponse(farmRepository.save(farm));
    }

    @Override
    public FarmResponse getById(Long id) {
        return farmRepository.findById(id)
                .map(farmMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Farm not found with id: " + id));
    }

    @Override
    public List<FarmResponse> getAll() {
        return farmRepository.findAll().stream()
                .map(farmMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public FarmResponse update(Long id, FarmRequest request) {
        Farm farm = farmRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Farm not found with id: " + id));
        farmMapper.updateFarmFromRequest(request, farm);
        return farmMapper.toResponse(farmRepository.save(farm));
    }

    @Override
    public void delete(Long id) {
        Farm farm = farmRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Farm not found with id: " + id));

        farmRepository.delete(farm);
    }

    @Override
    public Double calculateLeftArea(Long id) {
        Farm farm = farmRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Farm not found with id: " + id));
        return farm.calculateLeftArea();
    }

    @Override
    public List<FarmResponse> getByMinArea(Double minArea) {
        if (minArea <= 0) {
            throw new ValidationException("Minimum area must be positive");
        }
        return farmRepository.findByAreaGreaterThanEqual(minArea).stream()
                .map(farmMapper::toResponse)
                .toList();
    }

    @Override
    public List<FarmResponse> getByDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new ValidationException("Start date and end date must not be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new ValidationException("Start date must be before end date");
        }
        return farmRepository.findByCreationDateBetween(startDate, endDate).stream()
                .map(farmMapper::toResponse)
                .toList();
    }
}
