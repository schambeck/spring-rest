# spring-webflux

Spring Boot REST server application using Reactive WebFlux non-blocking approach with H2 in-memory database.

## Postman
Use the following collection in order to test its requests:
[spring-webflux-collection](https://www.postman.com/mschambeck/workspace/spring-webflux/collection/488527-6e936915-d6db-44dc-ac3e-c30eedcbc415)

## JMeter Throughput

### Params

+ Request delay = 1 sec
+ User Threads = 500

### Results

| Server  | Throughput |
| ------- | ----------:|
| WebFlux |        450 |
| WebMvc  |        200 |

#### Chart

![alt text](https://i.ibb.co/MZqgFB8/column-chart.png)
