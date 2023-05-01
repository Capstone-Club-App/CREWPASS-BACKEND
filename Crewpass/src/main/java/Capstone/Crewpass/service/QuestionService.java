package Capstone.Crewpass.service;

import Capstone.Crewpass.entity.Question;
import Capstone.Crewpass.repository.QuestionRepository;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    // 질문 등록
    public void registerQuestion(Question question) {
        questionRepository.save(question);
    }
}

