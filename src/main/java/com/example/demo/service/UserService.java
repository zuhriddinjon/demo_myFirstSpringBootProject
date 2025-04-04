package com.example.demo.service;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.UserDto;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public ApiResponse<List<UserDto>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = users.stream()
                .map(user -> new UserDto(user.getId(), user.getFirstName(), user.getEmail()))
                .collect(Collectors.toList());

        return ApiResponse.success(200, "Barcha foydalanuvchilar", userDtos);
    }

    public ApiResponse<UserDto> getUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional
                .map(user -> ApiResponse.success(200, "Foydalanuvchi topildi", new UserDto(user.getId(), user.getFirstName(), user.getEmail())))
                .orElseGet(() -> ApiResponse.error(404, "Foydalanuvchi topildi"));
    }

    public ApiResponse<UserDto> createUser(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getName());
        userRepository.save(user);
        return ApiResponse.success(200, "Foydalanuvchi yaratildi", new UserDto(user.getId(), user.getFirstName(), user.getEmail()));
    }

    public ApiResponse<UserDto> updateUser(Long id, UserDto userDTO) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setFirstName(userDTO.getName());
            user.setEmail(userDTO.getEmail());
            userRepository.save(user);
            return ApiResponse.success(200, "Foydalanuvchi yangilandi", new UserDto(user.getId(), user.getFirstName(), user.getEmail()));
        }
        return ApiResponse.error(404, "Foydalanuvchi topilmadi");
    }

    public ApiResponse<Void> deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ApiResponse.success(200, "Foydalanuvchi o‘chirildi", null);
        }
        return ApiResponse.error(404, "Foydalanuvchi topilmadi");
    }

}
