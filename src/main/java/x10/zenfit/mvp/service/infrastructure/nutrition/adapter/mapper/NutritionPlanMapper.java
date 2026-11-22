package x10.zenfit.mvp.service.infrastructure.nutrition.adapter.mapper;

import x10.zenfit.mvp.service.common.domain.entities.nutrition.DailyMacroTarget;
import x10.zenfit.mvp.service.common.domain.entities.nutrition.NutritionFoodEntity;
import x10.zenfit.mvp.service.common.domain.entities.nutrition.NutritionPlanEntity;
import x10.zenfit.mvp.service.infrastructure.nutrition.db.mongodb.DailyMacroTargetDoc;
import x10.zenfit.mvp.service.infrastructure.nutrition.db.mongodb.NutritionFoodDoc;
import x10.zenfit.mvp.service.infrastructure.nutrition.db.mongodb.NutritionPlanDocument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NutritionPlanMapper {

    public static NutritionPlanDocument toDocument(NutritionPlanEntity entity) {
        if (entity == null) {
            return null;
        }

        return NutritionPlanDocument.builder()
                .id(entity.getId())
                .trainerId(entity.getTrainerId())
                .clientId(entity.getClientId())
                .name(entity.getName())
                .description(entity.getDescription())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .status(entity.getStatus())
                .dailyMacroTarget(toMacroTargetDoc(entity.getDailyMacroTarget()))
                .foods(toFoodDocList(entity.getFoods()))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static NutritionPlanEntity toEntity(NutritionPlanDocument doc) {
        if (doc == null) {
            return null;
        }

        return NutritionPlanEntity.builder()
                .id(doc.getId())
                .trainerId(doc.getTrainerId())
                .clientId(doc.getClientId())
                .name(doc.getName())
                .description(doc.getDescription())
                .startDate(doc.getStartDate())
                .endDate(doc.getEndDate())
                .status(doc.getStatus())
                .dailyMacroTarget(toMacroTargetEntity(doc.getDailyMacroTarget()))
                .foods(toFoodEntityList(doc.getFoods()))
                .createdAt(doc.getCreatedAt())
                .updatedAt(doc.getUpdatedAt())
                .build();
    }

    public static DailyMacroTargetDoc toMacroTargetDoc(DailyMacroTarget target) {
        if (target == null) {
            return null;
        }
        return DailyMacroTargetDoc.builder()
                .calories(target.getCalories())
                .protein(target.getProtein())
                .carbs(target.getCarbs())
                .fat(target.getFat())
                .build();
    }

    public static DailyMacroTarget toMacroTargetEntity(DailyMacroTargetDoc doc) {
        if (doc == null) {
            return null;
        }
        return DailyMacroTarget.builder()
                .calories(doc.getCalories())
                .protein(doc.getProtein())
                .carbs(doc.getCarbs())
                .fat(doc.getFat())
                .build();
    }

    public static NutritionFoodDoc toFoodDoc(NutritionFoodEntity entity) {
        if (entity == null) {
            return null;
        }
        return NutritionFoodDoc.builder()
                .id(entity.getId())
                .mealType(entity.getMealType())
                .foodName(entity.getFoodName())
                .quantity(entity.getQuantity())
                .unit(entity.getUnit())
                .calories(entity.getCalories())
                .protein(entity.getProtein())
                .carbs(entity.getCarbs())
                .fat(entity.getFat())
                .note(entity.getNote())
                .build();
    }

    public static NutritionFoodEntity toFoodEntity(NutritionFoodDoc doc) {
        if (doc == null) {
            return null;
        }
        return NutritionFoodEntity.builder()
                .id(doc.getId())
                .mealType(doc.getMealType())
                .foodName(doc.getFoodName())
                .quantity(doc.getQuantity())
                .unit(doc.getUnit())
                .calories(doc.getCalories())
                .protein(doc.getProtein())
                .carbs(doc.getCarbs())
                .fat(doc.getFat())
                .note(doc.getNote())
                .build();
    }

    public static List<NutritionFoodDoc> toFoodDocList(List<NutritionFoodEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return new ArrayList<>();
        }
        return entities.stream().map(NutritionPlanMapper::toFoodDoc).toList();
    }

    public static List<NutritionFoodEntity> toFoodEntityList(List<NutritionFoodDoc> docs) {
        if (docs == null || docs.isEmpty()) {
            return new ArrayList<>();
        }
        return docs.stream().map(NutritionPlanMapper::toFoodEntity).toList();
    }
}

