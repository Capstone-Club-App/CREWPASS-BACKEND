package Capstone.Crewpass.repository;

import Capstone.Crewpass.dto.ApplicationRecentList;
import Capstone.Crewpass.dto.RecruitmentRecentList;
import Capstone.Crewpass.entity.Application;
import Capstone.Crewpass.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {
    // Application Id로 찾기
    Optional<Application> findByApplicationId(Integer id);

    // User Id로 해당 회원이 지원한 지원서 목록 조회
    @Query(value = "SELECT a.user_user_id," +
            " c.crew_profile, c.crew_name," +
            " a.application_id, a.submit_time" +
            " FROM crewpass.application a" +
            " INNER JOIN crewpass.crew c ON a.question_recruitment_crew_crew_id = c.crew_id" +
            " WHERE a.user_user_id = :userId", nativeQuery = true)
    List<ApplicationRecentList> findMyApplicationList(@Param("userId") Integer userId);
}
