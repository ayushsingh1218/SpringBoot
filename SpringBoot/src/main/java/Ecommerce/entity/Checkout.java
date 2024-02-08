package Ecommerce.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Checkout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "checkout", cascade = CascadeType.ALL)
    private List<Cart> cartItems;

    @Column(name= "delivery_address")
    private String deliveryAddress;

    @Column(name = "bill_id")
    private String billId;



}