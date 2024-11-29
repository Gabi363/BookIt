package bookit.backend.config.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties("security.jwt")
public class JwtProperties {

    @NotBlank
    private String secretKey;
    @NotNull
    private Integer expirationTime;
}
