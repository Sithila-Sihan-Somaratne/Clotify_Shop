package dto.tm;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SuppliersTM extends RecursiveTreeObject<SuppliersTM> {
    @Id
    private String id;
    private String title;
    private String name;
    private String company;
    private String contact;
    private JFXButton option;
}
