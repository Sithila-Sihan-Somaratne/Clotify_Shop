package dto;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Employers {
    @Id
    private String id;
    private String title;
    private String name;
    private String NIC;
    private String DOB;
    private String address;
    private String contact;
    private String BBAN;
    private String bank_branch;
}
