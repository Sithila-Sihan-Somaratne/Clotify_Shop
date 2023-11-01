
package dto.tm;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import lombok.*;


@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderDetailTM extends RecursiveTreeObject<OrderDetailTM> {
    private String itemCode;
    private String description;
    private int qty;
    private double unitPrice;
    private String date;
    private double discount;
    private String type;
    private String size;
    private double amount;
    private JFXButton option;
}
