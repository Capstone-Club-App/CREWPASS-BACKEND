package Capstone.Crewpass.repository;

import Capstone.Crewpass.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    // Recruitment Id로 찾기
    // Recruitment : Question = 1 : 1 관계이므로 RecruitmentId로 찾는 것으로 진행함
    Optional<Question> findByRecruitmentId(Integer recruitmentId);

    Optional<Question> findByQuestionId(Integer questionId);
}
