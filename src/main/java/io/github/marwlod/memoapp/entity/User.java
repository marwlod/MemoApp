package io.github.marwlod.memoapp.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;

    @Column(name = "email")
    @NotEmpty(message = "Email required")
    @Email(message = "Invalid email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    @NotEmpty(message = "First name required")
    private String firstName;

    @Column(name = "last_name")
    @NotEmpty(message = "Last name required")
    private String lastName;

    @Column(name = "enabled")
    private int enabled;

    @ManyToMany(fetch = FetchType.EAGER,
                cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @OneToMany(mappedBy = "owner",
            cascade = CascadeType.ALL)
    private List<Memo> memos;

    public User() {
    }

    public User(@NotEmpty(message = "Email required") @Email(message = "Invalid email") String email, String password, @NotEmpty(message = "First name required") String firstName, @NotEmpty(message = "Last name required") String lastName, int enabled, Set<Role> roles) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.enabled = enabled;
        this.roles = roles;
    }
}
