package com.projects.citrus.repositories;

import com.projects.citrus.domain.entities.Tree;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TreeRepository extends JpaRepository<Tree, Long>
{
    // Trouve tous les arbres d'un champ donné
    List<Tree> findByFieldId(Long fieldId);

    // Compte le nombre d'arbres dans un champ
    long countByFieldId(Long fieldId);

    // Vérifie si un arbre existe dans un champ donné
    boolean existsByFieldId(Long fieldId);
}
