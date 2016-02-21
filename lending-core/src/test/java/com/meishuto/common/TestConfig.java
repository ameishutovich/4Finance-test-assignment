package com.meishuto.common;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
* Test Configuration
*/
@Configuration
@ComponentScan(basePackages = "com.meishuto",
        excludeFilters = {@ComponentScan.Filter(Configuration.class)})
@EnableTransactionManagement
public class TestConfig {
}
