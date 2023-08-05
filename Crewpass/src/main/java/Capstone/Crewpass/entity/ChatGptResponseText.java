package Capstone.Crewpass.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@Data
@Getter
@Setter
public class ChatGptResponseText {
    Integer questionCount;
    HashMap<Integer, String[]> questionAnswerHashMap;
    List<String> summary;
    List<String> interview;

    public ChatGptResponseText(Integer questionCount, HashMap<Integer, String[]> questionAnswerHashMap, List<String> summary, List<String> interview) {
        this.questionCount = questionCount;
        this.questionAnswerHashMap = questionAnswerHashMap;
        this.summary = summary;
        this.interview = interview;
    }
}
