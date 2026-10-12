package x10.zenfit.mvp.service.config.payos;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.payos.PayOS;
import vn.payos.core.ClientOptions;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(PayOSProperties.class)
public class PayOSConfig {

    private final PayOSProperties properties;

    @Bean
    public PayOS payOS() {
        return new PayOS(
                ClientOptions.builder()
                        .clientId(properties.getClientId())
                        .apiKey(properties.getApiKey())
                        .checksumKey(properties.getChecksumKey())
                        .build()
        );
    }
}