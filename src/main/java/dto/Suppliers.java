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
public class Suppliers {
    @Id
    private String id;
    private String title;
    private String name;
    private String contact;
    private String company;
}
