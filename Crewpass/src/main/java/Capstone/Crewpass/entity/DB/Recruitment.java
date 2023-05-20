package Capstone.Crewpass.entity.DB;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Table(name = "recruitment")
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recruitment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // IDENTITY : 기본 키 생성을 데이터베이스에 위임 (= AUTO_INCREMENT)
    @Column(name = "recruitment_id", nullable = false)
    private Integer recruitmentId;

    @Column(name = "isDeleted")
    private Integer isDeleted;

    @Column(name = "title", nullable = false, length = 25)
    private String title;

    @Column(name = "register_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp registerTime;

    @Column(name = "deadline", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp deadline;

    @Column(name = "content", nullable = false, length = 5000)
    private String content;

    @Column(name = "image")
    private String image;

    @Column(name = "crew_crew_id", nullable = false)
    @JoinColumn(name = "crewId") // foreign key
    private Integer crewId;

    @Builder
    public Recruitment(Integer recruitmentId, Integer isDeleted, String title, Timestamp registerTime, Timestamp deadline, String content, String image, Integer crewId) {
        this.recruitmentId = recruitmentId;
        this.isDeleted = isDeleted;
        this.title = title;
        this.registerTime = registerTime;
        this.deadline = deadline;
        this.content = content;
        this.image = image;
        this.crewId = crewId;
    }
}
