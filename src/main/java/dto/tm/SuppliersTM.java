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
public class SuppliersTM extends RecursiveTreeObject<SuppliersTM> {
    private String id;
    private String title;
    private String name;
    private String contact;
    private String company;
    private JFXButton option;
}
