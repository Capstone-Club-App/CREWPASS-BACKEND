package Capstone.Crewpass.entity;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "question")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id", nullable = false)
    private Integer questionId;

    // 질문
    @Column(name = "question1", nullable = false, length = 45)
    private String question1;

    @Column(name = "question2", nullable = false, length = 45)
    private String question2;

    @Column(name = "question3", nullable = false, length = 45)
    private String question3;

    @Column(name = "question4", length = 45)
    private String question4;

    @Column(name = "question5", length = 45)
    private String question5;

    @Column(name = "question6", length = 45)
    private String question6;

    @Column(name = "question7", length = 45)
    private String question7;

    // 글자 제한 수
    @Column(name = "question1_limit", nullable = false)
    private Integer question1Limit;

    @Column(name = "question2_limit", nullable = false)
    private Integer question2Limit;

    @Column(name = "question3_limit", nullable = false)
    private Integer question3Limit;

    @Column(name = "question4_limit")
    private Integer question4Limit;

    @Column(name = "question5_limit")
    private Integer question5Limit;

    @Column(name = "question6_limit")
    private Integer question6Limit;

    @Column(name = "question7_limit")
    private Integer question7Limit;

    @Column(name = "recruitment_recruitment_id", nullable = false)
    @JoinColumn(name = "recruitmentId") // foreign key
    private Integer recruitmentId;

    @Builder
    public Question(Integer questionId, String question1, String question2, String question3, String question4, String question5, String question6, String question7, Integer question1Limit, Integer question2Limit, Integer question3Limit, Integer question4Limit, Integer question5Limit, Integer question6Limit, Integer question7Limit, Integer recruitmentId) {
        this.questionId = questionId;
        this.question1 = question1;
        this.question2 = question2;
        this.question3 = question3;
        this.question4 = question4;
        this.question5 = question5;
        this.question6 = question6;
        this.question7 = question7;
        this.question1Limit = question1Limit;
        this.question2Limit = question2Limit;
        this.question3Limit = question3Limit;
        this.question4Limit = question4Limit;
        this.question5Limit = question5Limit;
        this.question6Limit = question6Limit;
        this.question7Limit = question7Limit;
        this.recruitmentId = recruitmentId;
    }
}
