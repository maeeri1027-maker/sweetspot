package com.sweetspot.backend.controller;

import com.sweetspot.backend.entity.User;
import com.sweetspot.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // 新規会員登録API (/api/auth/register)
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        if (email == null || password == null || email.trim().isEmpty() || password.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "メールアドレスとパスワードを入力してください。"));
        }

        if (userRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "このメールアドレスは既に登録されています。"));
        }

        User user = new User(email, password);
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "ユーザー登録が完了しました。"));
    }

    // ログインAPI (/api/auth/login)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            User user = userOpt.get();

            Map<String, Object> response = new HashMap<>();
            response.put("token", "dummy-jwt-token-for-" + user.getId());
            response.put("user", Map.of("id", user.getId(), "email", user.getEmail()));

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "メールアドレスまたはパスワードが正しくありません。"));
        }
    }
}