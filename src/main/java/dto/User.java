package dto;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User {
    @Id
    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;
    private String password;
    private String type;
}