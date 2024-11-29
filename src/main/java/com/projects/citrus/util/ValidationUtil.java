package com.projects.citrus.util;

import com.projects.citrus.domain.entities.*;
import com.projects.citrus.domain.enums.Season;
import com.projects.citrus.exceptions.BusinessException;
import com.projects.citrus.exceptions.ValidationException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ValidationUtil {
    // Constants
    private static final double MIN_FIELD_AREA = 0.1;
    private static final double MAX_FIELD_RATIO = 0.5;
    private static final int MAX_FIELDS_PER_FARM = 10;
    private static final int MAX_TREES_PER_HECTARE = 100;
    private static final int PLANTING_START_MONTH = 3;
    private static final int PLANTING_END_MONTH = 5;
    private static final int MAX_TREE_AGE = 20;

    // Farm validations
    public static void validateFarmArea(Double area) {
        if (area <= 0) {
            throw new BusinessException("Farm area must be positive");
        }
    }

    public static void validateFieldForFarm(Farm farm, Field field) {
        // Vérification de la taille minimale du champ
        if (field.getArea() < MIN_FIELD_AREA) {
            throw new ValidationException(
                    String.format("Field area must be at least %.1f hectares", MIN_FIELD_AREA)
            );
        }

        // Vérification du ratio champ/ferme
        if (field.getArea() > (farm.getArea() * MAX_FIELD_RATIO)) {
            throw new ValidationException(
                    "Field area cannot exceed 50% of farm area"
            );
        }

        // Vérification du nombre maximum de champs
        if (farm.getFields().size() >= MAX_FIELDS_PER_FARM) {
            throw new ValidationException(
                    String.format("Farm cannot have more than %d fields", MAX_FIELDS_PER_FARM)
            );
        }

        // Vérification de la superficie totale
        double totalAreaAfterAdd = farm.getFields().stream()
                .mapToDouble(Field::getArea)
                .sum() + field.getArea();

        if (totalAreaAfterAdd >= farm.getArea()) {
            throw new ValidationException("Total field area would exceed farm area");
        }
    }

    // Field validations
    public static void validateTreeAddition(Field field, int newTreeCount) {
        int maxTrees = getMaxTreeCapacityForField(field.getArea());
        int futureTreeCount = field.getTrees().size() + newTreeCount;

        if (futureTreeCount > maxTrees) {
            throw new ValidationException(
                    String.format("Cannot add more trees. Maximum capacity is %d trees", maxTrees)
            );
        }
    }

    // Tree validations
    public static void validatePlantingDate(LocalDate plantingDate) {
        if (plantingDate == null) {
            throw new ValidationException("Planting date cannot be null");
        }

        int month = plantingDate.getMonthValue();
        if (month < PLANTING_START_MONTH || month > PLANTING_END_MONTH) {
            throw new ValidationException(
                    "Trees can only be planted between March and May"
            );
        }
    }

    public static void validateTreeForHarvest(Tree tree, Harvest harvest) {
        // Vérification de l'âge productif
        if (!isTreeProductiveAge(tree)) {
            throw new ValidationException(
                    String.format("Tree is too old (age: %d years). Maximum productive age is %d years",
                            tree.getAge(), MAX_TREE_AGE)
            );
        }

        // Vérification de la double récolte
        if (!canTreeBeHarvested(tree, harvest)) {
            throw new ValidationException(
                    "Tree has already been harvested this season"
            );
        }
    }

    // Harvest validations
    public static void validateHarvestDate(Harvest harvest) {
        if (!Season.fromDate(harvest.getHarvestDate()).equals(harvest.getSeason())) {
            throw new ValidationException(
                    "Harvest date does not match the specified season"
            );
        }
    }

    public static void validateHarvestQuantity(HarvestDetail harvestDetail) {
        if (harvestDetail.getQuantity() > harvestDetail.getTree().getProductivity()) {
            throw new ValidationException(
                    String.format("Harvest quantity exceeds tree productivity (%.2f kg)",
                            harvestDetail.getTree().getProductivity())
            );
        }
    }

    // Sale validations
    public static void validateSale(Sale sale) {
        if (!sale.getSaleDate().isAfter(sale.getHarvest().getHarvestDate())) {
            throw new ValidationException(
                    "Sale date must be after harvest date"
            );
        }

        if (sale.getHarvest().getTotalQuantity() <= 0) {
            throw new ValidationException(
                    "Cannot create sale for harvest with no quantity"
            );
        }
    }

    // Helper methods
    public static boolean isTreeProductiveAge(Tree tree) {
        return tree.getAge() <= MAX_TREE_AGE;
    }

    public static boolean canTreeBeHarvested(Tree tree, Harvest harvest) {
        return tree.getHarvestDetails().stream()
                .map(HarvestDetail::getHarvest)
                .noneMatch(h -> h.getSeason().equals(harvest.getSeason()));
    }

    public static int getMaxTreeCapacityForField(double fieldArea) {
        return (int) ((fieldArea * 10000) / MAX_TREES_PER_HECTARE);
    }
}