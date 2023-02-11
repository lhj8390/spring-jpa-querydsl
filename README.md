# spring-jpa-querydsl
<img src="https://img.shields.io/badge/spring boot-6DB33F?style=flat&logo=spring boot&logoColor=white"/> <img src="https://img.shields.io/badge/JPA-6DB33F?style=flat&logo=spring&logoColor=white"/> <img src="https://img.shields.io/badge/QueryDSL-6DB33F?style=flat&logo=spring&logoColor=white"/> <img src="https://img.shields.io/badge/Junit-25A162?style=flat&logo=junit5&logoColor=white"/> <img src="https://img.shields.io/badge/H2-4479A1?style=flat&logo=h2&logoColor=white"/> <img src="https://img.shields.io/badge/gradle-02303A?style=flat&logo=gradle&logoColor=white"/>

스프링 부트에서 Spring Data JPA 와 QueryDSL을 활용하여 간단한 service를 구현해보았습니다.

### 기본 기능
- 데이터 조회 with JPA
- 검색 및 페이징 처리 with QueryDSL

### 개발 과정에서 고려한 사항
> 성능 최적화 및 간결한 코드 작성에 대해 고민했습니다.
- 페이징 처리 시 CountQuery 최적화에 대해 고민해보았습니다.
- BooleanExpression 을 사용하여 동적 쿼리를 작성하였습니다.
- 지연로딩 사용 후 N+1 문제를 해결하기 위해 fetch join을 활용해보았습니다.
- 같은 테이블에서 여러 데이터를 조회 시 where in 절로 성능 최적화를 해보았습니다. (default_batch_fetch_size 포함)

<br/>

### 기술 스택
Component         | Technology
---               | ---
Backend (REST)    | [SpringBoot](https://projects.spring.io/spring-boot) (Java)
In Memory DB      | H2 
Persistence       | JPA (Using Spring Data), QueryDSL
Test              | Junit5
Server Build Tools| Gradle
