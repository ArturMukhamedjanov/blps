package delivery.models.items;

import lombok.*;

import jakarta.persistence.*;

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

    @Column(name = "seller_id")
    private long sellerId;

    @Column(nullable = false)
    private int count;

    @Column(nullable = false)
    private double price;
}
