package io.github.marwlod.memoapp.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@DiscriminatorValue("uni_memo")
@Getter
@Setter
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

    @Override
    public String toString() {
        return "UniMemo{" +
                "subject='" + subject + '\'' +
                ", uniMemoDetails=" + uniMemoDetails +
                "} " + super.toString();
    }
}
