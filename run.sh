#!/bin/bash

# List of microservices
microservices=("eureka-server" "api_gateway" "employee_service" "task_service" "team_service" )

# Loop through each microservice
for microservice in "${microservices[@]}"
do
  echo "Starting $microservice"
  # Navigate to the microservice directory and start the microservice in a subshell
  (cd "$microservice" && mvn spring-boot:run &)
done