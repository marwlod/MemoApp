package io.github.marwlod.memoapp.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import static io.github.marwlod.memoapp.entity.EntityUtil.EXPIRATION_MINS;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "verification_token")
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "token")
    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "expiration_date")
    private Date expirationDate;

    private Date calculateExpirationDate(int expirationTimeInMins) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Timestamp(calendar.getTime().getTime()));
        calendar.add(Calendar.MINUTE, expirationTimeInMins);
        return new Date(calendar.getTime().getTime());
    }

    public VerificationToken(String token) {
        this.token = token;
        this.expirationDate = calculateExpirationDate(EXPIRATION_MINS);
    }

    public VerificationToken(String token, User user) {
        this.token = token;
        this.user = user;
        this.expirationDate = calculateExpirationDate(EXPIRATION_MINS);
    }
}
