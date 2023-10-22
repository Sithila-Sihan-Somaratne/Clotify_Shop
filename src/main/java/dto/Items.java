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
public class Items {
    @Id
    private String code;
    private String description;
    private int qty;
    private double sellingPrice;
    private double buyingPrice;
    private String type;
    private String size;
    private double profit;
    private String supplierID;
}
