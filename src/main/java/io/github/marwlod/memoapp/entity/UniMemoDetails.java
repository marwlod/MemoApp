package io.github.marwlod.memoapp.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "uni_memo_details")
@Getter
@Setter
public class UniMemoDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uni_memo_details_id")
    private Long id;

    @Temporal(value = TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "due_date")
    private Date dueDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date")
    private Date creationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_modification")
    private Date lastModified;

    @OneToOne(mappedBy = "uniMemoDetails",
            cascade = CascadeType.ALL)
    private UniMemo uniMemo;

    public UniMemoDetails() {
    }

    @Override
    public String toString() {
        return "UniMemoDetails{" +
                "id=" + id +
                ", dueDate=" + dueDate +
                ", creationDate=" + creationDate +
                ", lastModification=" + lastModified +
                '}';
    }
}
