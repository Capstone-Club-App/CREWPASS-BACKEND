package Capstone.Crewpass.dto;

import java.sql.Timestamp;

public interface ApplicationRecentListByCrew {
    Integer getUser_id();
    String getUser_profile();
    String getUser_name();
    Integer getApplication_id();
    Timestamp getSubmit_time();
}
