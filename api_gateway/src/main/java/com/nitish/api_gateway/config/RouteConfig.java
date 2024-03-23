package com.nitish.api_gateway.config;

import com.nitish.api_gateway.filter.RequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Value("${microservice.employee-service.name}")
    private String employeeServiceName;

    @Value("${microservice.task-service.name}")
    private String taskServiceName;

    @Value("${microservice.team-service.name}")
    private String teamServiceName;
    @Autowired
    private RequestFilter requestFilter;
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {

        return builder.routes()
                .route(employeeServiceName, r -> r.path("/login")
                        .uri("lb://" + employeeServiceName))
                .route(employeeServiceName, r -> r.path("/register")
                        .uri("lb://" + employeeServiceName))
                .route(taskServiceName, r -> r.path("/task/**")
                        .filters(f -> f.filter(requestFilter))
                        .uri("lb://" + taskServiceName))
                .route(teamServiceName, r -> r.path("/team/**")
                        .filters(f -> f.filter(requestFilter))
                        .uri("lb://" + teamServiceName))
                .build();
    }
}