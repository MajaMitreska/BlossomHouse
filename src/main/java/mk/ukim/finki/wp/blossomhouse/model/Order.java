package mk.ukim.finki.wp.blossomhouse.model;


import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order
{
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    public Long id;

    @ManyToMany
    public List<Product> products;

    public Integer totalAmount;

    private LocalDateTime dateOrdered;

    @ManyToOne
    private BlossomUser user;

    private String name;

    private String address;

    private String phoneNumber;

    private String email;

    public Order(BlossomUser user, String name, String address, String phoneNumber, String email, Integer totalAmount) {
        this.products = new ArrayList<>();
        this.totalAmount = totalAmount;
        this.dateOrdered = LocalDateTime.now();
        this.user = user;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public Order() {

    }
}
