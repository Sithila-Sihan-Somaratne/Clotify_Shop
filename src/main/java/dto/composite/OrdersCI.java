package dto.composite;


import dto.Orders;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class OrdersCI implements Serializable {
    private String orderId;
    private String date;
    private double total;
    private String custName;
    private String custContact;
    private String custEmail;
    private String employerId;
    private String arrears;
}