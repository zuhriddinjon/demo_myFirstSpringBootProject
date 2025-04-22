package com.example.demo.service;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.UpdateUserRequest;
import com.example.demo.dto.UserDto;
import com.example.demo.dto.CreateUserRequest;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.UserEntity;
import com.example.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ApiResponse<List<UserDto>> getAllUsers() {
        try {
            List<UserEntity> users = userRepository.findAll();
            List<UserDto> userDtos = users.stream()
                    .map(user -> new UserDto(user.getId(), user.getFirstName(), user.getEmail(), user.getDataOfBirth()))
                    .collect(Collectors.toList());

            return ApiResponse.success(200, "Barcha foydalanuvchilar", userDtos);
        } catch (Exception e) {
            logger.error("Xatolik yuz berdi: {}", e.getMessage(), e);
            return ApiResponse.error(500, "Server xatosi");
        }
    }

    public ApiResponse<UserDto> getUserById(Long id) {
        try {
            UserEntity user = userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("Foydalanuvchi topilmadi: " + id));

            return ApiResponse.success(200, "Foydalanuvchi topildi", new UserDto(user.getId(), user.getFirstName(), user.getEmail(), user.getDataOfBirth()));
        } catch (Exception e) {
            logger.error("Xatolik yuz berdi: {}", e.getMessage(), e);
            return ApiResponse.error(500, "Server xatosi");
        }
    }

    public ApiResponse<UserDto> createUser(CreateUserRequest userDto) {
        try {
            UserEntity user = new UserEntity();
            user.setEmail(userDto.email());
            user.setFirstName(userDto.name());
            user.setDataOfBirth(userDto.birthDate());
            userRepository.save(user);

            return ApiResponse.success(200, "Foydalanuvchi yaratildi", new UserDto(user.getId(), user.getFirstName(), user.getEmail(), user.getDataOfBirth()));
        } catch (Exception e) {
            logger.error("Xatolik yuz berdi: {}", e.getMessage(), e);
            return ApiResponse.error(500, "Foydalanuvchi yaratishda xatolik");
        }
    }

    public ApiResponse<UserDto> updateUser(Long id, UpdateUserRequest userRequest) {
        try {
            Optional<UserEntity> userOptional = userRepository.findById(id);
            userOptional.orElseThrow(() -> new UserNotFoundException("Foydalanuvchi topilmadi: " + id));
            if (userOptional.isPresent()) {
                UserEntity user = userOptional.get();
                user.setFirstName(userRequest.name());
                user.setEmail(userRequest.email());
                user.setDataOfBirth(userRequest.birthDate());
                userRepository.save(user);
                return ApiResponse.success(200, "Foydalanuvchi yangilandi", new UserDto(user.getId(), user.getFirstName(), user.getEmail(), user.getDataOfBirth()));
            }
            return ApiResponse.error(404, "Foydalanuvchi topilmadi");
        } catch (Exception e) {
            logger.error("Xatolik yuz berdi: {}", e.getMessage(), e);
            return ApiResponse.error(500, "Foydalanuvchi update xatolik");
        }
    }

    public ApiResponse<Void> deleteUser(Long id) {
        try {
            if (userRepository.existsById(id)) {
                userRepository.deleteById(id);
                return ApiResponse.success(200, "Foydalanuvchi o‘chirildi", null);
            } else {
                throw new UserNotFoundException("Foydalanuvchi topilmadi: " + id);
            }
        } catch (Exception e) {
            logger.error("Xatolik yuz berdi: {}", e.getMessage(), e);
            return ApiResponse.error(500, "Server xatosi");
        }
    }

}
