package Capstone.Crewpass.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Blob;

@Entity
@Data
@Table(name="user")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Integer userId;

    @Column(name="user_login_id")
    private String userLoginId;

    @Column(name="user_pw")
    private String userPw;

    @Column(name="user_name")
    private String userName;

    @Column(name="user_email")
    private String userEmail;

    @Column(name="job")
    private String job;

    @Column(name="school")
    private String school;

    @Column(name="user_profile")
    private Blob userProfile;

    public User(Integer userId, String userLoginId, String userPw, String userName, String userEmail, String job, String school, Blob userProfile) {
        this.userId = userId;
        this.userLoginId = userLoginId;
        this.userPw = userPw;
        this.userName = userName;
        this.userEmail = userEmail;
        this.job = job;
        this.school = school;
        this.userProfile = userProfile;
    }
}
