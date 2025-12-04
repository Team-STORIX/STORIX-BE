package com.storix.storix_api.global.config;

//import com.storix.storix_api.global.apiPayload.exception.KakaoInfoErrorDecoder;
//import com.storix.storix_api.global.apiPayload.exception.KakaoOIDCErrorDecoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.form.FormEncoder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.storix.storix_api")
public class FeignClientConfig {

    @Bean
    public Encoder formEncoder() { return new FormEncoder(); }

    // TODO: ErrorDecoder 관리

    // 카카오 OAuth
    @Configuration
    public static class KakaoInfoConfig {

//        @Bean
//        @ConditionalOnMissingBean(value = ErrorDecoder.class)
//        public ErrorDecoder kakaoInfoErrorDecoder() {
//            return new KakaoInfoErrorDecoder();
//        }
    }

    @Configuration
    public static class KakaoOauthConfig {

//        @Bean
//        @ConditionalOnMissingBean(value = ErrorDecoder.class)
//        public ErrorDecoder kakaoOauthErrorDecoder() {
//            return new KakaoOIDCErrorDecoder();
//        }

    }

    // 네이버 OAuth


}
