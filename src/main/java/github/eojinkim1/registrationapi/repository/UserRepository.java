package github.eojinkim1.registrationapi.repository;


import github.eojinkim1.registrationapi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username); // null이 올 수 있는 값을 감싸는 Wrapper 클래스(Optional<T>)
    Optional<User> findByEmail(String email);
}
































/*
*
* package github.eojinkim1.registrationapi.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
*/
