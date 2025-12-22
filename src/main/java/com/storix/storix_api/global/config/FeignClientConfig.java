package com.storix.storix_api.global.config;

import com.storix.storix_api.global.apiPayload.exception.web.KakaoInfoErrorDecoder;
import com.storix.storix_api.global.apiPayload.exception.web.KakaoOauthErrorDecoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.form.FormEncoder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.storix.storix_api")
public class FeignClientConfig {

    @Bean
    public Encoder formEncoder() { return new FormEncoder(); }

    // 카카오 OAuth
    @Configuration
    public static class KakaoOauthConfig {

        @Bean
        public ErrorDecoder kakaoOauthErrorDecoder() {
            return new KakaoOauthErrorDecoder();
        }

    }

    @Configuration
    public static class KakaoInfoConfig {

        @Bean
        public ErrorDecoder kakaoInfoErrorDecoder() {
            return new KakaoInfoErrorDecoder();
        }

    }

    // 네이버 OAuth


}
