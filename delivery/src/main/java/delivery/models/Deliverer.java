package delivery.models;

import delivery.models.auth.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "deliverers")
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Deliverer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "_user")
    private User user;

    @Column(name = "distance", nullable = false)
    private Distance distance;

    @Column(name = "is_free", nullable = false)
    private boolean isFree;

}
