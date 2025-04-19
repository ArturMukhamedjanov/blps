package delivery.models;

import lombok.*;

import javax.persistence.*;

@ToString
@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @ManyToOne
    @JoinColumn(name = "deliverer_id")
    private Deliverer deliverer;

    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "description")
    private String description;

    @Column(name = "seller_description")
    private String sellerDescription;
}
