package dto;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Items extends RecursiveTreeObject<Items> {
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
