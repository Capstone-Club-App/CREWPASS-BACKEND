package Capstone.Crewpass.repository;

import Capstone.Crewpass.dto.RecruitmentDeadlineList;
import Capstone.Crewpass.dto.ScrapRecruitmentDeadlineList;
import Capstone.Crewpass.entity.Scrap;
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
public interface ScrapRepository extends JpaRepository<Scrap, Integer> {

    // 스크랩 취소
    @Modifying
    @Query(value = "DELETE FROM crewpass.recruitment_scrap s WHERE s.scrap_id = :scrapId AND s.user_user_id = :userId", nativeQuery = true)
    void deleteScrap(@Param("scrapId") Integer scrapId, @Param("userId") Integer userId);

    // 스크랩한 모집글을 "마감임박" 순으로 목록 조회
    @Query(value = "SELECT s.scrap_id, " +
            " c.crew_id, c.crew_profile, c.crew_name, c.region1, c.region2, c.field1, c.field2," +
            " r.recruitment_id, r.title, r.register_time," +
            " q.question_id" +
            " FROM crewpass.recruitment_scrap s" +
            " INNER JOIN crewpass.user u ON s.user_user_id = u.user_id" +
            " INNER JOIN crewpass.recruitment r ON s.recruitment_recruitment_id = r.recruitment_id" +
            " INNER JOIN crewpass.crew c ON r.crew_crew_id = c.crew_id" +
            " INNER JOIN crewpass.question q ON r.recruitment_id = q.recruitment_recruitment_id" +
            " WHERE u.user_id = :userId" +
            " ORDER BY now() - r.deadline DESC, r.recruitment_id"
            , nativeQuery = true)
    List<ScrapRecruitmentDeadlineList> findAllScrapListByDeadline(@Param("userId") Integer userId);
}
