package dto.tm;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import lombok.*;

import javax.persistence.Entity;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ItemsTM extends RecursiveTreeObject<ItemsTM> {
    private String code;
    private String description;
    private int qty;
    private double sellingPrice;
    private double buyingPrice;
    private String type;
    private String size;
    private double profit;
    private String supplierID;
    private JFXButton option;
}
