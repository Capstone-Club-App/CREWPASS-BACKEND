package Capstone.Crewpass.service;

import Capstone.Crewpass.config.ChatGptConfig;
import Capstone.Crewpass.dto.ChatGptRequest;
import Capstone.Crewpass.dto.ChatGptResponse;
import Capstone.Crewpass.entity.DB.Application;
import Capstone.Crewpass.entity.DB.Question;
import Capstone.Crewpass.repository.ApplicationRepository;
import Capstone.Crewpass.repository.QuestionRepository;
import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Optional;

@Slf4j
@Service
public class AiService {
    private final ApplicationRepository applicationRepository;
    private final QuestionRepository questionRepository;
    private final ChatgptService chatgptService;


    @Autowired
    public AiService(ApplicationRepository applicationRepository, QuestionRepository questionRepository, ChatgptService chatgptService) {
        this.applicationRepository = applicationRepository;
        this.questionRepository = questionRepository;
        this.chatgptService = chatgptService;
    }

    public ChatGptResponse analyzeApplication(Integer applicationId) throws NoSuchFieldException {
        //application 찾기
        Optional<Application> optionalApplication = applicationRepository.findByApplicationId(applicationId);
        //question 찾기
        Integer questionId = optionalApplication.get().getQuestionId();
        Optional<Question> optionalQuestion = questionRepository.findByQuestionId(questionId);
        //문항 별 question-answer 배열 생성
        String[] questionAnswer1 = {optionalQuestion.get().getQuestion1(), optionalApplication.get().getAnswer1()};
        String[] questionAnswer2 = {optionalQuestion.get().getQuestion2(), optionalApplication.get().getAnswer2()};
        String[] questionAnswer3 = {optionalQuestion.get().getQuestion3(), optionalApplication.get().getAnswer3()};
        String[] questionAnswer4 = {optionalQuestion.get().getQuestion4(), optionalApplication.get().getAnswer4()};
        String[] questionAnswer5 = {optionalQuestion.get().getQuestion5(), optionalApplication.get().getAnswer5()};
        String[] questionAnswer6 = {optionalQuestion.get().getQuestion6(), optionalApplication.get().getAnswer6()};
        String[] questionAnswer7 = {optionalQuestion.get().getQuestion7(), optionalApplication.get().getAnswer7()};
        //question-answer HashMap 생성
        HashMap<Integer, String[]> questionAnswerHashMap = new HashMap<Integer, String[]>();
        questionAnswerHashMap.put(1, questionAnswer1);
        questionAnswerHashMap.put(2, questionAnswer2);
        questionAnswerHashMap.put(3, questionAnswer3);
        questionAnswerHashMap.put(4, questionAnswer4);
        questionAnswerHashMap.put(5, questionAnswer5);
        questionAnswerHashMap.put(6, questionAnswer6);
        questionAnswerHashMap.put(7, questionAnswer7);
        //chatGPT API에게 전송할 요청
        Integer questionCount = optionalQuestion.get().getQuestionCount();
        String content = "";
        for(int i = 1 ; i <= questionCount ; i++){
            String[] questionAnswer = questionAnswerHashMap.get(i);
            String question = "Q" + i + ". " + questionAnswer[0] + "\n";
            String answer = "A" + i + ". " + questionAnswer[1] + "\n\n";
            content += answer;
            //content += question + answer;
        }
        String prompt = "The following is the application form written by the applicant who applied for our club recruitment. "
                + "The language is written in Korean.\n\n"
                + content
                + "First, please briefly summarize the answers written by the applicant for each application question in Korean. "
                + "Summarize the summary for each question with a title such as \"A1\".\n"
                + "Second, please recommend 7 interview questions in Korean to ask the applicant about the overall contents of the application. "
                + "When making recommendations, please number them under the title \"면접 질문 추천\".\n"
                + "Please do not return all other content except these two requests as a result.";
        //chatGPT API에게 요청 전송
        ChatGptResponse response = askQuestionToChatGpt(prompt);
        return response;
    }

    private static RestTemplate restTemplate = new RestTemplate();

    public HttpEntity<ChatGptRequest> createHttpEntity(ChatGptRequest chatGptRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(ChatGptConfig.MEDIA_TYPE));
        headers.add(ChatGptConfig.AUTHORIZATION, ChatGptConfig.BEARER + ChatGptConfig.CertificateKey);
        return new HttpEntity<>(chatGptRequest, headers);
    }

    public ChatGptResponse getResponse(HttpEntity<ChatGptRequest> chatGptRequest) {
        ResponseEntity<ChatGptResponse> responseEntity = restTemplate.postForEntity(
                ChatGptConfig.URL,
                chatGptRequest,
                ChatGptResponse.class);
        return responseEntity.getBody();
    }

    public ChatGptResponse askQuestionToChatGpt(String prompt) {
        return this.getResponse(
                this.createHttpEntity(
                        ChatGptRequest.builder()
                                .model(ChatGptConfig.MODEL)
                                .prompt(prompt)
                                .maxTokens(ChatGptConfig.MAX_TOKEN)
                                .temperature(ChatGptConfig.TEMPERATURE)
                                .topP(ChatGptConfig.TOP_P)
                                .build()));
    }
}