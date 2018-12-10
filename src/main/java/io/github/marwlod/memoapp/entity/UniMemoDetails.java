package io.github.marwlod.memoapp.entity;

import io.github.marwlod.memoapp.audit.AbstractMemoAuditable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "uni_memo_details")
@NoArgsConstructor
@Getter
@Setter
public class UniMemoDetails extends AbstractMemoAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uni_memo_details_id")
    private Long id;

    @NotNull(message = "Field required")
    @Future(message = "Date due cannot be in the past or present")
    @Temporal(value = TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "due_date")
    private Date dueDate;

    @OneToOne(mappedBy = "uniMemoDetails",
            cascade = CascadeType.ALL)
    private UniMemo uniMemo;

    public UniMemoDetails(Date dueDate, UniMemo uniMemo) {
        this.dueDate = dueDate;
        this.uniMemo = uniMemo;
    }

    @Override
    public String toString() {
        return "UniMemoDetails{" +
                "id=" + id +
                ", dueDate=" + dueDate +
                '}' + super.toString();
    }
}
