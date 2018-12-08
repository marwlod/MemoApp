package io.github.marwlod.memoapp.entity;

import javax.persistence.*;

@Entity
@DiscriminatorValue("uni_memo")
public class UniMemo extends Memo {

    @Column(name = "subject")
    private String subject;

    @OneToOne(mappedBy = "uniMemo",
            cascade = CascadeType.ALL)
    private UniMemoDetails uniMemoDetails;

    public UniMemo() {
    }

    public UniMemo(String title, String shortDescription, String memoText, User owner, String subject, UniMemoDetails uniMemoDetails) {
        super(title, shortDescription, memoText, owner);
        this.subject = subject;
        this.uniMemoDetails = uniMemoDetails;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public UniMemoDetails getUniMemoDetails() {
        return uniMemoDetails;
    }

    public void setUniMemoDetails(UniMemoDetails uniMemoDetails) {
        this.uniMemoDetails = uniMemoDetails;
    }
}
