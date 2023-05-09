package Capstone.Crewpass.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Login {
    private String loginId;
    private String password;

    public Login(String loginId) {
        this.loginId = loginId;
    }

    public Login(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}
