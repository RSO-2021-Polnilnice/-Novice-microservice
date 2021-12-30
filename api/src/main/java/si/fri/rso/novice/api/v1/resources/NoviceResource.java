package si.fri.rso.novice.api.v1.resources;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.cdi.Log;
import com.kumuluz.ee.logs.cdi.LogParams;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.json.JSONArray;
import si.fri.rso.novice.api.v1.config.NoviceProperties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Log
@ConfigBundle("external-api")
@Path("/novice")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class NoviceResource {

    private static final Logger LOG = LogManager.getLogger(NoviceResource.class.getName());

    @Inject
    private NoviceProperties noviceProperties;

    @GET
    @Path("/test")
    public void testGet() {
        System.out.println("Calling GET test");
    }

    @Operation(description = "Send newsletter to users.", summary = "Send newsletter")
    @APIResponses({
            @APIResponse(responseCode = "201",
                    description = "Newsletter sent."
            ),
            @APIResponse(responseCode = "405", description = "Sending error.")
    })
    @Counted
    @POST
    @Log(LogParams.METRICS)
    @Path("/poslji")
    public Response sendEmail() throws UnirestException {
        HttpResponse<JsonNode> uporabnikiResponse = Unirest.get("http://uporabniki-instance:8080/v1/uporabniki/emails").asJson();

        System.out.println(uporabnikiResponse.getBody());

        JSONArray uporabnikiJson = uporabnikiResponse.getBody().getArray();

        System.out.println(noviceProperties.getUrl());
        System.out.println(noviceProperties.getApiKey());
        System.out.println(noviceProperties.getSenderMail());

        for (int i = 0; i < uporabnikiJson.length(); i++) {
            System.out.println(uporabnikiJson.getJSONObject(i).getString("email"));

            Unirest.post("https://api.mailgun.net/v3/" + noviceProperties.getUrl() + "/messages")
                    .basicAuth("api", noviceProperties.getApiKey())
                    .queryString("from", noviceProperties.getSenderMail())
                    .queryString("to", uporabnikiJson.getJSONObject(i).getString("email"))
                    .queryString("subject", "New charging station")
                    .queryString("text", "Hello " + uporabnikiJson.getJSONObject(i).getString("firstName") + " " + uporabnikiJson.getJSONObject(i).getString("lastName") + ". A new charging station has just been added!")
                    .asJson();
        }

        return Response.status(Response.Status.OK).build();
    }

}