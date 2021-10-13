package mk.ukim.finki.wp.blossomhouse.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Category
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public Category() {

    }

    public Category (String name)
    {
        this.name = name;
    }
}
