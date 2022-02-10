package me.doyoung.jpaconcurrency.reservation.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.doyoung.jpaconcurrency.reservation.domain.validator.ReservationValidator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class Reservation {

    private static final String EMPTY_NAME_ERROR_MESSAGE = "예약자의 이름을 입력해주세요.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Version
    private Integer version;

    private Reservation(String name) {
        validateName(name);
        this.name = name;
    }

    private void validateName(String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException(EMPTY_NAME_ERROR_MESSAGE);
        }
    }

    public static Reservation from(String name, ReservationValidator validator) {
        final Reservation reservation = new Reservation(name);
        validator.validate(reservation);
        return reservation;
    }

    public static Reservation getFakeInstance(String name) {
        return new Reservation(name);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "name='" + name + '\'' +
                '}';
    }
}
