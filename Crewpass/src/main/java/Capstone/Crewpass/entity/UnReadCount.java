package Capstone.Crewpass.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UnReadCount {
    private Integer count;

    public UnReadCount(Integer count) {
        this.count = count;
    }
}
