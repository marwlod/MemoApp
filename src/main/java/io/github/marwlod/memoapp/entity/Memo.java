package io.github.marwlod.memoapp.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "memo")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "memo_type")
@Getter
@Setter
public abstract class Memo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memo_id")
    private Long id;

    @Column(name = "short_description")
    private String shortDescription;

    @Column(name = "memo_text")
    private String memoText;

    @ManyToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH })
    @JoinColumn(name = "memo_owner")
    private User owner;

    public Memo() {
    }

    public Memo(String shortDescription, String memoText, User owner) {
        this.shortDescription = shortDescription;
        this.memoText = memoText;
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Memo{" +
                "id=" + id +
                ", shortDescription='" + shortDescription + '\'' +
                ", memoText='" + memoText + '\'' +
                ", owner=" + owner +
                '}';
    }
}
