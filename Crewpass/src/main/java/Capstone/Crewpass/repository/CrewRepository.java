package Capstone.Crewpass.repository;

import Capstone.Crewpass.entity.Crew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CrewRepository extends JpaRepository<Crew, Integer> {
    Optional<Crew> findByCrewLoginId(String crewLoginId);

    Optional<Crew> findByCrewLoginIdAndCrewPw(String loginId, String password);
}
