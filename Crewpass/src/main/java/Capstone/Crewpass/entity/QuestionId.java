package Capstone.Crewpass.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class QuestionId {
    private Integer questionId;

    public QuestionId(Integer questionId) {
        this.questionId = questionId;
    }
}
