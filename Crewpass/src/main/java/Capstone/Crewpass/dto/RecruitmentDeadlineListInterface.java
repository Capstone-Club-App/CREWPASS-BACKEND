package Capstone.Crewpass.dto;

import java.sql.Timestamp;

public interface RecruitmentDeadlineListInterface {
    Integer getCrew_id();
    String getCrew_profile();
    String getCrew_name();
    String getRegion1();
    String getRegion2();
    String getField1();
    String getField2();
    Integer getRecruitment_id();
    String getTitle();
    Timestamp getRegister_time();
    Timestamp getDeadline();
    Integer getQuestion_id();
}
