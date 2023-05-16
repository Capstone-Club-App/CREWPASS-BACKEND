package Capstone.Crewpass.repository;

import Capstone.Crewpass.entity.Crew;
import Capstone.Crewpass.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserLoginId(String userLoginId);

    Optional<User> findByUserLoginIdAndUserPw(String loginId, String password);

    Optional<User> findByUserEmail(String email);

    Optional<User> findByUserLoginIdAndUserEmail(String loginId, String email);

    Optional<User> findByUserId(String userId);
}
