package ru.endienasg.foxworld.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.endienasg.foxworld.models.User;

public interface IUserRepository extends JpaRepository<User, String> {
    User save(User user);
    User findByUsername(String username);
}