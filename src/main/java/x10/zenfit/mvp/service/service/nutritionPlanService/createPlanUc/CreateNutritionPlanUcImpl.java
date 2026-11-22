package x10.zenfit.mvp.service.service.nutritionPlanService.createPlanUc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import x10.zenfit.mvp.service.Repository.IClientRepository;
import x10.zenfit.mvp.service.Repository.INutritionPlanRepository;
import x10.zenfit.mvp.service.common.domain.entities.client.ClientEntity;
import x10.zenfit.mvp.service.common.domain.entities.nutrition.DailyMacroTarget;
import x10.zenfit.mvp.service.common.domain.entities.nutrition.NutritionFoodEntity;
import x10.zenfit.mvp.service.common.domain.entities.nutrition.NutritionPlanEntity;
import x10.zenfit.mvp.service.common.domain.enums.NutritionPlanStatus;
import x10.zenfit.mvp.service.service.nutritionPlanService.dto.CreateNutritionPlanRequest;
import x10.zenfit.mvp.service.service.nutritionPlanService.dto.NutritionPlanResponse;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.nutrition.NutritionPlanError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateNutritionPlanUcImpl implements ICreateNutritionPlanUc {

    private final INutritionPlanRepository nutritionPlanRepository;
    private final IClientRepository clientRepository;

    @Override
    @Transactional
    public NutritionPlanResponse createPlan(String trainerId, CreateNutritionPlanRequest request) {
        if (trainerId == null || trainerId.isBlank()) {
            throw new BusinessException(NutritionPlanError.ACCESS_DENIED);
        }

        if (request == null) {
            throw new BusinessException(NutritionPlanError.INVALID_DATA);
        }

        // 1. Kiểm tra ngày hợp lệ
        if (request.getStartDate() != null && request.getEndDate() != null) {
            if (request.getStartDate().isAfter(request.getEndDate())) {
                throw new BusinessException(NutritionPlanError.INVALID_DATE_RANGE);
            }
        }

        // 2. Kiểm tra Client tồn tại
        ClientEntity client = clientRepository.findById(request.getClientId().trim())
                .orElseThrow(() -> new BusinessException(NutritionPlanError.CLIENT_NOT_FOUND));

        // 3. Kiểm tra Client có thuộc quyền PT đang đăng nhập không
        if (client.getAssignedTrainerId() == null || !client.getAssignedTrainerId().equals(trainerId.trim())) {
            log.warn("Trainer {} tried to create nutrition plan for client {} assigned to {}",
                    trainerId, client.getId(), client.getAssignedTrainerId());
            throw new BusinessException(NutritionPlanError.CLIENT_NOT_ASSIGNED_TO_TRAINER);
        }

        // 4. Tạo target macro
        DailyMacroTarget target = null;
        if (request.getDailyMacroTarget() != null) {
            target = DailyMacroTarget.builder()
                    .calories(request.getDailyMacroTarget().getCalories())
                    .protein(request.getDailyMacroTarget().getProtein())
                    .carbs(request.getDailyMacroTarget().getCarbs())
                    .fat(request.getDailyMacroTarget().getFat())
                    .build();
        }

        // 5. Tạo danh sách món ăn ban đầu (nếu có)
        List<NutritionFoodEntity> foods = new ArrayList<>();
        if (request.getFoods() != null && !request.getFoods().isEmpty()) {
            foods = request.getFoods().stream()
                    .map(f -> NutritionFoodEntity.builder()
                            .id(UUID.randomUUID().toString())
                            .mealType(f.getMealType())
                            .foodName(f.getFoodName().trim())
                            .quantity(f.getQuantity())
                            .unit(f.getUnit())
                            .calories(f.getCalories() != null ? f.getCalories() : 0.0)
                            .protein(f.getProtein() != null ? f.getProtein() : 0.0)
                            .carbs(f.getCarbs() != null ? f.getCarbs() : 0.0)
                            .fat(f.getFat() != null ? f.getFat() : 0.0)
                            .note(f.getNote())
                            .build())
                    .toList();
        }

        LocalDateTime now = LocalDateTime.now();
        NutritionPlanEntity plan = NutritionPlanEntity.builder()
                .trainerId(trainerId.trim())
                .clientId(client.getId())
                .name(request.getName().trim())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(request.getStatus() != null ? request.getStatus() : NutritionPlanStatus.ACTIVE)
                .dailyMacroTarget(target)
                .foods(foods)
                .createdAt(now)
                .updatedAt(now)
                .build();

        NutritionPlanEntity saved = nutritionPlanRepository.save(plan);
        log.info("Created nutrition plan {} for client {} by trainer {}", saved.getId(), client.getId(), trainerId);

        return NutritionPlanResponse.fromEntity(saved, client.getFullName());
    }
}

