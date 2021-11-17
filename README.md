# spring-rest

Spring boot rest application with static data. 

## Postman
Use the following collection in order to test its requests:
[spring-rest-collection](https://www.postman.com/mschambeck/workspace/spring-rest/collection/488527-6e936915-d6db-44dc-ac3e-c30eedcbc415)

## JMeter Throughput

### Params

+ User Threads = 500
+ Loop Count = 3

### Results

| WebClient    | Throughput |
| ------------ | ----------:|
| Mono         |         90 |
| Flux         |         92 |
| RestTemplate |          4 |
