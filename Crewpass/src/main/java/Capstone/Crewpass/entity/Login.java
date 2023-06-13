package Capstone.Crewpass.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Login {
    private String crew_user_id;
    private String loginId;
    private String password;

    public Login(String crew_user_id, String loginId, String password) {
        this.crew_user_id = crew_user_id;
        this.loginId = loginId;
        this.password = password;
    }
}
