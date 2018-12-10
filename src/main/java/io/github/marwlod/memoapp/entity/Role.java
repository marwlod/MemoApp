package io.github.marwlod.memoapp.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "role")
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

    public Role(String name, List<User> users) {
        this.name = name;
        this.users = users;
    }
}

