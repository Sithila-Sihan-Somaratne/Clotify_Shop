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
public class OrderDetails {
    @Id
    private String itemCode;
    private String description;
    private int qty;
    private double unitPrice;
    private String date;
    private double discount;
    private String type;
    private String size;
    private double amount;
}