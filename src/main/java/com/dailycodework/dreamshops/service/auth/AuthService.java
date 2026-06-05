package com.dailycodework.dreamshops.service.auth;

import com.dailycodework.dreamshops.constant.ResultNotify;
import com.dailycodework.dreamshops.entity.AppUser;
import com.dailycodework.dreamshops.payload.dto.BaseResultDTO;
import com.dailycodework.dreamshops.payload.dto.auth.AuthResponse;
import com.dailycodework.dreamshops.payload.dto.auth.LoginRequest;
import com.dailycodework.dreamshops.payload.dto.auth.RegisterRequest;
import com.dailycodework.dreamshops.repository.user.IAppUserRepository;
import com.dailycodework.dreamshops.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final IAppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public BaseResultDTO register(RegisterRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            return new BaseResultDTO("Tên đăng nhập đã tồn tại", "DUPLICATE_USERNAME", false);
        }

        AppUser user = AppUser.builder()
                .username(req.getUsername())
                .password(passwordEncoder.encode(req.getPassword()))
                .email(req.getEmail())
                .fullName(req.getFullName())
                .companyId(req.getCompanyId())
                .role(req.getRole() != null ? req.getRole().toUpperCase() : "USER")
                .active(true)
                .createdAt(ZonedDateTime.now())
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user);
        return new BaseResultDTO(
                ResultNotify.successCreate,
                true,
                new AuthResponse(token, user.getUsername(), user.getFullName(), user.getRole(), user.getCompanyId())
        );
    }

    public BaseResultDTO login(LoginRequest req) {
//        Đây là bước:
//        Spring Security kiểm tra tài khoản/mật khẩu có đúng không.
//                Nó sẽ làm gì bên trong?
//                Spring Security sẽ:
//        tìm user trong DB
//        lấy password đã mã hoá
//        compare password
//        kiểm tra account lock/disable
//        nếu đúng → authenticate thành công
//        nếu sai → throw exception
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
            );
        } catch (BadCredentialsException e) {
            return new BaseResultDTO("Tên đăng nhập hoặc mật khẩu không đúng", "BAD_CREDENTIALS", false);
        }

        AppUser user = userRepository.findByUsername(req.getUsername()).orElseThrow();
        String token = jwtService.generateToken(user);
        return new BaseResultDTO(
                ResultNotify.successGet,
                true,
                new AuthResponse(token, user.getUsername(), user.getFullName(), user.getRole(), user.getCompanyId())
        );
    }
}
