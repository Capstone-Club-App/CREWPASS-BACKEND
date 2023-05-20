package Capstone.Crewpass.entity.DB;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "crew")
@Getter
@Setter
@NoArgsConstructor
public class Crew {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="crew_id")
    private Integer crewId;

    @Column(name="crew_login_id")
    private String crewLoginId;

    @Column(name="crew_pw")
    private String crewPw;

    @Column(name="crew_name")
    private String crewName;

    @Column(name="region1")
    private String region1;

    @Column(name="region2")
    private String region2;

    @Column(name="field1")
    private String field1;

    @Column(name="field2")
    private String field2;

    @Column(name="crew_master_email")
    private String crewMasterEmail;

    @Column(name="crew_sub_email")
    private String crewSubEmail;

    @Column(name = "crew_profile")
    private String crewProfile;

    public Crew(Integer crewId, String crewLoginId, String crewPw, String crewName, String region1, String region2, String field1, String field2, String crewMasterEmail, String crewSubEmail, String crewProfile) {
        this.crewId = crewId;
        this.crewLoginId = crewLoginId;
        this.crewPw = crewPw;
        this.crewName = crewName;
        this.region1 = region1;
        this.region2 = region2;
        this.field1 = field1;
        this.field2 = field2;
        this.crewMasterEmail = crewMasterEmail;
        this.crewSubEmail = crewSubEmail;
        this.crewProfile = crewProfile;
    }
}
