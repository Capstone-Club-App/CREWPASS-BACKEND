package Capstone.Crewpass.repository;

import Capstone.Crewpass.entity.DB.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    Optional<Question> findByQuestionId(Integer questionId);
}
