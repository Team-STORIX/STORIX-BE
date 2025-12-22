package com.storix.storix_api.domains.user.application.client;

import lombok.Getter;

@Getter
public class KakaoProperties {

    private final String baseUri;
    private final String clientId;
    private final String clientSecret;

    public KakaoProperties(String baseUri, String clientId, String clientSecret) {
        this.baseUri = baseUri;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

}