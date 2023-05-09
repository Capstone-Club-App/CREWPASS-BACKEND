package Capstone.Crewpass.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ApplicationId {
    private Integer applicationId;

    public ApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }
}
