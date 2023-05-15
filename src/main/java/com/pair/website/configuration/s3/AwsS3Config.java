package com.pair.website.configuration.s3;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsS3Config {
    // @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey = "AKIAUK7PQC2BFNY6ZPPP";

    // @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey = "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";

    // @Value("${cloud.aws.region.static}")
    private String region = "ap-northeast-2";

    @Bean
    public AmazonS3 amazonS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();
    }

}