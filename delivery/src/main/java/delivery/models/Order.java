package delivery.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "order")
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
    @JoinColumn(name = "store_id")
    private Seller seller;

    @ManyToOne
    @JoinColumn(name = "deliverer_id")
    private Deliverer deliverer;

    @Column(name = "status")
    private OrderStatus status;
}
