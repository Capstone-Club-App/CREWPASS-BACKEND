package Capstone.Crewpass.repository;

import Capstone.Crewpass.dto.RecruitmentDeadlineList;
import Capstone.Crewpass.dto.RecruitmentRecentList;
import Capstone.Crewpass.entity.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecruitmentRepository extends JpaRepository<Recruitment, Integer> {
    // Recruitment Id로 찾기
    Optional<Recruitment> findByRecruitmentId(Integer id);

    // Crew Id로 해당 동아리의 모집글 목록 조회
    @Query(value = "SELECT c.crew_id, c.crew_profile, c.crew_name, c.region1, c.region2, c.field1, c.field2," +
            " r.recruitment_id, r.title, r.register_time," +
            " q.question_id" +
            " FROM crewpass.recruitment r " +
            " INNER JOIN crewpass.crew c ON r.crew_crew_id = c.crew_id " +
            " INNER JOIN crewpass.question q ON r.recruitment_id = q.recruitment_recruitment_id" +
            " WHERE c.crew_id = :crewId", nativeQuery = true)
    List<RecruitmentRecentList> findMyRecruitmentList(@Param("crewId") Integer crewId);

    // "전체" 동아리의 모집글 목록 "최신순"으로 조회
    @Query(value = "SELECT c.crew_id, c.crew_profile, c.crew_name, c.region1, c.region2, c.field1, c.field2," +
            " r.recruitment_id, r.title, r.register_time," +
            " q.question_id" +
            " FROM crewpass.recruitment r " +
            " INNER JOIN crewpass.crew c ON r.crew_crew_id = c.crew_id " +
            " INNER JOIN crewpass.question q ON r.recruitment_id = q.recruitment_recruitment_id" +
            " ORDER BY r.register_time DESC, r.recruitment_id"
            , nativeQuery = true)
    List<RecruitmentRecentList> findAllRecruitmentListByNewest();

    // 동아리 "분야 별" "최신순"으로 모집글 목록 조회
    @Query(value = "SELECT c.crew_id, c.crew_profile, c.crew_name, c.region1, c.region2, c.field1, c.field2," +
            " r.recruitment_id, r.title, r.register_time," +
            " q.question_id" +
            " FROM crewpass.recruitment r " +
            " INNER JOIN crewpass.crew c ON r.crew_crew_id = c.crew_id " +
            " INNER JOIN crewpass.question q ON r.recruitment_id = q.recruitment_recruitment_id" +
            " WHERE c.field1 = :field OR c.field2 = :field" +
            " ORDER BY r.register_time DESC, r.recruitment_id"
            , nativeQuery = true)
    List<RecruitmentRecentList> findFieldRecruitmentListByNewest(@Param("field") String field);

    // "전체" 동아리의 모집글 목록 "마감임박순"으로 조회
    @Query(value = "SELECT c.crew_id, c.crew_profile, c.crew_name, c.region1, c.region2, c.field1, c.field2," +
            " r.recruitment_id, r.title, r.register_time, r.deadline," +
            " q.question_id" +
            " FROM crewpass.recruitment r " +
            " INNER JOIN crewpass.crew c ON r.crew_crew_id = c.crew_id " +
            " INNER JOIN crewpass.question q ON r.recruitment_id = q.recruitment_recruitment_id" +
            " ORDER BY now() - r.deadline DESC, r.recruitment_id"
            , nativeQuery = true)
    List<RecruitmentDeadlineList> findAllRecruitmentListByDeadline();

    // 동아리 "분야 별" "마감임박순"으로 모집글 목록 조회
    @Query(value = "SELECT c.crew_id, c.crew_profile, c.crew_name, c.region1, c.region2, c.field1, c.field2," +
            " r.recruitment_id, r.title, r.register_time, r.deadline," +
            " q.question_id" +
            " FROM crewpass.recruitment r " +
            " INNER JOIN crewpass.crew c ON r.crew_crew_id = c.crew_id " +
            " INNER JOIN crewpass.question q ON r.recruitment_id = q.recruitment_recruitment_id" +
            " WHERE c.field1 = :field OR c.field2 = :field" +
            " ORDER BY now() - r.deadline DESC, r.recruitment_id"
            , nativeQuery = true)
    List<RecruitmentDeadlineList> findFieldRecruitmentListByDeadline(@Param("field") String field);

    //    // Crew Id로 모집글 찾기
//    Optional<Recruitment> findByCrewId(Integer id);
}
