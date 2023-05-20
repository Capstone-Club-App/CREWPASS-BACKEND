package Capstone.Crewpass.service;

import Capstone.Crewpass.entity.DB.Application;
import Capstone.Crewpass.entity.DB.Question;
import Capstone.Crewpass.repository.ApplicationRepository;
import Capstone.Crewpass.repository.QuestionRepository;
import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class AiService {
    private final ApplicationRepository applicationRepository;
    private final QuestionRepository questionRepository;

    @Autowired
    public AiService(ApplicationRepository applicationRepository, QuestionRepository questionRepository) {
        this.applicationRepository = applicationRepository;
        this.questionRepository = questionRepository;
    }

    public void analyzeApplication(Integer applicationId) {
        Optional<Application> optionalApplication = applicationRepository.findByApplicationId(applicationId);
        //분석할 지원서 답변 찾기
        String answer1 = optionalApplication.get().getAnswer1();
        String answer2 = optionalApplication.get().getAnswer2();
        String answer3 = optionalApplication.get().getAnswer3();
        String answer4 = optionalApplication.get().getAnswer4();
        String answer5 = optionalApplication.get().getAnswer5();
        String answer6 = optionalApplication.get().getAnswer6();
        String answer7 = optionalApplication.get().getAnswer7();
        //분석할 지원서에 대한 질문 찾기
        Integer questionId = optionalApplication.get().getQuestionId();
        Optional<Question> optionalQuestion = questionRepository.findByQuestionId(questionId);
        Integer questionCount = optionalQuestion.get().getQuestionCount();
        String question1 = optionalQuestion.get().getQuestion1();
        String question2 = optionalQuestion.get().getQuestion2();
        String question3 = optionalQuestion.get().getQuestion3();
        String question4 = optionalQuestion.get().getQuestion4();
        String question5 = optionalQuestion.get().getQuestion5();
        String question6 = optionalQuestion.get().getQuestion6();
        String question7 = optionalQuestion.get().getQuestion7();
        //chatGPT API에게 전송할 요청
        String command2Ai = "다음은 우리 동아리 모집에 지원한 지원자가 작성한 지원서입니다.\n"
                + "1. " + answer1 + "\n"
                + "2. " + answer2 + "\n"
                + "3. " + answer3 + "\n"
                + "4. " + answer4 + "\n"
                + "5. " + answer5 + "\n"
                + "6. " + answer6 + "\n"
                + "7. " + answer7 + "\n"
                + "내용이 null 값이 아닌 지원서 답변만을 문항 별로 간략히 요약해주십시오.\n"
                + "또한, 해당 지원서의 전체적인 내용에 대해서 지원자에게 질문할 면접 질문을 추천해주십시오";
        //chatGPT에게 전달
        OpenAiService openAiService = new OpenAiService("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");
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt(command2Ai)
                .model("text-davinci-003")
                .echo(false)
                .build();
        String result = openAiService.createCompletion(completionRequest).getChoices().toString();
        log.info(result);
    }
}