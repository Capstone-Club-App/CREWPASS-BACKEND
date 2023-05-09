package Capstone.Crewpass.entity;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CertificateNumb {
    private Integer certificateNumb;

    public CertificateNumb(Integer certificateNumb) {
        this.certificateNumb = certificateNumb;
    }
}
