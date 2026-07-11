package com.dailycodework.dreamshops.service.user;

import com.dailycodework.dreamshops.constant.ResultNotify;
import com.dailycodework.dreamshops.entity.AppUser;
import com.dailycodework.dreamshops.payload.dto.BaseResultDTO;
import com.dailycodework.dreamshops.payload.dto.user.PasswordChangeReq;
import com.dailycodework.dreamshops.payload.dto.user.UserInfo;
import com.dailycodework.dreamshops.repository.user.IAppUserRepository;
import com.dailycodework.dreamshops.service.storage.FileStorageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements IUserService {
    private final IAppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;

    @Override
    public BaseResultDTO getUsersByCompany(Long companyId) {
        List<AppUser> users = userRepository.findByCompanyId(companyId);
        users.forEach(u -> u.setPassword(null));
        return new BaseResultDTO(ResultNotify.successGet, true, users, users.size());
    }

    @Override
    public BaseResultDTO findById(Long id) {
        Optional<AppUser> user = userRepository.findById(id);
        if (user.isEmpty()) throw new RuntimeException(ResultNotify.notFound);
        AppUser u = user.get();
        u.setPassword(null);
        return new BaseResultDTO(ResultNotify.successGet, true, u);
    }

    @Override
    public BaseResultDTO createUser(UserInfo userInfo) {
        if (userRepository.existsByUsername(userInfo.getUsername())) {
            return new BaseResultDTO("Tên đăng nhập đã tồn tại", false, null);
        }
        AppUser user = AppUser.builder()
                .username(userInfo.getUsername())
                .password(passwordEncoder.encode(userInfo.getPassword()))
                .email(userInfo.getEmail())
                .fullName(userInfo.getFullName())
                .companyId(userInfo.getCompanyId())
                .role(userInfo.getRole() != null ? userInfo.getRole().toUpperCase() : "USER")
                .active(true)
                .createdAt(ZonedDateTime.now())
                .avatar(userInfo.getAvatar())
                .build();
        userRepository.save(user);
        user.setPassword(null);
        return new BaseResultDTO(ResultNotify.successCreate, true, user);
    }

    @Override
    public BaseResultDTO updateUser(UserInfo userInfo) {
        Optional<AppUser> existing = userRepository.findById(userInfo.getId());
        if (existing.isEmpty()) throw new RuntimeException(ResultNotify.notFound);
        AppUser user = existing.get();
        if (userInfo.getEmail() != null) user.setEmail(userInfo.getEmail());
        if (userInfo.getFullName() != null) user.setFullName(userInfo.getFullName());
        if (userInfo.getRole() != null) user.setRole(userInfo.getRole().toUpperCase());
        if (userInfo.getActive() != null) user.setActive(userInfo.getActive());
        if (userInfo.getAvatar() != null) user.setAvatar(userInfo.getAvatar());
        userRepository.save(user);
        user.setPassword(null);
        return new BaseResultDTO(ResultNotify.successUpdate, true, user);
    }

    @Override
    public BaseResultDTO uploadAvatar(Long id, MultipartFile file) throws IOException {
        Optional<AppUser> existing = userRepository.findById(id);
        if (existing.isEmpty()) throw new RuntimeException(ResultNotify.notFound);
        AppUser user = existing.get();
        String oldAvatar = user.getAvatar();
        String url = fileStorageService.store(file, "avatars");
        user.setAvatar(url);
        userRepository.save(user);
        fileStorageService.delete(oldAvatar);
        user.setPassword(null);
        return new BaseResultDTO(ResultNotify.successUpdate, true, user);
    }

    @Override
    public BaseResultDTO deleteUser(Long id) {
        if (!userRepository.existsById(id)) throw new RuntimeException(ResultNotify.notFound);
        userRepository.deleteById(id);
        return new BaseResultDTO(ResultNotify.successDelete, true, null);
    }

    @Override
    public BaseResultDTO changePassword(PasswordChangeReq req) {
        Optional<AppUser> existing = userRepository.findById(req.getUserId());
        if (existing.isEmpty()) throw new RuntimeException(ResultNotify.notFound);
        AppUser user = existing.get();
        if (!passwordEncoder.matches(req.getOldPassword(), user.getPassword())) {
            return new BaseResultDTO("Mật khẩu cũ không đúng", false, null);
        }
        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        userRepository.save(user);
        return new BaseResultDTO(ResultNotify.successUpdate, true, null);
    }

    @Override
    public BaseResultDTO toggleActive(Long id) {
        Optional<AppUser> existing = userRepository.findById(id);
        if (existing.isEmpty()) throw new RuntimeException(ResultNotify.notFound);
        AppUser user = existing.get();
        user.setActive(!Boolean.TRUE.equals(user.getActive()));
        userRepository.save(user);
        return new BaseResultDTO(ResultNotify.successUpdate, true, null);
    }
}
