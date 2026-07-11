package com.dailycodework.dreamshops.service.user;

import com.dailycodework.dreamshops.payload.dto.BaseResultDTO;
import com.dailycodework.dreamshops.payload.dto.user.PasswordChangeReq;
import com.dailycodework.dreamshops.payload.dto.user.UserInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IUserService {
    BaseResultDTO getUsersByCompany(Long companyId);
    BaseResultDTO findById(Long id);
    BaseResultDTO createUser(UserInfo userInfo);
    BaseResultDTO updateUser(UserInfo userInfo);
    BaseResultDTO uploadAvatar(Long id, MultipartFile file) throws IOException;
    BaseResultDTO deleteUser(Long id);
    BaseResultDTO changePassword(PasswordChangeReq req);
    BaseResultDTO toggleActive(Long id);
}
