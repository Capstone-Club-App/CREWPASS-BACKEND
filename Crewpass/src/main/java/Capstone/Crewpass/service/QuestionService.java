package Capstone.Crewpass.service;

import Capstone.Crewpass.entity.DB.Question;
import Capstone.Crewpass.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    // 질문 등록
    public String registerQuestion(Question question) {
        questionRepository.save(question);
        return "registerQuestion - success";
    }

    // questionId로 recruitmentId 찾기
    public Integer findRecruitmentId(Integer questionId) {
        Optional<Question> optionalQuestion = questionRepository.findByQuestionId(questionId);
        if (optionalQuestion.isPresent()) {
            return optionalQuestion.get().getRecruitmentId();
        } else {
            return null;
        }
    }

    // 선택한 질문 상세 조회
    public Optional<Question> checkQuestionDetail(Integer questionId) {
        return questionRepository.findByQuestionId(questionId);
    }
}

