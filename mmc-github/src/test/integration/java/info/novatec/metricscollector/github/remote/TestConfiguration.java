package info.novatec.metricscollector.github.remote;

import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import info.novatec.metricscollector.commons.rest.RestService;
import info.novatec.metricscollector.github.Metrics;
import info.novatec.metricscollector.github.collector.Commits;
import info.novatec.metricscollector.github.collector.GithubBasicMetricCollector;


/**
 * Custom configuration class that aims to load the minimum required application context.
 */
public class TestConfiguration {
    private static final String VALID_GITHUB_URL = "https://github.com/nt-ca-aqe/marketing-metrics-collector";

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    Metrics metrics() {
        return new Metrics(VALID_GITHUB_URL);
    }

    @Bean
    RestService restService() {
        return new RestService(restTemplate()).prepareRequest().addHttpHeader("Authorization", "token " + "Invalid");
    }

    @Bean
    GithubBasicMetricCollector githubBasicMetricCollector() {
        return new GithubBasicMetricCollector(restService(), metrics()) {
            @Override
            public void collect() {

            }
        };
    }

    @Bean
    Commits commits() {
        return new Commits(restService(), metrics());
    }
}
