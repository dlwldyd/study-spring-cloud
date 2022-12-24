# eureka
* [넷플릭스 유레카 yml 파일 설정](./discoveryservice/src/main/resources/application.yml)
# api gateway
* [API Gateway yml 파일 설정(필터 설정 포함)](./apigateway-service/src/main/resources/application.yml)
* [API Gateway java 파일 설정(필터 설정)](./apigateway-service/src/main/java/com/example/apigatewayservice/config/FilterConfig.java)
  - 필터 설정 시 yml 파일과 java 파일 설정이 둘 다 필요한게 아니라 둘 중 하나만 적용하면 된다. 편한거 쓰면 됨
* [API Gateway CustomFilter](./apigateway-service/src/main/java/com/example/apigatewayservice/filter/CustomFilter.java)
* [API Gateway GlobalFilter](./apigateway-service/src/main/java/com/example/apigatewayservice/filter/GlobalFilter.java)
* [API Gateway LoggingFilter](./apigateway-service/src/main/java/com/example/apigatewayservice/filter/LoggingFilter.java)
  - 필터에 종류가 있는게 아님
  - CustomFilter는 특정 서비스에만 적용되도록 설정, GlobalFilter는 모든 서비스에 대해 적용되도록 설정, LoggingFilter는 해당 필터가 로깅 기능을 함
* [Http 요청의 헤더값 지워서 마이크로서비스로 보내기](./msa/apigateway-service/src/main/resources/application.yml)
* [url 경로 변경해서 마이크로서비스로 보내기](./msa/apigateway-service/src/main/resources/application.yml)
* API Gateway 에서 JWT 인증 처리
  - [필터](./msa/apigateway-service/src/main/java/com/example/apigatewayservice/filter/AuthorizationHeaderFilter.java)
  - [필터 등록](./msa/apigateway-service/src/main/resources/application.yml)
# 마이크로서비스
* [등록 서비스 yml 파일 설정](./userservice/src/main/resources/application.yml)
# 잡다
* [Context Path, url prefix 전역적으로 설정](./msa/userservice/src/main/resources/application.yml)
* [ModelMapper](./msa/userservice/src/main/java/com/example/userservice/controller/UserController.java)
* [Environment, 환경정보 가져오기(포트번호 등)](./msa/userservice/src/main/java/com/example/userservice/controller/UserController.java)
* [@JsonInclude](./msa/userservice/src/main/java/com/example/userservice/domain/vo/ResponseUser.java)