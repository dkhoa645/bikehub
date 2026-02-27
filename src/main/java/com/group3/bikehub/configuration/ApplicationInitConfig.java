package com.group3.bikehub.configuration;

import com.group3.bikehub.entity.Enum.RoleEnum;
import com.group3.bikehub.entity.Role;
import com.group3.bikehub.entity.User;
import com.group3.bikehub.repository.RoleRepository;
import com.group3.bikehub.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.BeanDefinitionDsl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(RoleRepository roleRepository, UserRepository userRepository) {
        return args -> {

            Arrays.stream(RoleEnum.values()).forEach(roleEnum -> {
                        roleRepository.findByName(roleEnum.toString())
                                .orElseGet(() -> {
                                    return roleRepository.save(
                                            Role.builder().name(roleEnum.toString()).build());
                                });
                    });
            if(userRepository.findByUsername("admin").isEmpty()) {
                User userAdmin = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .name("admin")
                        .roles(Set.of(Role.builder().name("ADMIN").build()))
                        .build();
                userRepository.save(userAdmin);
                if(userRepository.findByUsername("buyer").isEmpty()) {
                    User buyer = User.builder()
                            .username("buyer")
                            .password(passwordEncoder.encode("string"))
                            .name("buyer")
                            .roles(Set.of(Role.builder().name("BUYER").build()))
                            .build();
                    userRepository.save(buyer);
                }
                    if(userRepository.findByUsername("seller").isEmpty()) {
                        User seller = User.builder()
                                .username("seller")
                                .password(passwordEncoder.encode("string"))
                                .name("seller")
                                .roles(Set.of(Role.builder().name("SELLER").build()))
                                .build();
                        userRepository.save(seller);
                    }
                        if(userRepository.findByUsername("inspector").isEmpty()) {
                            User inspector = User.builder()
                                    .username("inspector")
                                    .password(passwordEncoder.encode("string"))
                                    .name("inspector")
                                    .roles(Set.of(Role.builder().name("INSPECTOR").build()))
                                    .build();
                            userRepository.save(inspector);
                        }
            }
                if(userRepository.findByUsername("buyer").isEmpty()) {
                    User buyer = User.builder()
                            .username("buyer")
                            .password(passwordEncoder.encode("admin"))
                            .name("buyer")
                            .roles(Set.of(Role.builder().name("BUYER").build()))
                            .build();
                    userRepository.save(buyer);
                }
                    if(userRepository.findByUsername("seller").isEmpty()) {
                        User seller = User.builder()
                                .username("seller")
                                .password(passwordEncoder.encode("admin"))
                                .name("seller")
                                .roles(Set.of(Role.builder().name("SELLER").build()))
                                .build();
                        userRepository.save(seller);
                    }


            log.warn("admin user has been created with default password: admin, please change it");
        };
    }


}
