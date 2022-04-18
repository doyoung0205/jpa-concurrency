package me.doyoung.jpaconcurrency.reservation.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.doyoung.jpaconcurrency.reservation.domain.validator.ReservationValidator;
import org.hibernate.Hibernate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import java.time.LocalDateTime;
import java.util.Objects;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {@UniqueConstraint(name = "unique_name", columnNames = "name")})
public class Reservation {

    private static final String EMPTY_NAME_ERROR_MESSAGE = "예약자의 이름을 입력해주세요.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long treatmentId;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime reservationDate;

    @Version
    private Integer version;

    private Reservation(Long treatmentId, String name) {
        validateName(name);
        this.name = name;
        this.treatmentId = treatmentId;
    }

    private void validateName(String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException(EMPTY_NAME_ERROR_MESSAGE);
        }
    }

    public static Reservation from(Long treatmentId, String name, ReservationValidator validator) {
        final Reservation reservation = new Reservation(treatmentId, name);
        validator.validate(reservation);
        return reservation;
    }

    public static Reservation getFakeInstance(Long treatmentId, String name) {
        return new Reservation(treatmentId, name);
    }

    public Integer getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "Reservation[" + name + ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Reservation that = (Reservation) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
