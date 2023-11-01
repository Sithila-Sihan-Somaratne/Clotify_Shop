package dto;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Orders {
    @Id
    private String orderId;
    private String date;
    private double total;
    private String custName;
    private String custContact;
    private String custEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Employers employer;
}
