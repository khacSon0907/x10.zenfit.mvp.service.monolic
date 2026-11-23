package x10.zenfit.mvp.service.infrastructure.workout.adapter.mapper;

import x10.zenfit.mvp.service.common.domain.entities.workout.WorkoutDayEntity;
import x10.zenfit.mvp.service.common.domain.entities.workout.WorkoutExerciseEntity;
import x10.zenfit.mvp.service.common.domain.entities.workout.WorkoutPlanEntity;
import x10.zenfit.mvp.service.infrastructure.workout.db.mongodb.WorkoutDayDoc;
import x10.zenfit.mvp.service.infrastructure.workout.db.mongodb.WorkoutExerciseDoc;
import x10.zenfit.mvp.service.infrastructure.workout.db.mongodb.WorkoutPlanDocument;

import java.util.ArrayList;
import java.util.List;

public class WorkoutPlanMapper {

    public static WorkoutPlanDocument toDocument(WorkoutPlanEntity entity) {
        if (entity == null) {
            return null;
        }

        return WorkoutPlanDocument.builder()
                .id(entity.getId())
                .trainerId(entity.getTrainerId())
                .clientId(entity.getClientId())
                .name(entity.getName())
                .description(entity.getDescription())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .status(entity.getStatus())
                .trainingDays(toDayDocList(entity.getTrainingDays()))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static WorkoutPlanEntity toEntity(WorkoutPlanDocument doc) {
        if (doc == null) {
            return null;
        }

        return WorkoutPlanEntity.builder()
                .id(doc.getId())
                .trainerId(doc.getTrainerId())
                .clientId(doc.getClientId())
                .name(doc.getName())
                .description(doc.getDescription())
                .startDate(doc.getStartDate())
                .endDate(doc.getEndDate())
                .status(doc.getStatus())
                .trainingDays(toDayEntityList(doc.getTrainingDays()))
                .createdAt(doc.getCreatedAt())
                .updatedAt(doc.getUpdatedAt())
                .build();
    }

    public static WorkoutDayDoc toDayDoc(WorkoutDayEntity entity) {
        if (entity == null) {
            return null;
        }

        return WorkoutDayDoc.builder()
                .id(entity.getId())
                .dayNumber(entity.getDayNumber())
                .dayName(entity.getDayName())
                .description(entity.getDescription())
                .exercises(toExerciseDocList(entity.getExercises()))
                .build();
    }

    public static WorkoutDayEntity toDayEntity(WorkoutDayDoc doc) {
        if (doc == null) {
            return null;
        }

        return WorkoutDayEntity.builder()
                .id(doc.getId())
                .dayNumber(doc.getDayNumber())
                .dayName(doc.getDayName())
                .description(doc.getDescription())
                .exercises(toExerciseEntityList(doc.getExercises()))
                .build();
    }

    public static WorkoutExerciseDoc toExerciseDoc(WorkoutExerciseEntity entity) {
        if (entity == null) {
            return null;
        }

        return WorkoutExerciseDoc.builder()
                .id(entity.getId())
                .exerciseName(entity.getExerciseName())
                .sets(entity.getSets())
                .reps(entity.getReps())
                .restSeconds(entity.getRestSeconds())
                .tempo(entity.getTempo())
                .note(entity.getNote())
                .build();
    }

    public static WorkoutExerciseEntity toExerciseEntity(WorkoutExerciseDoc doc) {
        if (doc == null) {
            return null;
        }

        return WorkoutExerciseEntity.builder()
                .id(doc.getId())
                .exerciseName(doc.getExerciseName())
                .sets(doc.getSets())
                .reps(doc.getReps())
                .restSeconds(doc.getRestSeconds())
                .tempo(doc.getTempo())
                .note(doc.getNote())
                .build();
    }

    public static List<WorkoutDayDoc> toDayDocList(List<WorkoutDayEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return new ArrayList<>();
        }
        return entities.stream().map(WorkoutPlanMapper::toDayDoc).toList();
    }

    public static List<WorkoutDayEntity> toDayEntityList(List<WorkoutDayDoc> docs) {
        if (docs == null || docs.isEmpty()) {
            return new ArrayList<>();
        }
        return docs.stream().map(WorkoutPlanMapper::toDayEntity).toList();
    }

    public static List<WorkoutExerciseDoc> toExerciseDocList(List<WorkoutExerciseEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return new ArrayList<>();
        }
        return entities.stream().map(WorkoutPlanMapper::toExerciseDoc).toList();
    }

    public static List<WorkoutExerciseEntity> toExerciseEntityList(List<WorkoutExerciseDoc> docs) {
        if (docs == null || docs.isEmpty()) {
            return new ArrayList<>();
        }
        return docs.stream().map(WorkoutPlanMapper::toExerciseEntity).toList();
    }
}

