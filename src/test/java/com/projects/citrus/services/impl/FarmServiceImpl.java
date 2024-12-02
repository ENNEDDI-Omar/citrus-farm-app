package com.projects.citrus.services.impl;

import com.projects.citrus.domain.entities.Farm;
import com.projects.citrus.dto.requests.FarmRequest;
import com.projects.citrus.dto.responses.FarmResponse;
import com.projects.citrus.mapper.FarmMapper;
import com.projects.citrus.repositories.FarmRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FarmServiceImplTest {
    @Mock
    private FarmRepository farmRepository;

    @Mock
    private FarmMapper farmMapper;

    @InjectMocks
    private FarmServiceImpl farmService;

    @Test
    void createFarm_ValidRequest_ReturnsFarmResponse() {
        // Arrange
        FarmRequest request = FarmRequest.builder()
                .name("Test Farm")
                .location("Test Location")
                .area(100.0)
                .build();

        Farm farm = new Farm();
        FarmResponse expectedResponse = FarmResponse.builder()
                .id(1L)
                .name("Test Farm")
                .build();

        when(farmMapper.toEntity(request)).thenReturn(farm);
        when(farmRepository.save(farm)).thenReturn(farm);
        when(farmMapper.toResponse(farm)).thenReturn(expectedResponse);

        // Act
        FarmResponse actualResponse = farmService.create(request);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getId(), actualResponse.getId());
        assertEquals(expectedResponse.getName(), actualResponse.getName());

        verify(farmMapper).toEntity(request);
        verify(farmRepository).save(farm);
        verify(farmMapper).toResponse(farm);
    }
}
