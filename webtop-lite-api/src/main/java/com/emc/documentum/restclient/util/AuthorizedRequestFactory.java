/*
 * Copyright (c) 2016. EMC Coporation. All Rights Reserved.
 */

package com.emc.documentum.restclient.util;

import java.io.IOException;
import java.net.HttpURLConnection;

import org.springframework.http.client.SimpleClientHttpRequestFactory;

public final class AuthorizedRequestFactory extends
        SimpleClientHttpRequestFactory {
    private String authorization;

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    @Override
    protected void prepareConnection(HttpURLConnection connection,
            String httpMethod) throws IOException {
        super.prepareConnection(connection, httpMethod);
        if (authorization != null) {
            connection.setRequestProperty("Authorization", authorization);
        }
    }
}
