package Capstone.Crewpass.entity;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "recruitment_scrap")
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Scrap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // IDENTITY : 기본 키 생성을 데이터베이스에 위임 (= AUTO_INCREMENT)
    @Column(name = "scrap_id", nullable = false)
    private Integer scrapId;

    @Column(name = "recruitment_recruitment_id", nullable = false)
    @JoinColumn(name = "recruitmentId") // foreign key
    private Integer recruitmentId;

    @Column(name = "user_user_id", nullable = false)
    @JoinColumn(name = "userId") // foreign key
    private Integer userId;

    @Builder
    public Scrap(Integer scrapId, Integer recruitmentId, Integer userId) {
        this.scrapId = scrapId;
        this.recruitmentId = recruitmentId;
        this.userId = userId;
    }
}
