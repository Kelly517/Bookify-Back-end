package com.bookify.books.Bookify.cache;

import com.bookify.books.Bookify.constants.RedisKeys;
import com.bookify.books.Bookify.model.dto.userdto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class VerificationCodeService {

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisTemplate<String, UserDTO> template;
    private static final long EXPIRATION_MINUTES = 5;

    public void saveCode(String email, String code) {
        String key = RedisKeys.VERIFICATION_CODE_PREFIX + email;
        redisTemplate.opsForValue().set(key, code, EXPIRATION_MINUTES, TimeUnit.MINUTES);
    }

    public void saveUser(UserDTO userDTO) {
        String key = RedisKeys.TEMP_USER_PREFIX + userDTO.getEmail();
        template.opsForValue().set(key, userDTO, EXPIRATION_MINUTES, TimeUnit.MINUTES);
    }

    public Optional<String> getCode(String email) {
        String key = RedisKeys.VERIFICATION_CODE_PREFIX + email;
        String code = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(code);
    }

    public Optional<UserDTO> getUser(String email) {
        String key = RedisKeys.TEMP_USER_PREFIX + email;
        UserDTO user = template.opsForValue().get(key);
        return Optional.ofNullable(user);
    }

    public void deleteCode(String email) {
        redisTemplate.delete(email);
    }
}
