package info.novatec.metricscollector.github.remote;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.*;

import org.junit.Before;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;

import info.novatec.metricscollector.commons.rest.RestService;
import info.novatec.metricscollector.github.Metrics;
import info.novatec.metricscollector.github.collector.GithubBasicMetricCollector;


public class WiremockBaseTest {
    String WIREMOCK_GIT_API_URL = "http://localhost:8085/repos";
    String REPOSITORY_NAME = "marketing-metrics-collector";
    String GIT_BASE_URL = String.format("%s/%s", WIREMOCK_GIT_API_URL, REPOSITORY_NAME);

    @Autowired
    RestService restService;

    @Autowired
    Metrics metrics;

    @Spy
    GithubBasicMetricCollector githubBasicMetricCollector = new GithubBasicMetricCollector(restService, metrics) {
        @Override
        public void collect() {

        }
    };

    @Before
    public void setUp() {
        initMocks(this);
        doReturn(GIT_BASE_URL).when(githubBasicMetricCollector).getBaseRequestUrl();
    }
}
