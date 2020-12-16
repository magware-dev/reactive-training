package dev.magware.reactivetraining.lecture03;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.services.s3.S3AsyncClient;

import java.net.URI;

@Configuration
public class AwsS3Config {

    @Bean
    public S3AsyncClient S3AsyncClient(
        @Value("${s3.accessKey}") String accessKey,
        @Value("${s3.secretKey}") String secretKey,
        @Value("${s3.endpoint}") String endpoint
    ) {
        return S3AsyncClient.builder()
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(accessKey, secretKey)
                )
            )
            .endpointOverride(URI.create(endpoint))
            .httpClient(NettyNioAsyncHttpClient.create())
            .build();
    }

}
