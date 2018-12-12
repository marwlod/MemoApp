package io.github.marwlod.memoapp.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import static io.github.marwlod.memoapp.entity.EntityUtil.TEXT_LENGTH;

@Entity
@DiscriminatorValue("uni_memo")
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
public class UniMemo extends Memo {
    // particular kind of memo - university memo

    @NotEmpty
    @Size(max = TEXT_LENGTH)
    @Column(name = "subject")
    private String subject;

    // also check validity of that object's fields
    @Valid
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "uni_memo_details_id")
    private UniMemoDetails uniMemoDetails;

    public UniMemo(String shortDescription, String memoText, User owner, String subject, UniMemoDetails uniMemoDetails) {
        super(shortDescription, memoText, owner);
        this.subject = subject;
        this.uniMemoDetails = uniMemoDetails;
    }
}
