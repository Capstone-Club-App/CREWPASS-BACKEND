package Capstone.Crewpass.dto;

import java.sql.Timestamp;

public interface ApplicationRecentListByUser {
    Integer getUser_user_id();
    String getCrew_profile();
    String getCrew_name();
    Integer getApplication_id();
    Timestamp getSubmit_time();
}
