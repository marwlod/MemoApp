package io.github.marwlod.memoapp.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "role")
@Getter
@Setter
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "roles",
                cascade = CascadeType.ALL)
    private List<User> users;

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }
}
