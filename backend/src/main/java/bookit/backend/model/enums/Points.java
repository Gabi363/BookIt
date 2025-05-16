package bookit.backend.model.enums;

import lombok.Getter;

@Getter
public enum Points {
    CLIENT_POINTS(5),
    BUSINESS_POINTS(1);

    private final int value;

    Points(final int value) {
        this.value = value;
    }

}
