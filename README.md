# spring-gift-enhancement

## 구현 기능 목록
### 0 단계 : 프로젝트 `spring-gift-enhancement`로 이관
+[X] 모든 코드 이관



## 이전 단계 요약

### 1 주차

+ [X] 0 단계 : 상품 API 구현
+ [X] 1 단계 : 관리자 화면 구현
+ [X] 2 단계 : 데이터베이스 적용

### 2 주차
+ [X] 0 단계 : 프로젝트 `spring-gift-wishlist`으로 이관
+ [X] 1 단계 : 상품 유효성 검사 및 예외 처리 기능 추가
+ [X] 2 단계 : 회원 로그인 기능 추가
+ [X] 3 단계 : 위시리스트 구현 완료

## API 명세서
- 
- [사용자 API 명세서.md](document/%EC%82%AC%EC%9A%A9%EC%9E%90%20API%20%EB%AA%85%EC%84%B8%EC%84%9C.md)
- [상품 조회 API 명세서.md](document/%EC%83%81%ED%92%88%20%EC%A1%B0%ED%9A%8C%20API%20%EB%AA%85%EC%84%B8%EC%84%9C.md)
- [인증 API 명세서.md](document/%EC%9D%B8%EC%A6%9D%20API%20%EB%AA%85%EC%84%B8%EC%84%9C.md)

## 커밋 컨벤션

| type     | meaning      |
|----------|--------------|
| feat     | 새로운 기능 추가    |
| fix      | 오류, 오타 수정    |
| docs     | 문서 생성, 수정    |
| style    | format 변경    |
| refactor | 리팩토링         |
| test     | 테스트 코드 추가/수정 |
| chore    | 유지보수 작업      |

### 커밋 메시지 작성 규칙(AngularJS 컨벤션 기반) 

```md
<type>(<scope>): <subject>
// blank line 필수!
<body>
// (footer 입력시)blank line 필수!
<footer>
```
> + 명령형, 소문자 시작, 마침표 없이 작성
> + 필요시 body, footer(이슈번호, breaking change 등) 추가