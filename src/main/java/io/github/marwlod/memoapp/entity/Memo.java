package io.github.marwlod.memoapp.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import static io.github.marwlod.memoapp.entity.EntityUtil.LOB_LENGTH;
import static io.github.marwlod.memoapp.entity.EntityUtil.TEXT_LENGTH;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "memo_type")
@Table(name = "memo")
public abstract class Memo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memo_id")
    private Long id;

    @NotEmpty(message = "Field required")
    @Size(max = TEXT_LENGTH, message = "maximum " + TEXT_LENGTH + " characters")
    @Column(name = "short_description")
    private String shortDescription;

    @Lob
    @NotEmpty(message = "Field required")
    @Size(max = LOB_LENGTH, message = "too long didn''t read, maximum " + LOB_LENGTH + " characters")
    @Column(name = "memo_text", length = LOB_LENGTH)
    private String memoText;

    @ManyToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH })
    @JoinColumn(name = "memo_owner")
    private User owner;

    public Memo(String shortDescription, String memoText, User owner) {
        this.shortDescription = shortDescription;
        this.memoText = memoText;
        this.owner = owner;
    }
}
