package Capstone.Crewpass.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Table(name = "application")
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="application_id")
    private Integer applicationId;

    @Column(name = "submit_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp submitTime;

    @Column(name="answer1", nullable = false, length = 3000)
    private String answer1;

    @Column(name="answer2", nullable = false, length = 3000)
    private String answer2;

    @Column(name="answer3", nullable = false, length = 3000)
    private String answer3;

    @Column(name="answer4", length = 3000)
    private String answer4;

    @Column(name="answer5", length = 3000)
    private String answer5;

    @Column(name="answer6", length = 3000)
    private String answer6;

    @Column(name="answer7", length = 3000)
    private String answer7;

    @Column(name="answer1_count", nullable = false)
    private Integer answer1Count;

    @Column(name="answer2_count", nullable = false)
    private Integer answer2Count;

    @Column(name="answer3_count", nullable = false)
    private Integer answer3Count;

    @Column(name="answer4_count")
    private Integer answer4Count;

    @Column(name="answer5_count")
    private Integer answer5Count;

    @Column(name="answer6_count")
    private Integer answer6Count;

    @Column(name="answer7_count")
    private Integer answer7Count;

    @Column(name = "user_user_id", nullable = false)
    @JoinColumn(name = "userId") // foreign key
    private Integer userId;

    @Column(name = "question_question_id", nullable = false)
    @JoinColumn(name = "questionId") // foreign key
    private Integer questionId;

    @Column(name = "question_recruitment_recruitment_id", nullable = false)
    @JoinColumn(name = "recruitmentId") // foreign key
    private Integer recruitmentId;

    @Builder
    public Application(Integer applicationId, Timestamp submitTime, String answer1, String answer2, String answer3, String answer4, String answer5, String answer6, String answer7, Integer answer1Count, Integer answer2Count, Integer answer3Count, Integer answer4Count, Integer answer5Count, Integer answer6Count, Integer answer7Count, Integer userId, Integer questionId, Integer recruitmentId) {
        this.applicationId = applicationId;
        this.submitTime = submitTime;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.answer5 = answer5;
        this.answer6 = answer6;
        this.answer7 = answer7;
        this.answer1Count = answer1Count;
        this.answer2Count = answer2Count;
        this.answer3Count = answer3Count;
        this.answer4Count = answer4Count;
        this.answer5Count = answer5Count;
        this.answer6Count = answer6Count;
        this.answer7Count = answer7Count;
        this.userId = userId;
        this.questionId = questionId;
        this.recruitmentId = recruitmentId;
    }
}