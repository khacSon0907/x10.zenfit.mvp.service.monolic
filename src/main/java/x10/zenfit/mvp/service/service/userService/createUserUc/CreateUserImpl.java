package x10.zenfit.mvp.service.service.userService.createUserUc;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.Repository.IUserRepository;
import x10.zenfit.mvp.service.common.domain.entities.user.UserEntity;
import x10.zenfit.mvp.service.common.domain.enums.AuthProvider;
import x10.zenfit.mvp.service.common.domain.enums.UserStatus;
import x10.zenfit.mvp.service.config.redis.IRedisService;
import x10.zenfit.mvp.service.service.notification.OtpGenerator;
import x10.zenfit.mvp.service.service.notification.event.RegisterOtpEvent;
import x10.zenfit.mvp.service.service.roleService.RoleResolver;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.user.UserError;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CreateUserImpl implements ICreateUserUc {

        private static final Duration REGISTER_OTP_EXPIRE =
                Duration.ofMinutes(5);

        private final IUserRepository userRepository;

        private final PasswordEncoder passwordEncoder;

        private final IRedisService redisService;

        private final OtpGenerator otpGenerator;

        private final ApplicationEventPublisher eventPublisher;

        private final RoleResolver roleResolver;

        @Override
        public UserEntity createUser(CreateUserReq req) {

                String email = req.getEmail()
                        .trim()
                        .toLowerCase();

                String phone = normalizePhone(req.getPhone());

                Optional<UserEntity> existedUser =
                        userRepository.findByEmail(email);

                if (existedUser.isPresent()) {

                        UserEntity user = existedUser.get();

                        /*
                         * CASE 1:
                         * Guest account upgrade -> LOCAL
                         */
                        if (user.getProviderType() == AuthProvider.GUEST) {

                                user.setUsername(req.getUsername());

                                user.setEmail(email);

                                user.setPhoneNumber(phone);

                                user.setPasswordHash(
                                        passwordEncoder.encode(req.getPassword()));

                                user.setProviderType(AuthProvider.LOCAL);

                                user.setStatus(UserStatus.PENDING);

                                user.setEmailVerified(false);

                                if (user.getRoleIds() == null
                                        || user.getRoleIds().isEmpty()) {

                                        user.setRoleIds(resolveRoleIds(req.getRoleIds()));
                                }

                                user.setUpdatedAt(Instant.now());

                                UserEntity savedUser =
                                        userRepository.save(user);

                                createOtpAndPublishEvent(savedUser);

                                return savedUser;
                        }

                        /*
                         * CASE 2:
                         * LOCAL account chưa verify email
                         * Cho phép register lại + gửi OTP mới
                         */
                        if (user.getProviderType() == AuthProvider.LOCAL
                                && user.getStatus() == UserStatus.PENDING
                                && !user.isEmailVerified()) {

                                user.setUsername(req.getUsername());

                                user.setPhoneNumber(phone);

                                user.setPasswordHash(
                                        passwordEncoder.encode(req.getPassword()));

                                user.setUpdatedAt(Instant.now());

                                UserEntity savedUser =
                                        userRepository.save(user);

                                createOtpAndPublishEvent(savedUser);

                                return savedUser;
                        }

                        /*
                         * CASE 3:
                         * GOOGLE account muốn tạo password
                         *
                         * Không cần OTP:
                         * - Google đã verify email
                         * - account đã ACTIVE
                         */
                        if (user.getProviderType() == AuthProvider.GOOGLE) {

                                user.setPasswordHash(
                                        passwordEncoder.encode(req.getPassword()));

                                /*
                                 * Optional:
                                 * update username nếu chưa có
                                 */
                                if (user.getUsername() == null
                                        || user.getUsername().isBlank()) {

                                        user.setUsername(req.getUsername());
                                }

                                /*
                                 * Optional:
                                 * update phone nếu chưa có
                                 */
                                if (user.getPhoneNumber() == null
                                        || user.getPhoneNumber().isBlank()) {

                                        user.setPhoneNumber(phone);
                                }

                                if (user.getRoleIds() == null
                                        || user.getRoleIds().isEmpty()) {

                                        user.setRoleIds(resolveRoleIds(req.getRoleIds()));
                                }

                                user.setStatus(UserStatus.ACTIVE);

                                user.setEmailVerified(true);

                                user.setUpdatedAt(Instant.now());

                                return userRepository.save(user);
                        }

                        /*
                         * CASE 4:
                         * Email đã tồn tại hoàn toàn
                         */
                        throw new BusinessException(
                                UserError.EMAIL_EXISTS);
                }

                /*
                 * CASE 5:
                 * Tạo LOCAL account mới
                 */
                UserEntity newUser = UserEntity.builder()
                        .username(req.getUsername())
                        .email(email)
                        .phoneNumber(phone)
                        .passwordHash(
                                passwordEncoder.encode(req.getPassword()))
                        .providerType(AuthProvider.LOCAL)
                        .status(UserStatus.PENDING)
                        .emailVerified(false)
                        .birthdayUpdateCount(0)
                        .points(0L)
                        .roleIds(resolveRoleIds(req.getRoleIds()))
                        .createdAt(Instant.now())
                        .updatedAt(Instant.now())
                        .build();

                UserEntity savedUser =
                        userRepository.save(newUser);

                createOtpAndPublishEvent(savedUser);

                return savedUser;
        }

        private List<String> resolveRoleIds(List<String> roleIds) {
                if (roleIds == null || roleIds.isEmpty()) {
                        return roleResolver.getDefaultRoleIds();
                }

                List<String> normalizedRoleIds = roleIds.stream()
                        .map(String::trim)
                        .filter(roleId -> !roleId.isEmpty())
                        .distinct()
                        .toList();

                if (normalizedRoleIds.isEmpty()) {
                        return roleResolver.getDefaultRoleIds();
                }

                return normalizedRoleIds;
        }

        /**
         * Chuẩn hóa SĐT về dạng 0xxxxxxxxx:
         * - bỏ khoảng trắng
         * - đổi +84 thành 0
         * Trả về null nếu không nhập (trường hợp SĐT không bắt buộc).
         */
        private String normalizePhone(String rawPhone) {

                if (rawPhone == null || rawPhone.isBlank()) {
                        return null;
                }

                String phone = rawPhone.replaceAll("\\s+", "");

                if (phone.startsWith("+84")) {
                        phone = "0" + phone.substring(3);
                }

                return phone;
        }

        private void createOtpAndPublishEvent(
                UserEntity user) {

                String otp = otpGenerator.generate();

                redisService.saveRegisterOtp(
                        user.getEmail(),
                        otp,
                        REGISTER_OTP_EXPIRE
                );

                eventPublisher.publishEvent(
                        new RegisterOtpEvent(
                                user.getEmail(),
                                user.getUsername(),
                                otp
                        )
                );
        }
}