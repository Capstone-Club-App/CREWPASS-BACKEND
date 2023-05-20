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

    public class QuestionSet{
        String question1;
        String question2;
        String question3;
        String question4;
        String question5;
        String question6;
        String question7;
    }

    public class AnswerSet{
        String answer1;
        String answer2;
        String answer3;
        String answer4;
        String answer5;
        String answer6;
        String answer7;
    }

    public void analyzeApplication(Integer applicationId) throws NoSuchFieldException {
        QuestionSet questionSet = new QuestionSet();
        AnswerSet answerSet = new AnswerSet();
        Optional<Application> optionalApplication = applicationRepository.findByApplicationId(applicationId);
        //분석할 지원서 답변 찾기
        answerSet.answer1 = optionalApplication.get().getAnswer1();
        answerSet.answer2 = optionalApplication.get().getAnswer2();
        answerSet.answer3 = optionalApplication.get().getAnswer3();
        answerSet.answer4 = optionalApplication.get().getAnswer4();
        answerSet.answer5 = optionalApplication.get().getAnswer5();
        answerSet.answer6 = optionalApplication.get().getAnswer6();
        answerSet.answer7 = optionalApplication.get().getAnswer7();
        //분석할 지원서에 대한 질문 찾기
        Integer questionId = optionalApplication.get().getQuestionId();
        Optional<Question> optionalQuestion = questionRepository.findByQuestionId(questionId);
        Integer questionCount = optionalQuestion.get().getQuestionCount();
        questionSet.question1 = optionalQuestion.get().getQuestion1();
        questionSet.question2 = optionalQuestion.get().getQuestion2();
        questionSet.question3 = optionalQuestion.get().getQuestion3();
        questionSet.question4 = optionalQuestion.get().getQuestion4();
        questionSet.question5 = optionalQuestion.get().getQuestion5();
        questionSet.question6 = optionalQuestion.get().getQuestion6();
        questionSet.question7 = optionalQuestion.get().getQuestion7();
        //chatGPT API에게 전송할 요청
        String content = "";
        for(int i=1 ; i<questionCount ; i++){
            String questionSetCount = "question" + i;
            String answerSetCount = "answer" + i;
            String question = "Q" + i + ". " + questionSet.getClass().getDeclaredField(questionSetCount) + "\n";
            String answer = "A" + i + ". " + answerSet.getClass().getDeclaredField(answerSetCount) + "\n\n";
            String questionAnswer = question + answer;
            content += questionAnswer;
        }
        String command2Ai = "다음은 우리 동아리 모집에 지원한 지원자가 작성한 지원서입니다.\n"
                + content
                + "지원서 문항 별로 지원자가 작성한 답변을 간략히 요약해주십시오.\n"
                + "또한, 해당 지원서의 전체적인 내용에 대해서 지원자에게 질문할 면접 질문을 7개 추천해주십시오";
        log.info(command2Ai);
        /*
        //chatGPT에게 전달
        OpenAiService openAiService = new OpenAiService("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");
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt(command2Ai)
                .model("text-davinci-003")
                .echo(false)
                .build();
        String result = openAiService.createCompletion(completionRequest).getChoices().toString();
        log.info(result);
         */
    }
}