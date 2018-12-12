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
// hibernate creates only one table for the whole inheritance tree,
// some columns may not be populated for every object, depending on fields
// that a particular object has, additional column for storing type of object
// (discriminator column)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "memo_type")
@Table(name = "memo")
abstract class Memo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memo_id")
    private Long id;

    @NotEmpty
    @Size(max = TEXT_LENGTH)
    @Column(name = "short_description")
    private String shortDescription;

    // longer text field
    @Lob
    @NotEmpty
    @Size(max = LOB_LENGTH)
    @Column(name = "memo_text", length = LOB_LENGTH)
    private String memoText;

    // don't delete owner when deleting memo
    @ManyToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH })
    @JoinColumn(name = "memo_owner")
    private User owner;

    Memo(String shortDescription, String memoText, User owner) {
        this.shortDescription = shortDescription;
        this.memoText = memoText;
        this.owner = owner;
    }
}
