package Capstone.Crewpass.repository;

import Capstone.Crewpass.dto.ApplicationDetail;
import Capstone.Crewpass.dto.ApplicationRecentListByCrew;
import Capstone.Crewpass.dto.ApplicationRecentListByUser;
import Capstone.Crewpass.entity.DB.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {

    // User Id로 해당 회원이 지원한 지원서 목록 조회
    @Query(value = "SELECT a.user_user_id, " +
            "c.crew_profile, c.crew_name, " +
            "a.application_id, a.submit_time, a.is_pass " +
            "FROM crewpass.application a " +
            "INNER JOIN crewpass.question q ON a.question_question_id = q.question_id " +
            "INNER JOIN crewpass.recruitment r ON r.recruitment_id = q.recruitment_recruitment_id " +
            "INNER JOIN crewpass.crew c ON r.crew_crew_id = c.crew_id " +
            "WHERE a.user_user_id = :userId and a.is_deleted = 0", nativeQuery = true)
    List<ApplicationRecentListByUser> findMyApplicationList(@Param("userId") Integer userId);

    // 선택한 지원서 상세 조회
    @Query(value = "SELECT u.user_profile, u.user_name," +
            "  a.submit_time," +
            "  q.question1, q.question2, q.question3, q.question4, q.question5, q.question6, q.question7," +
            "  q.question1_limit, q.question2_limit, q.question3_limit, q.question4_limit, q.question5_limit, q.question6_limit, q.question7_limit," +
            "  a.answer1, a.answer2, a.answer3, a.answer4, a.answer5, a.answer6, a.answer7, " +
            "  a.answer1_count, a.answer2_count, a.answer3_count, a.answer4_count, a.answer5_count, a.answer6_count, a.answer7_count" +
            "  FROM crewpass.application a" +
            "  INNER JOIN crewpass.user u ON u.user_id = a.user_user_id" +
            "  INNER JOIN crewpass.question q ON q.question_id = a.question_question_id" +
            "  WHERE a.application_id = :applicationId"
            , nativeQuery = true)
    List<ApplicationDetail> getApplicationDetail(@Param("applicationId") Integer applicationId);

    // 선택한 모집글에 대한 지원서를 최신순으로 목록 조회
    @Query(value = "SELECT u.user_id, u.user_profile, u.user_name," +
            " a.application_id, a.submit_time, a.is_pass" +
            " FROM crewpass.application a" +
            " INNER JOIN crewpass.user u ON a.user_user_id = u.user_id" +
            " INNER JOIN crewpass.question q ON a.question_question_id = q.question_id" +
            " WHERE q.question_id = :questionId and a.is_deleted = 0" +
            " ORDER BY a.submit_time DESC"
            , nativeQuery = true)
    List<ApplicationRecentListByCrew> findApplicationListByQuestion(@Param("questionId") Integer questionId);

    // 지원서 삭제
    @Modifying
    @Transactional
    @Query(value = "UPDATE crewpass.application SET is_deleted = 1 WHERE application_id = :applicationId AND user_user_id = :userId", nativeQuery = true)
    void deleteApplication(@Param("applicationId") Integer applicationId, @Param("userId") Integer userId);

    Optional<Application> findByApplicationId(Integer applicationId);

    Application findByUserIdAndRecruitmentId(Integer userId, Integer recruitmentId);
}
