package dto.tm.withoutBtn;


import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class OrderDetailsTM extends RecursiveTreeObject<OrderDetailsTM> {
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