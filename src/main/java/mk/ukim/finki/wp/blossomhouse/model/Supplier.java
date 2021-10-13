package mk.ukim.finki.wp.blossomhouse.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data@Entity
public class Supplier
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;

    public Supplier(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public Supplier() {

    }
}
