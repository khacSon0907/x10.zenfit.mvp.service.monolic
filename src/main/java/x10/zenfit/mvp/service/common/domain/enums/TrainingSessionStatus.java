package x10.zenfit.mvp.service.common.domain.enums;

public enum TrainingSessionStatus {
    SCHEDULED,   // Buổi tập đã lên lịch, chưa diễn ra
    COMPLETED,   // Buổi tập đã hoàn thành tập luyện
    CANCELLED,   // Buổi tập đã bị hủy bỏ
    NO_SHOW      // Học viên vắng mặt không lý do
}
