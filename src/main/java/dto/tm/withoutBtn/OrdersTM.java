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
public class OrdersTM extends RecursiveTreeObject<OrdersTM> {
    private String orderId;
    private String date;
    private double total;
    private String custName;
    private String custContact;
    private String custEmail;
    private String employerId;
}