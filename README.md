# study_spring_cloud
* [넷플릭스 유레카 yml 파일 설정](./discoveryservice/src/main/resources/application.yml)
* [API Gateway yml 파일 설정(필터 설정 포함)](./apigateway-service/src/main/resources/application.yml)
* [API Gateway java 파일 설정(필터 설정)](./apigateway-service/src/main/java/com/example/apigatewayservice/config/FilterConfig.java)
  - 필터 설정 시 yml 파일과 java 파일 설정이 둘 다 필요한게 아니라 둘 중 하나만 적용하면 된다. 편한거 쓰면 됨
* [API Gateway CustomFilter](./apigateway-service/src/main/java/com/example/apigatewayservice/filter/CustomFilter.java)
* [API Gateway GlobalFilter](./apigateway-service/src/main/java/com/example/apigatewayservice/filter/GlobalFilter.java)
* [API Gateway LoggingFilter](./apigateway-service/src/main/java/com/example/apigatewayservice/filter/LoggingFilter.java)
  - 필터에 종류가 있는게 아님
  - CustomFilter는 특정 서비스에만 적용되도록 설정, GlobalFilter는 모든 서비스에 대해 적용되도록 설정, LoggingFilter는 해당 필터가 로깅 기능을 함
* [등록 서비스 yml 파일 설정](./userservice/src/main/resources/application.yml)