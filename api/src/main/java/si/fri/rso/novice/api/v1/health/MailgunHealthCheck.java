package si.fri.rso.novice.api.v1.health;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

import javax.enterprise.context.ApplicationScoped;
import java.net.HttpURLConnection;
import java.net.URL;

@Readiness
@ApplicationScoped
public class MailgunHealthCheck implements HealthCheck {

    private static final String url = "https://www.mailgun.com/";

    @Override
    public HealthCheckResponse call() {
        try {

            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("HEAD");

            if (connection.getResponseCode() == 200) {
                return HealthCheckResponse.named(MailgunHealthCheck.class.getSimpleName()).up().build();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return HealthCheckResponse.named(MailgunHealthCheck.class.getSimpleName()).down().build();
    }
}