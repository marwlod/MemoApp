package io.github.marwlod.memoapp.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@DiscriminatorValue("uni_memo")
@Getter
@Setter
@ToString(callSuper = true)
public class UniMemo extends Memo {

    @Column(name = "subject")
    private String subject;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "uni_memo_details_id")
    private UniMemoDetails uniMemoDetails;

    public UniMemo() {
    }

    public UniMemo(String shortDescription, String memoText, User owner, String subject, UniMemoDetails uniMemoDetails) {
        super(shortDescription, memoText, owner);
        this.subject = subject;
        this.uniMemoDetails = uniMemoDetails;
    }
}
