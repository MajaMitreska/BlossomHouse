package mk.ukim.finki.wp.blossomhouse.model;

import lombok.Data;
import org.attoparser.dom.Text;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Product
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer price;

    private Integer quantity;

    @ManyToOne
    private Category category;

    @ManyToOne
    private Supplier supplier;

    private String image;

    private Integer likes;

    @Column(name="xxx_description", length = 1024)
    private String description;

    @Column(name="xxx_details", length = 1024)
    private String details;

    public Product(String name, Integer price, Integer quantity, Category category, Supplier supplier, String image, String description, String details) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.supplier = supplier;
        this.image = image;
        this.likes = 0;
        this.description = description;
        this.details = details;
    }

    public Product() {

    }
}
