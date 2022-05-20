package com.example;

import org.junit.jupiter.api.Test;
import org.mono.stacksaga.autoconfigure.property.Properties;
import org.mono.stacksaga.common.comiunication.ServiceReleaseVersion;
import org.mono.stacksaga.db.service.ServiceReleaseVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Assertions;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ServiceReleaseVersionServiceTest {
    @Autowired
    private ServiceReleaseVersionService serviceReleaseVersionService;

    @Autowired
    private Properties properties;

    @Value("${spring.application.name}")
    private String applicationName;


    @Test
    void searchByVersionPrefixTest() {

        List<ServiceReleaseVersion> serviceReleaseVersions =
                serviceReleaseVersionService.searchByVersionPrefix(
                        properties.getAppReleaseVersion(),
                        applicationName
                );
        Assertions.assertTrue(serviceReleaseVersions.size() > 0);
    }

}
