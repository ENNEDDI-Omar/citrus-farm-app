package com.projects.citrus.services.interfaces;

import com.projects.citrus.dto.requests.TreeRequest;
import com.projects.citrus.dto.responses.TreeResponse;

import java.util.List;

public interface ITreeService
{
    TreeResponse create(TreeRequest request);
    TreeResponse update(Long id, TreeRequest request);
    TreeResponse getById(Long id);
    List<TreeResponse> getAll();
    List<TreeResponse> getByFieldId(Long fieldId);
    long getTreeCountInField(Long fieldId);
    void delete(Long id);
}
