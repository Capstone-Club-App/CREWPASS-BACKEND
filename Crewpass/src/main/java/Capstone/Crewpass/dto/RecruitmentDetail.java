package Capstone.Crewpass.dto;

import java.sql.Timestamp;

public interface RecruitmentDetail {
    Integer getCrew_id();
    String getCrew_profile();
    String getCrew_name();
    String getRegion1();
    String getRegion2();
    String getField1();
    String getField2();
    String getTitle();
    String getContent();
    String getImage();
    Timestamp getRegister_time();
    Timestamp getDeadline();
    Integer getQuestion_id();
}
