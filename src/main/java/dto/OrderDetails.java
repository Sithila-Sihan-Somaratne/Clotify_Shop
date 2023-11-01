package dto;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemCode;
    private String description;
    private int qty;
    private double unitPrice;
    private String date;
    private double discount;
    private String type;
    private String size;
    private double amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId")
    private Orders order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code")
    private Items item;

    public OrderDetails(String itemCode, String description, int qty, double unitPrice, String date, double discount, String type, String size, double amount, Orders order, Items item) {
        this.itemCode = itemCode;
        this.description = description;
        this.qty = qty;
        this.unitPrice = unitPrice;
        this.date = date;
        this.discount = discount;
        this.type = type;
        this.size = size;
        this.amount = amount;
        this.order = order;
        this.item = item;
    }
}
