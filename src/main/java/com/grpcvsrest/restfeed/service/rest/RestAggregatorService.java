package com.grpcvsrest.restfeed.service.rest;

import com.grpcvsrest.restfeed.service.AggregatedContentResponse;
import com.grpcvsrest.restfeed.service.AggregatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * RESTful implementation of {@link AggregatorService}.
 */
@Service("rest-aggr-service")
public class RestAggregatorService implements AggregatorService {
    private final String url;
    private final RestTemplate restTemplate;

    @Autowired
    public RestAggregatorService(
            @Value("${rest_aggregator.url}") String url,
            RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    @Override
    public AggregatedContentResponse fetch(Integer id, String username) {
        ResponseEntity<AggregatedContentResponse> entity;
        try {
            entity = restTemplate.getForEntity(
                    url + "/content/{id}",
                    AggregatedContentResponse.class,
                    id);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 404) {
                return null;
            }
            throw e;
        }

        return entity.getBody();
    }
}
