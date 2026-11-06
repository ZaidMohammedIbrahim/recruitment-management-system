package com.zaid.recruitment.Repository;

import com.zaid.recruitment.Auth.UserEntity;
import com.zaid.recruitment.Auth.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Test
    public void shouldAddUser() {
        UserEntity user = new UserEntity();
        user.setName("test");
       user.setPassword("123");
        user.setRole("ROLE_USER");
        userRepository.save(user);
        Optional<UserEntity> optionalUser = userRepository.findByName("test");
        assertEquals(user.getName(), optionalUser.get().getName());
        assertEquals(user.getPassword(), optionalUser.get().getPassword());
        assertEquals(user.getRole(), optionalUser.get().getRole());


    }


}
