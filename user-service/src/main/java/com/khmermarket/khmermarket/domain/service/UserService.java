package com.khmermarket.khmermarket.domain.service;

import com.khmermarket.khmermarket.domain.dto.UserDto;
import com.khmermarket.khmermarket.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface UserService {
    Page<UserDto> findAll(Pageable pageable);
    UserDto findById(UUID id);
    void delete(UUID id);
    UserDto fromEntityToDto(User user);
}
