# UI 구현하면 -> 쇼핑몰 완성 ***

# 프로젝트 구성 
<img width="700" alt="image" src="https://github.com/user-attachments/assets/1f1d631e-959d-4fa8-8213-7bbfbd6d08dc">

- RESTful API 서버 구현스프링 부트와 JPA를 이용하는 API 서버 만들기 
- SpringBoot : @RestController에서 모든 응답처리, @RestControllerAdvice에서 모든 예외처리
- SpringDataJPA : 목록데이터는 Querydsl로 설계하고, Projections를 이용해서 DTO로 추출한다.
- SpringSecurity : JWT의 AccessToken과 RefreshToken, 동시에 SecurityContext 활용
  


## REST API 
<img width="700" alt="image" src="https://github.com/user-attachments/assets/5502bdde-a36e-4ed3-9a8d-fbdea00cbb8f">

- 기존 브라우저는 단순한 뷰어 역할만을 수행, 반면 Web 2.0부터 프로그램이 실행되는 하나의 플랫폼으로 자리잡음
    * 반응형 웹은 CSS 기법, 모바일 앱 등은 데이터만을 요구 => 컴퓨터 일편적인 HTML View ( X )
    * Android app, IOS app, Browser, Applications, etc 등에게 동일한 데이터를 보내는게 API서버
- 서버에서 완성본을 보내주는 방식에서 -> 순수한 데이터를 보내주는 방식으로 변화 (배달음식->밀키트)
- REST 방식 혹은 Restfull 은 Http/Htpps를 이용해서 원하는 데이터를 제공하는 방식에 사용하는 구조 의미
    * 이 REST는 정해진 스펙이 존재하지 않음, 단지 조건이나 가이드만 존재  

## 쿼리스트링 (Query String) 
- @RequestParam(?로 시작하는 쿼리스트링) vs @PathVariable(고정된 주소)
- https://github.com/adorahelen?tab=overview&from=2024-08-01&to=2024-08-23 
- https://career.programmers.co.kr/competitions (고유한 식별자 사용) 
- 페이지 번호와 같이, 오늘 1페이지의 내용이 시간이 지나면 변경될 수 있기에, 식별자X -> 쿼리스트링O
    * 매번 동일한 데이터를 나타내지 않는다면 쿼리스트링으로 처리된다.


## JPA : Java Persistence API
- JPA란? : 자바 언어에서 지정한 객체의 '영속성 관리'에 대한 스펙(규약,정의)
- 영속성 관리 : 객체지향 구조를 관계형 데이터베이스에 매핑해서 관리하는 방식을 의미
- 정리 : ORM(Object-Relation-Mapping), 하나의 객체가 하나의 레코드가 되도록 관리하는데 이를 '매핑'이라 한다.
- 결론 : ORM은 하나의 페러다임, 각 프로그래밍 언어들은 각자 스펙을 결정, JPA는 JAVA에서 택한 ORM 스펙을 의미.
- JPA가 스펙이라면, JPA에 맞게 구현한 다양한 제품이나 라이브러리가 존재한다.
- 스프링 부트는 'Hibernate'를 선택

<img width="700" alt="image" src="https://github.com/user-attachments/assets/d3fc18cd-915a-444f-869b-86113109233b">
<img width="700" alt="image" src="https://github.com/user-attachments/assets/a522d351-4fad-4622-9e9f-be310075363e">

- Hibernate를 이용하는 경우 엔티티 객체를 관리하는 여러 API,트랜잭션,예외처리 등에 신경을 써야한다.
    * 엔티티 매니저(엔티티 객체(영속상태-준영속상태-비영속상태))
    * 엔티티의 상태를 직접 관리하고, 필요한 시점에 커밋을 해야 하는 등, 개발자가 신경 써야 할 필요한 부분이 많다.
- Spring Data를 사용해서 더 간단한 방식으로 개발 할 수 있다. 
    * 개발자가 직접 코드를 작성하지 않아도 CRUD, 페이징 처리 등을 지원함
    * SpringData는 비즈니스 로직에 더 집중할 수 있게 데이터베이스 사용 기능을 클래스 레벨에서 추상화
    * 스프링 데이터에서 제공하는 인터페이스를 통해(리포지토리라는 이름을 가지는 인터페이스), 스프링 데이터를 사용
    * 표준 스펙인 JPA는 스프링에서 구현한"Spring Data JPA"사용 (if몽고디비는springDataMongoDB사용)
- Spring Data JPA란?
    * 스프링 데이터의 공통적인 기능에서 JPA의 유용한 기술이 추가된 기술
- 요약
    * ORM : 객체와 데이터베이스를 연결하는 프로그래밍 기법,패러다임 (자바, 파이썬 다 가능)
    * JPA : 자바에서 정의한 관계형 데이터베이스를 사용하는 방식
    * Hibernate : JPA에 따라 만든 대표적인 라이브러리,프레임워크
    *  SpringDataJPA : JPA를 더욱 쓰기 편하게 마들어 놓은 모듈(리포지토리 인터페이스-상속을 통해 이용)
- DTO
    * 이처럼 JPA에서 엔티티 객체들은 영속 컨텍스트가 관리하고 있기 때문에, 직접 접근하는 것을 지양한다.
    * 따라서 DTO(엔티티 데이터를 복사해서 가지고 있는 객체) 등으로 변환하여 사용(= 읽기/쓰기 가능)
 
## 페이징, Querydsl
- 페이징 방식
    * findAll() : 모든 데이터를 조회, 메서드의 파라미터로 Pageable 타입 지정 가능-> 자동 페이징 처리
    * @Query : JPQL이라는 쿼리언어로 작성, SQl과 유사한 형식, 어떠한 DB든 동일하게 동작(종속 X)
    * Querydsl / JQQQ 라이브러리
- @Query : 특정한 속성(칼럼)들만을 조회, where 조건절의 사용
    * 쿼리 메서드 : 메서드의 이름 자체가 쿼리가 되는 기능 (findBy + where = findByTitleLike)
- Querydsl : @Query를 이용해서 기존의 SQl 기능들을 어느 정도 사용할 수 있지만, 이미 고정된 것들이기에
    * 상황에 따라 다른 JPQL을 만들어내기 위해서 사용한다. (JQQQ || Querydsl)
      
<img width="700" alt="image" src="https://github.com/user-attachments/assets/0f41c358-a85a-422e-834c-4a809215f34e">

- 필요한 라이브러리들을 추가하면, build폴더 내부에 Q로 시작하는 파일이 생성된다. (QTodo)
    * 개발자가 직접 수정하지 않는 파일(빌드), Q로 시작하는 클래스를 Q도메인 클래스라고 한다.
    * Q 도메인 클래스들은 동적으로 JPQL을 만들기 위해서 사용된다. 

## 예외처리, AOP 
- AOP : 공통 관심사, 공통적이고 반복적인 문제에 대한 코드를 기존 코드와 결합(weaving)해서 사용
    * Advice - 공통적인 문제를 처리하는 객체
    * @ExceptionHandler 메서드 : ResponseEntity를 통해 사용자에게 에러 메시지와 상태 코드를 반환
- NoSuchElement Exception : Controller에서 바라보면 어디가 문제인지 찾을 수 없음
    * if 엔티티가 없어서 발생, 다음과 같이 처리 .orElseThrow(()->new EntityNotFoundException)
    * 서비스 단에서, 테스트를 진행하고 위와같이 예외처리를 하나하나 지정해줘야 함 (다른 문제면, 다른 예외로)
- MethodArgumentNotValidException : 검증에 실패한 경우 발생 
- EntityNotFoundException : 엔티티를 찾을 수 없음
- MethodArgumentTypeMismatchException : 설정값과 맞지 않는 URL
- NoResourceFoundException : 뷰 파일을 찾을 수 없음

## 스웨거(API_DOCS)
- 의존성 추가 implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0'
- @OpenAPIDefinition : info=@Info (title = "REST API",version = "ver 0.1",description = "RESTful API Documentation")
    * servers = { @Server(description = "Prod ENV", url = "http://localhost:8080/")
  

## 토큰발급(http://localhost:8080/api/v1/token/make)
<img width="700" alt="image" src="https://github.com/user-attachments/assets/561fa055-719d-4fa0-b9aa-fe5572725db4">
<img width="700" alt="image" src="https://github.com/user-attachments/assets/42cbaae8-73fc-40fb-8fd2-4bc33d46232c">
<img width="700" alt="image" src="https://github.com/user-attachments/assets/34ad7717-45f3-4633-9647-0c6c1175ca45">


<img width="700" alt="image" src="https://github.com/user-attachments/assets/41541bd8-7521-42d8-8b8f-dd26fdd57ed4">

## 비동기처리(AJAX)
<img width="700" alt="image" src="https://github.com/user-attachments/assets/1ff2827b-2826-464a-aa6e-23ac6aac28b9">
<img width="700" alt="image" src="https://github.com/user-attachments/assets/5fc8ccd4-0b4f-429d-8300-80a044e9e44e">


## CORS 처리 
<img width="700" alt="image" src="https://github.com/user-attachments/assets/956b91bb-e87d-4f05-9314-d9d393768f9e">
<img width="700" alt="image" src="https://github.com/user-attachments/assets/03de5618-497e-4534-b638-c11bb08a3bf9">
<img width="700" alt="image" src="https://github.com/user-attachments/assets/6be00ead-5319-47f6-a59f-593f4f039f9d">




## 파일업로드

<img width="700" alt="image" src="https://github.com/user-attachments/assets/5d7a2a49-dd74-4772-95dc-aceba2f1b99f">
<img width="700" alt="image" src="https://github.com/user-attachments/assets/13632520-fa05-4613-a44a-75362244c6ca">

- 파일을 서버에 업로드 하고, 파일 이름만 디비에 넣는 경우가 있고
- 파일을 디비에 통째로 올리는 경우도 있다. -> 선택하기 나름 파일 관리를 어떻게 할지 

<img width="700" alt="image" src="https://github.com/user-attachments/assets/7f55027c-2c89-42b2-8b86-ae918ceb02d4">
<img width="700" alt="image" src="https://github.com/user-attachments/assets/6cd66d6c-3580-489f-b2e1-dad289a19472">
<img width="700" alt="image" src="https://github.com/user-attachments/assets/e31cc733-f425-4076-9496-a981aa78a5d2">

- 7.5.2 등록처리
  
<img width="700" alt="image" src="https://github.com/user-attachments/assets/9abe8dc6-b031-4885-aeec-f9637bf2ae90">


## JPA 심화
- @ElementCollection
- @Collection
- @Embedable

<img width="700" alt="image" src="https://github.com/user-attachments/assets/78fa43a2-59a0-47fd-bbe2-3f29d7cbc0f5">
<img width="700" alt="image" src="https://github.com/user-attachments/assets/921786de-3235-4bb4-a64e-263207cd6e12">
<img width="873" alt="image" src="https://github.com/user-attachments/assets/f8f623db-2bd1-46aa-9be3-ab368b09bb28">

## ManyToOne 
<img width="700" alt="image" src="https://github.com/user-attachments/assets/acacfe71-7dfe-4712-8f6c-83782a5896cf">

- tbl_product_image는 키 없이 종속, tbl_review를 키로 매핑, 아래와 같이 자동 생성
  
<img width="700" alt="image" src="https://github.com/user-attachments/assets/3e8341c7-cb8f-4367-a7b9-ddf0086d7c6a">


## ManyToMany
<img width="700" alt="image" src="https://github.com/user-attachments/assets/6d002ea3-f1fb-4eca-be02-9a12030889e3">
<img width="700" alt="image" src="https://github.com/user-attachments/assets/f9ff6565-9467-4a98-a6d8-5d283a0ad5f2">

- 현재 작성된 엔티티들은 Cart와 CartItem, Product 간의 관계를 설정
- Cart와 Product 간의 다대다 관계를 CartItem이라는 중간 엔티티를 통해 1대다 및 다대1 관계로 풀어낸 것

<img width="700" alt="image" src="https://github.com/user-attachments/assets/b567b622-f4d8-4b16-b01f-b9500133e7d1">





