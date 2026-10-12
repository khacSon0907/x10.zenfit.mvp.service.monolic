package x10.zenfit.mvp.service.config.location;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.location")
public class LocationProperties {

    private String baseUrl;
}