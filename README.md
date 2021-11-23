# spring-rest

Spring Boot REST server application using Reactive WebFlux (Mono and Flux types) and traditional blocking approach with H2 in-memory database.

## Postman
Use the following collection in order to test its requests:
[spring-rest-collection](https://www.postman.com/mschambeck/workspace/spring-rest/collection/488527-6e936915-d6db-44dc-ac3e-c30eedcbc415)

## JMeter Throughput

### Params

+ Request delay = 1 sec
+ User Threads = 500
+ Loop Count = 10

### Results

| WebClient    | Throughput |
| ------------ | ----------:|
| Mono         |        450 |
| Flux         |        450 |
| RestTemplate |        200 |

#### Chart

![alt text](https://i.ibb.co/MZqgFB8/column-chart.png)
