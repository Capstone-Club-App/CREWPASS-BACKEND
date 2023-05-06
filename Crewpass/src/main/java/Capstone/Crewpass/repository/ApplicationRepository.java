package Capstone.Crewpass.repository;

import Capstone.Crewpass.entity.Application;
import Capstone.Crewpass.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {
    Optional<Application> findByApplicationId(Integer id);
}
