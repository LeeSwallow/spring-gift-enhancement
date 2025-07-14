# 사용자 API 명세서

## 구현 목록

| URL                      | 메서드 | 기능       | 설명                |
|:-------------------------|:------|------------|-------------------|
| `/api/users`             | GET   | 사용자 목록 조회 | 모든 사용자의 정보를 조회합니다. (관리자 권한 필요) |
| `/api/users/{id}`        | GET   | 사용자 조회 | 특정 사용자의 정보를 조회합니다. (관리자 권한 필요) |
| `/api/users/me`          | GET   | 현재 사용자 조회 | 현재 사용자의 정보를 조회합니다. |
| `/api/users`             | POST  | 사용자 생성 | 새로운 사용자를 생성합니다. (관리자 권한 필요) |
| `/api/users/{id}`        | PUT   | 사용자 수정 | 특정 사용자의 정보를 수정합니다. (관리자 권한 필요) |
| `/api/users/me`          | PUT   | 현재 사용자 수정 | 현재 사용자의 정보를 수정합니다. |
| `/api/users/{id}`        | DELETE| 사용자 삭제 | 특정 사용자를 삭제합니다. (관리자 권한 필요) |
| `/api/users/me`          | DELETE| 현재 사용자 삭제 | 현재 사용자를 삭제합니다. |

## 요청 및 응답 예시

### 1. 사용자 목록 조회

#### 요청

모든 사용자의 정보를 조회합니다. **관리자 권한이 필요합니다.**

```http
GET /api/users HTTP/1.1
Authorization: Bearer {adminToken}
```

#### 요청 파라미터

| 이름         | 기본값        | 설명                |
|:-------------|:-------------|-------------------|
| `page`       | `0`           | 조회할 페이지 번호 (0부터 시작) |
| `size`       | `5`           | 한 페이지에 표시할 사용자 수      |

#### 응답

사용자 목록과 페이지 정보가 포함됩니다.

##### 응답 예시
```json
{
  "page": 0,
  "size": 5,
  "totalElements": 10,
  "totalPages": 2,
  "contents": [
    {
      "id": 1,
      "email": "user1@example.com",
      "password": "encodedPassword",
      "roles": ["ROLE_USER"]
    },
    // ... 나머지 사용자들 ...
  ]
}
```

### 2. 사용자 조회

#### 요청

특정 사용자의 정보를 조회합니다. **관리자 권한이 필요합니다.**

```http
GET /api/users/{id} HTTP/1.1
Authorization: Bearer {adminToken}
```

#### 응답

사용자의 상세 정보가 포함된 응답을 반환합니다.

##### 응답 예시
```json
{
  "id": 1,
  "email": "user1@example.com",
  "password": "encodedPassword",
  "roles": ["ROLE_USER"]
}
```

### 3. 현재 사용자 조회

#### 요청

현재 사용자의 정보를 조회합니다.

```http
GET /api/users/me HTTP/1.1
Authorization: Bearer {userToken}
```

#### 응답

현재 사용자의 정보가 포함된 응답을 반환합니다.

##### 응답 예시
```json
{
  "id": 1,
  "email": "user1@example.com"
}
```

### 4. 사용자 생성

#### 요청

새로운 사용자를 생성합니다. **관리자 권한이 필요합니다.**

```http
POST /api/users HTTP/1.1
Content-Type: application/json
Authorization: Bearer {adminToken}

{
  "email": "newuser@example.com",
  "password": "password123!",
  "role": "ROLE_USER"
}
```

#### 응답

생성된 사용자의 정보가 포함된 응답을 반환합니다.

##### 응답 예시
```json
{
  "id": 11,
  "email": "newuser@example.com",
  "password": "encodedPassword",
  "roles": ["ROLE_USER"]
}
```

### 5. 사용자 수정

#### 요청

특정 사용자의 정보를 수정합니다. **관리자 권한이 필요합니다.**

```http
PUT /api/users/{id} HTTP/1.1
Content-Type: application/json
Authorization: Bearer {adminToken}

{
  "email": "updated@example.com",
  "password": "newPassword123!",
  "roles": ["ROLE_USER"]
}
```

#### 응답

수정된 사용자의 정보가 포함된 응답을 반환합니다.

##### 응답 예시
```json
{
  "id": 1,
  "email": "updated@example.com",
  "password": "encodedPassword",
  "roles": ["ROLE_USER"]
}
```

### 6. 현재 사용자 수정

#### 요청

현재 사용자의 정보를 수정합니다.

```http
PUT /api/users/me HTTP/1.1
Content-Type: application/json
Authorization: Bearer {userToken}

{
  "password": "newPassword123!"
}
```

#### 응답

수정된 현재 사용자의 정보가 포함된 응답을 반환합니다.

##### 응답 예시
```json
{
  "id": 1,
  "email": "user1@example.com"
}
```

### 7. 사용자 삭제

#### 요청

특정 사용자를 삭제합니다. **관리자 권한이 필요합니다.**

```http
DELETE /api/users/{id} HTTP/1.1
Authorization: Bearer {adminToken}
```

#### 응답

삭제가 성공적으로 완료되면 HTTP 상태 코드 204 No Content를 반환합니다.

### 8. 현재 사용자 삭제

#### 요청

현재 사용자를 삭제합니다.

```http
DELETE /api/users/me HTTP/1.1
Authorization: Bearer {userToken}
```

#### 응답

삭제가 성공적으로 완료되면 HTTP 상태 코드 204 No Content를 반환합니다.

### 9. 에러 응답
에러가 발생할 경우, RFC 7807 Problem Details 형식의 응답을 반환합니다.

#### 에러 응답 예시
잘못된 사용자 ID로 요청 시의 예시입니다.

```json
{
  "type": "https://example.com/errors/user-not-found",
  "title": "사용자를 찾을 수 없음",
  "status": 404,
  "detail": "Id 1111에 해당하는 사용자가 존재하지 않습니다.",
  "instance": "/api/users/1111",
  "timestamp": "2025-06-27T13:12:47.7452837",
  "stackTrace": ["/*오류*/"]
}
```

#### 유효성 검사 실패 에러 응답 예시
잘못된 입력으로 인한 유효성 검사 실패 시의 예시입니다.

```json
{
  "type": "https://example.com/errors/validation-error",
  "title": "유효성 검사 실패",
  "status": 400,
  "detail": "입력 데이터가 유효하지 않습니다.",
  "instance": "/api/users",
  "timestamp": "2025-06-27T13:12:47.7452837",
  "validationErrors": [
    {
      "field": "email",
      "message": "이메일 형식이 올바르지 않습니다."
    },
    {
      "field": "password",
      "message": "비밀번호는 8자 이상이어야 합니다."
    }
  ]
}
```

#### 인증 실패 에러 응답 예시
인증 토큰이 없거나 유효하지 않을 때의 예시입니다.

```json
{
  "type": "https://example.com/errors/authentication-error",
  "title": "인증 실패",
  "status": 401,
  "detail": "유효한 인증 토큰이 필요합니다.",
  "instance": "/api/users",
  "timestamp": "2025-06-27T13:12:47.7452837"
}
```

#### 권한 부족 에러 응답 예시
권한이 부족한 사용자의 요청 시의 예시입니다.

```json
{
  "type": "https://example.com/errors/authorization-error",
  "title": "권한 부족",
  "status": 403,
  "detail": "이 작업을 수행할 권한이 없습니다.",
  "instance": "/api/users/123",
  "timestamp": "2025-06-27T13:12:47.7452837"
}
```

+ **필드 설명**
  + `type`: 에러를 해결할 수 있는 문서 주소
  + `title`: 에러 제목
  + `status`: HTTP 상태 코드
  + `detail`: 에러 상세 메시지
  + `instance`: 에러 인스턴스 ID (선택적)
  + `timestamp`: 에러가 발생한 시간
  + `stackTrace`: 스택 트레이스 (개발 환경에서만 사용, 선택적)
  + `validationErrors`: 유효성 검사 오류 목록 (선택적)
  + `validationErrors[].field`: 유효성 검사 오류 필드 (선택적)
  + `validationErrors[].message`: 유효성 검사 오류 메시지 (선택적)
