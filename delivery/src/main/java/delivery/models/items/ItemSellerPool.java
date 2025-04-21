package delivery.models.items;


import delivery.models.orders.Seller;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "item_seller_pool")
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ItemSellerPool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Seller seller;

    @Column(nullable = false)
    private int count;

    @Column(nullable = false)
    private double price;
}
