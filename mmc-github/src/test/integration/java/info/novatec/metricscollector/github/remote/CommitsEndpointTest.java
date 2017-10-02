package info.novatec.metricscollector.github.remote;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import info.novatec.metricscollector.github.Metrics;
import info.novatec.metricscollector.github.collector.Commits;


@RunWith(SpringRunner.class)
@WebMvcTest
@ContextConfiguration(classes = TestConfiguration.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = GithubApplicationInitializer.class)
@AutoConfigureWireMock(port = 8085)
public class CommitsEndpointTest extends WiremockBaseTest {
    private String AUTH_VALID_TOKEN = "token " + "bcc6d022a59eb38b6399eb17afb820eb8136a7af";
    private String METRICS_MAP_KEY = "commits";

    @Autowired
    private Commits commits;

    @Test
    @Ignore
    public void wiremockStubForCommmits() throws Exception {
        Metrics metrics = commits.getMetrics();
        String responseBody = "[{},{},{},{},{}]"; ///repos/marketing-metrics-collector
        Integer expectedNumOfCommits = 5;

        //@formatter:off
        stubFor(
            get(urlPathEqualTo("/commits"))
                .withHeader(AUTHORIZATION, equalTo(AUTH_VALID_TOKEN))
                .willReturn(
                    aResponse().withStatus(ACCEPTED.value())
                        .withBody(responseBody)));
        //@formatter:on

        commits.collect();
        Integer actualNumberOfCommits = metrics.getMetrics().get(METRICS_MAP_KEY);

        assertThat(actualNumberOfCommits).isEqualTo(expectedNumOfCommits);
    }

    @Test
    @Ignore
    public void wiremockStubForCommmitsWithInvalidToken() throws Exception {
        stubFor(get(urlPathEqualTo("/commits")).withHeader(AUTHORIZATION, equalTo("token " + "Invalid"))
            .willReturn(aResponse().withStatus(UNAUTHORIZED.value())));
        try {
            commits.collect();
        } catch (HttpClientErrorException e) {
            assertThat(e.getStatusCode()).isEqualTo(UNAUTHORIZED);
        }
    }

    @Test
    @Ignore
    public void wiremockStubForCommmitsWithoutAuthorizationHeader() throws Exception {
        //TODO to verify what status code sends Github when headers are missing
        stubFor(get(urlPathEqualTo("/commits")).willReturn(aResponse().withStatus(UNAUTHORIZED.value())));
        try {
            commits.collect();
        } catch (HttpClientErrorException e) {
            assertThat(e.getStatusCode()).isEqualTo(UNAUTHORIZED);
        }
    }
}
