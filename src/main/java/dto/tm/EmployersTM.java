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
public class EmployersTM extends RecursiveTreeObject<EmployersTM> {
    private String id;
    private String title;
    private String name;
    private String NIC;
    private String DOB;
    private String address;
    private String contact;
    private String BBAN;
    private String bank_branch;
    private JFXButton option;
}
