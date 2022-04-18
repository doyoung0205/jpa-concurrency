package me.doyoung.jpaconcurrency.treatment.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Treatment {

    private static final String EMPTY_NAME_ERROR_MESSAGE = "진료명 입력해주세요.";
    public static final int DEFAULT_CAPACITY = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private int capacity;

    @Version
    private Integer version;

    public Treatment(String name) {
        validateName(name);
        this.capacity = DEFAULT_CAPACITY;
        this.name = name;
    }

    private void validateName(String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException(EMPTY_NAME_ERROR_MESSAGE);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Treatment)) return false;

        Treatment treatment = (Treatment) o;

        return getId().equals(treatment.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
