package Capstone.Crewpass.repository;

import Capstone.Crewpass.entity.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecruitmentRepository extends JpaRepository<Recruitment, Integer> {
    Optional<Recruitment> findByRecruitmentId(Integer id);
}
