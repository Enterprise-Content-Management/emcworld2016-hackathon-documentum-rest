/*
 * Copyright (c) 2016. EMC Coporation. All Rights Reserved.
 */

package com.emc.documentum.restclient;

import java.net.URI;
import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.emc.documentum.restclient.util.AuthorizedRequestFactory;

public class DctmRestTemplate {
    private static final Log LOGGER = LogFactory.getLog(DctmRestTemplate.class);
    private static final MediaType DCTM_VND_JSON_TYPE = MediaType.parseMediaType("application/vnd.emc.documentum+json");

    protected RestTemplate restTemplate;
    protected HttpHeaders defaultHeaders;

    public DctmRestTemplate(String loginName, String password, boolean streaming) {
        String usernameAndPassword = String.format("%s:%s", loginName, password);
        String auth = String.format("Basic %s", new String(Base64.encodeBase64(usernameAndPassword.getBytes())));
        this.restTemplate = newRestTemplate(auth, streaming);
        this.defaultHeaders = defaultHttpHeaders();
    }

    public <T> ResponseEntity<T> get(String uri, Class<T> responseBodyClass, String... params) {
        return sendRequest(null, HttpMethod.GET, uri, responseBodyClass, params);
    }

    public <E, T> ResponseEntity<T> post(String uri, E requestBody, Class<T> responseBodyClass, String... params) {
        return sendRequest(requestBody, HttpMethod.POST, uri, responseBodyClass, params);
    }

    public <E, T> ResponseEntity<T> put(String uri, E requestBody, Class<T> responseBodyClass, String... params) {
        return sendRequest(requestBody, HttpMethod.PUT, uri, responseBodyClass, params);
    }

    public ResponseEntity delete(String uri, String... params) {
        return sendRequest(null, HttpMethod.DELETE, uri, null, params);
    }

    protected  <T, R> ResponseEntity<T> sendRequest(
            R requestBody,
            HttpMethod httpMethod,
            String url,
            Class<T> responseBodyClass,
            String... params) {

        MultiValueMap<String, String> paramMap = buildParams(params);
        String requestUri = url;
        requestUri = UriComponentsBuilder.fromHttpUrl(requestUri).queryParams(paramMap).build(true).toUriString();
        RequestEntity<R> request = new RequestEntity<>(requestBody, defaultHttpHeaders(), httpMethod, URI.create(requestUri));

        logRequest(requestUri, httpMethod, defaultHttpHeaders());
        org.springframework.http.ResponseEntity<T> response = restTemplate.exchange(request, responseBodyClass);
        logResponse(response.getStatusCode(), response.getHeaders());

        return response;
    }

    protected HttpHeaders defaultHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(DCTM_VND_JSON_TYPE, MediaType.APPLICATION_JSON));
        headers.setContentType(DCTM_VND_JSON_TYPE);
        return headers;
    }

    private MultiValueMap<String, String> buildParams(String[] params) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        if (params != null) {
            for (int i = 0; i < params.length; i += 2) {
                map.add(params[i], params[i + 1]);
            }
        }
        return map;
    }

    private void logRequest(String requestUri, HttpMethod httpMethod, HttpHeaders headers) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("\r\n---------->> Sending request [%s %s] with HTTP headers: %s", httpMethod,
                    requestUri, headers == null ? "[NULL]" : headers.toString()));
        }
    }

    private void logResponse(HttpStatus statusCode, HttpHeaders headers) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("\r\n<<---------- Receiving response [%s] and HTTP headers: %s",
                    statusCode.toString(), headers == null ? "[NULL]" : headers.toString()));
        }
    }

    private RestTemplate newRestTemplate(String authorization, boolean streaming) {
        AuthorizedRequestFactory factory = new AuthorizedRequestFactory();
        if (authorization != null) {
            factory.setAuthorization(authorization);
        }
        factory.setBufferRequestBody(!streaming);
        return new RestTemplate(factory);
    }
}
