package x10.zenfit.mvp.service.service.clientService;

import x10.zenfit.mvp.service.common.domain.entities.client.ClientEntity;

public final class ClientResponseMapper {

    private ClientResponseMapper() {
    }

    public static ClientResponse toResponse(ClientEntity entity) {
        if (entity == null) {
            return null;
        }

        return ClientResponse.builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .gender(entity.getGender())
                .dateOfBirth(entity.getDateOfBirth())
                .phone(entity.getPhone())
                .email(entity.getEmail())
                .address(entity.getAddress())
                .avatarUrl(entity.getAvatarUrl())
                .status(entity.getStatus())
                .height(entity.getHeight())
                .initialWeight(entity.getInitialWeight())
                .fitnessGoal(entity.getFitnessGoal())
                .experienceLevel(entity.getExperienceLevel())
                .targetWeight(entity.getTargetWeight())
                .medicalConditions(entity.getMedicalConditions())
                .injuries(entity.getInjuries())
                .emergencyContactName(entity.getEmergencyContactName())
                .emergencyContactPhone(entity.getEmergencyContactPhone())
                .assignedTrainerId(entity.getAssignedTrainerId())
                .notes(entity.getNotes())
                .joinedDate(entity.getJoinedDate())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
