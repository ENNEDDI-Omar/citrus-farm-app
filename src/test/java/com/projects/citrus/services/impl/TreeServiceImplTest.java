package com.projects.citrus.services.impl;

import com.projects.citrus.domain.entities.Field;
import com.projects.citrus.domain.entities.Tree;
import com.projects.citrus.dto.requests.TreeRequest;
import com.projects.citrus.dto.responses.TreeResponse;
import com.projects.citrus.exceptions.ResourceNotFoundException;
import com.projects.citrus.exceptions.ValidationException;
import com.projects.citrus.mapper.TreeMapper;
import com.projects.citrus.repositories.FieldRepository;
import com.projects.citrus.repositories.TreeRepository;
import com.projects.citrus.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TreeServiceImplTest {

    @Mock
    private TreeRepository treeRepository;

    @Mock
    private FieldRepository fieldRepository;

    @Mock
    private TreeMapper treeMapper;

    @InjectMocks
    private TreeServiceImpl treeService;

    private Tree tree;
    private TreeRequest treeRequest;
    private TreeResponse treeResponse;
    private Field field;

    @BeforeEach
    void setUp() {
        field = Field.builder().id(1L).area(1000.0).build();
        tree = Tree.builder().id(1L).plantingDate(LocalDate.now()).field(field).build();
        treeRequest = TreeRequest.builder().plantingDate(LocalDate.now()).fieldId(1L).build();
        treeResponse = TreeResponse.builder().id(1L).plantingDate(LocalDate.now()).fieldId(1L).build();
    }


    @Test
    void create_ShouldThrowException_WhenFieldNotFound() {
        when(fieldRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> treeService.create(treeRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Field not found with id: 1");

        verify(treeRepository, never()).save(any(Tree.class));
    }


    @Test
    void update_ShouldThrowException_WhenTreeNotFound() {
        when(treeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> treeService.update(1L, treeRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Tree not found with id: 1");

        verify(treeRepository, never()).save(any(Tree.class));
    }

    @Test
    void getById_ShouldReturnTreeResponse_WhenTreeExists() {
        when(treeRepository.findById(1L)).thenReturn(Optional.of(tree));
        when(treeMapper.toResponse(tree)).thenReturn(treeResponse);

        TreeResponse result = treeService.getById(1L);

        assertThat(result).isEqualTo(treeResponse);
    }

    @Test
    void getById_ShouldThrowException_WhenTreeNotFound() {
        when(treeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> treeService.getById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Tree not found with id: 1");
    }

    @Test
    void getAll_ShouldReturnListOfTreeResponses() {
        Tree tree2 = Tree.builder().id(2L).plantingDate(LocalDate.now()).field(field).build();
        TreeResponse treeResponse2 = TreeResponse.builder().id(2L).plantingDate(LocalDate.now()).fieldId(1L).build();
        List<Tree> trees = List.of(tree, tree2);
        List<TreeResponse> treeResponses = List.of(treeResponse, treeResponse2);

        when(treeRepository.findAll()).thenReturn(trees);
        when(treeMapper.toResponse(tree)).thenReturn(treeResponse);
        when(treeMapper.toResponse(tree2)).thenReturn(treeResponse2);

        List<TreeResponse> result = treeService.getAll();

        assertThat(result).isEqualTo(treeResponses);
    }

    @Test
    void getByFieldId_ShouldReturnListOfTreeResponses_WhenFieldExists() {
        Tree tree2 = Tree.builder().id(2L).plantingDate(LocalDate.now()).field(field).build();
        TreeResponse treeResponse2 = TreeResponse.builder().id(2L).plantingDate(LocalDate.now()).fieldId(1L).build();
        List<Tree> trees = List.of(tree, tree2);
        List<TreeResponse> treeResponses = List.of(treeResponse, treeResponse2);

        when(fieldRepository.existsById(1L)).thenReturn(true);
        when(treeRepository.findByFieldId(1L)).thenReturn(trees);
        when(treeMapper.toResponse(tree)).thenReturn(treeResponse);
        when(treeMapper.toResponse(tree2)).thenReturn(treeResponse2);

        List<TreeResponse> result = treeService.getByFieldId(1L);

        assertThat(result).isEqualTo(treeResponses);
    }

    @Test
    void getByFieldId_ShouldThrowException_WhenFieldNotFound() {
        when(fieldRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> treeService.getByFieldId(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Field not found with id: 1");
    }

    @Test
    void getTreeCountInField_ShouldReturnTreeCount_WhenFieldExists() {
        when(fieldRepository.existsById(1L)).thenReturn(true);
        when(treeRepository.countByFieldId(1L)).thenReturn(10L);

        long result = treeService.getTreeCountInField(1L);

        assertThat(result).isEqualTo(10L);
    }

    @Test
    void getTreeCountInField_ShouldThrowException_WhenFieldNotFound() {
        when(fieldRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> treeService.getTreeCountInField(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Field not found with id: 1");
    }

    @Test
    void delete_ShouldDeleteTree_WhenTreeExists() {
        when(treeRepository.existsById(1L)).thenReturn(true);

        treeService.delete(1L);

        verify(treeRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_ShouldThrowException_WhenTreeNotFound() {
        when(treeRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> treeService.delete(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Tree not found with id: 1");

        verify(treeRepository, never()).deleteById(anyLong());
    }
}