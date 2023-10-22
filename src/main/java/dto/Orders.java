package dto;


import dto.composite.OrdersCI;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(OrdersCI.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Orders {
    @Id
    private String orderId;
    @Id
    private String date;
    private double total;
    private String custName;
    private String custContact;
    private String custEmail;
    private String employerId;
    private String arrears;
}