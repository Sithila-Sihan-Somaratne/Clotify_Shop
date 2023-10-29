package dto.tm.withoutBtn;


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
public class OrdersTM extends RecursiveTreeObject<OrdersTM> {
    @Id
    private String orderId;
    private String date;
    private double total;
    private String custName;
    private String custContact;
    private String custEmail;
    private String employerId;
}