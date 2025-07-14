# 인증 API 명세서

## 구현 목록

| URL                      | 메서드 | 기능       | 설명                |
|:-------------------------|:------|------------|-------------------|
| `/api/auth/login`        | POST  | 로그인     | 사용자 로그인 처리 |
| `/api/auth/signup`       | POST  | 회원가입   | 새로운 사용자 등록 |

## 요청 및 응답 예시

### 1. 로그인

#### 요청

사용자 이메일과 비밀번호를 사용하여 로그인합니다.

```http
POST /api/auth/login HTTP/1.1
Content-Type: application/json

{
  "email": "user1@example.com",
  "password": "password123!"
}
```

#### 응답

로그인이 성공하면 인증 토큰을 반환합니다.

##### 응답 예시
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### 2. 회원가입

#### 요청

새로운 사용자를 등록합니다. 이메일, 비밀번호, 비밀번호 확인을 요청 본문에 포함시켜 POST 요청을 보냅니다.

```http
POST /api/auth/signup HTTP/1.1
Content-Type: application/json

{
  "email": "newuser@test.com",
  "password": "password123!",
  "passwordConfirm": "password123!"
}
```

#### 응답

회원가입이 성공하면 인증 토큰을 반환합니다.

##### 응답 예시
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### 3. 에러 응답
에러가 발생할 경우, 다음과 같은 형식의 응답을 반환합니다.

#### 에러 응답 예시
잘못된 이메일 또는 비밀번호로 로그인 시도 시의 예시입니다.

```json
{
  "timestamp": "2025-06-27T13:12:47.7452837",
  "message": "잘못된 이메일 또는 비밀번호입니다.",
  "status": 400,
  "error": "Bad Request",
  "path": "/api/auth/login",
  "stackTrace": "/*오류*/"
}
```
+ **필드 설명**
  + `timestamp`: 에러가 발생한 시간
  + `message`: 에러 메시지
  + `status`: HTTP 상태 코드
  + `error`: 에러 타입
  + `path`: 요청한 URL 경로
  + `stackTrace`: 에러가 발생한 코드의 스택 트레이스 (선택적, 디버깅 용도)
