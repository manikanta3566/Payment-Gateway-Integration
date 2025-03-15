package com.project.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Value("${region}")
    private String awsRegion;

    @Value("${accessKey}")
    private String awsAccessKey;

    @Value("${secretKey}")
    private String awsSecretKey;

    @Autowired
    private ObjectMapper objectMapper;


    @Bean
    public DataSource dataSource() throws JsonProcessingException {
        String secret = getSecret();
        JsonNode jsonNode = objectMapper.readTree(secret);
        String username = jsonNode.get("username").textValue();
        String password = jsonNode.get("password").textValue();
        String engine = jsonNode.get("engine").textValue();
        String host = jsonNode.get("host").textValue();
        int port = jsonNode.get("port").asInt();
        String dbName = jsonNode.get("dbname").textValue();
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setJdbcUrl("jdbc:" + engine + "://" + host + ":" + port + "/" + dbName);
        return dataSource;
    }


    public String getSecret() {

        String secretName = "mysqldb-secret";
        Region region = Region.of(awsRegion);

        // Create AWS Credentials object
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(awsAccessKey, awsSecretKey);

        // Create a Secrets Manager client
        SecretsManagerClient client = SecretsManagerClient.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .region(region)
                .build();

        GetSecretValueRequest secretValueRequest = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();

        GetSecretValueResponse secretValueResponse = client.getSecretValue(secretValueRequest);

        return secretValueResponse.secretString();

    }
}
