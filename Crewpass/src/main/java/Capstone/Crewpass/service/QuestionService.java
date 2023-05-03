package Capstone.Crewpass.service;

import Capstone.Crewpass.entity.Question;
import Capstone.Crewpass.entity.Recruitment;
import Capstone.Crewpass.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    // 질문 등록
    public String registerQuestion(Question question) {
        if (validateDuplicateQuestion(question) != null) {
            questionRepository.save(question);
            return "registerQuestion - success";
        } else {
            return null;
        }
    }

    // 중복 질문 검증
    private String validateDuplicateQuestion(Question question) {
        Optional<Question> optionalQuestion = questionRepository.findByRecruitmentId(question.getRecruitmentId());
        if (optionalQuestion.isPresent()) {
            return null;
        } else {
            return "validateDuplicateQuestion - success";
        }
    }
}

