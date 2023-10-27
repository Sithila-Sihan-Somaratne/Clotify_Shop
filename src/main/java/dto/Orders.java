package dto;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Orders {
    @Id
    private String orderId;
    private String date;
    private double total;
    private String custName;
    private String custContact;
    private String custEmail;
    private String employerId;
    private String arrears;
}