package delivery.models.orders;


import delivery.models.items.Item;
import lombok.*;

import jakarta.persistence.*;

@Entity
@Table(name = "item_order_pool")
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ItemOrderPool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "item_id")
    private long itemId;

    @Column(nullable = false)
    private int count;
}
