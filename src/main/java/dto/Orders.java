package dto;


import lombok.*;

import javax.persistence.Column;
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
    @Column(name = "orderId")
    private String orderId;
    @Id
    @Column(name = "date")
    private String date;
    private double total;
    private String custName;
    private String custContact;
    private String custEmail;
    @Id
    @Column(name = "employerId")
    private String employerId;
    private String arrears;
}