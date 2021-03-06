package info.novatec.metricscollector.github.collector;

import org.springframework.stereotype.Component;

import info.novatec.metricscollector.commons.rest.RestService;
import info.novatec.metricscollector.github.Metrics;


@Component
public class OpenIssues extends GithubBasicMetricCollector implements GithubMetricCollector {

    public OpenIssues(RestService restService, Metrics metrics) {
        super(restService, metrics);
    }

    @Override
    public void collect() {
        metrics.addMetric("openIssues", getProjectRepository().getInt("open_issues_count"));
    }
}
