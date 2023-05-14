package Capstone.Crewpass.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RecruitmentId {
    private Integer recruitmentId;

    public RecruitmentId(Integer recruitmentId) {
        this.recruitmentId = recruitmentId;
    }
}
