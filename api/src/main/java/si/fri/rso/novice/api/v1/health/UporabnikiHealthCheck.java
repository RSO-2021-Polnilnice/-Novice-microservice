package si.fri.rso.novice.api.v1.health;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;
import si.fri.rso.novice.api.v1.config.NoviceProperties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.net.HttpURLConnection;
import java.net.URL;

@Readiness
@ApplicationScoped
public class UporabnikiHealthCheck implements HealthCheck {

    @Inject
    private NoviceProperties noviceProperties;

    @Override
    public HealthCheckResponse call() {
        try {

            HttpURLConnection connection = (HttpURLConnection) new URL(noviceProperties.getUporabnikiUrl()).openConnection();
            connection.setRequestMethod("HEAD");

            if (connection.getResponseCode() == 200) {
                return HealthCheckResponse.named(UporabnikiHealthCheck.class.getSimpleName()).up().build();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return HealthCheckResponse.named(UporabnikiHealthCheck.class.getSimpleName()).down().build();
    }
}