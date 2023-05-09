package Capstone.Crewpass.dto;

import java.sql.Timestamp;

public interface ApplicationDetail {
    String getUser_profile();
    String getUser_name();

    Timestamp getSubmit_time();

    String getQuestion1();
    String getQuestion2();
    String getQuestion3();
    String getQuestion4();
    String getQuestion5();
    String getQuestion6();
    String getQuestion7();

    String getQuestion1_limit();
    String getQuestion2_limit();
    String getQuestion3_limit();
    String getQuestion4_limit();
    String getQuestion5_limit();
    String getQuestion6_limit();
    String getQuestion7_limit();

    String getAnswer1();
    String getAnswer2();
    String getAnswer3();
    String getAnswer4();
    String getAnswer5();
    String getAnswer6();
    String getAnswer7();

    String getAnswer1_count();
    String getAnswer2_count();
    String getAnswer3_count();
    String getAnswer4_count();
    String getAnswer5_count();
    String getAnswer6_count();
    String getAnswer7_count();
}
