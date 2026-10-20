package com.pmt.iscod.services;

import com.pmt.iscod.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import com.pmt.iscod.User;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User registerUser(User user) {
    if (userRepository.findByEmail(user.getEmail()).isPresent()) {
        throw new IllegalArgumentException("Impossible de créer un compte avec cet email");
    }
    return userRepository.save(user);
    }

public User loginUser(String email, String password) {
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec cet email"));
    if (!user.getPassword().equals(password)) {
        throw new IllegalArgumentException("Mot de passe incorrect");
    }
    return user;
    }
}

