package com.grpcvsrest.restfeed.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class AggregatorService {
    private final String url;
    private final RestTemplate restTemplate;

    @Autowired
    public AggregatorService(
            @Value("${rest_aggregator.url}") String url,
            RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    public AggregatedContentResponse fetch(int id) {
        ResponseEntity<AggregatedContentResponse> entity = null;
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
