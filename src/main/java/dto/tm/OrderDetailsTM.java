package dto.tm;


import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class OrderDetailsTM extends RecursiveTreeObject<OrderDetailsTM> {
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