package dto.AccessTablesFromOtherClasses_ATFOC;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ItemsATFOC extends RecursiveTreeObject<ItemsATFOC> {
    private String itemCode;
    private String description;
    private int qty;
}
