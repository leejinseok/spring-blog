package com.wonder.blog.service;

import com.wonder.blog.domain.User;
import com.wonder.blog.exception.DataDuplicateException;
import com.wonder.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email + " user not founded"));
    }

    public User addUser(String email, String name, String password) throws DataDuplicateException {
        User user = User.builder()
          .email(email)
          .name(name)
          .password(bCryptPasswordEncoder.encode(password))
          .build();

        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new DataDuplicateException("이미 존재하는 회원입니다.");
        }
    }
}
