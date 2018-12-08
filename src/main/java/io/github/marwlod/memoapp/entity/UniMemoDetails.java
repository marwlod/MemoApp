package io.github.marwlod.memoapp.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "uni_memo_details")
public class UniMemoDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uni_memo_details_id")
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "creation_date")
    private Date creationDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "last_modification")
    private Date lastModification;

    @Version
    @Column(name = "version")
    private int version;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "uni_memo_id")
    private UniMemo uniMemo;

    public UniMemoDetails() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastModification() {
        return lastModification;
    }

    public void setLastModification(Date lastModification) {
        this.lastModification = lastModification;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public UniMemo getUniMemo() {
        return uniMemo;
    }

    public void setUniMemo(UniMemo uniMemo) {
        this.uniMemo = uniMemo;
    }
}
