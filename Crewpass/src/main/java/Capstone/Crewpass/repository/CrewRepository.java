package Capstone.Crewpass.repository;

import Capstone.Crewpass.entity.Crew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CrewRepository extends JpaRepository<Crew, Integer> {
    Optional<Crew> findByCrewLoginId(String crewLoginId);

    Optional<Crew> findByCrewLoginIdAndCrewPw(String loginId, String password);

    @Query(value="SELECT * " +
            "FROM crewpass.crew c " +
            "WHERE c.crew_name = :crewName AND c.crew_master_email = :email " +
            "OR c.crew_name = :crewName AND c.crew_sub_email= :email",
            nativeQuery = true
    )
    Optional<Crew> findByCrewNameAndCrewEmail(@Param("crewName") String crewName, @Param("email") String email);
}
