# 위시리스트 API 명세서

## 구현 목록

| URL                      | 메서드 | 기능       | 설명                |
|:-------------------------|:------|------------|-------------------|
| `/api/wishes`            | GET   | 위시리스트 전체 조회 | 사용자의 위시리스트 전체를 조회합니다. |
| `/api/wishes/{id}`       | GET   | 위시리스트 단건 조회 | 위시리스트의 특정 제품을 조회합니다. |
| `/api/wishes`            | POST  | 위시리스트 제품 추가 | 위시리스트에 새로운 제품을 추가합니다. |
| `/api/wishes/{id}`       | PUT   | 위시리스트 제품 수정 | 위시리스트의 특정 제품 수량을 수정합니다. |
| `/api/wishes/{id}`       | PATCH | 위시리스트 제품 부분 수정 | 위시리스트의 특정 제품 수량을 증가/감소시킵니다. |
| `/api/wishes/{id}`       | DELETE| 위시리스트 제품 삭제 | 위시리스트의 특정 제품을 삭제합니다. |
| `/api/wishes`            | DELETE| 위시리스트 전체 삭제 | 사용자의 위시리스트를 전체 삭제합니다. |

## 요청 및 응답 예시

### 1. 위시리스트 전체 조회

#### 요청

사용자의 위시리스트 전체를 조회합니다.

```http
GET /api/wishes HTTP/1.1
Authorization: Bearer {token}
```

#### 요청 파라미터

| 이름         | 기본값        | 설명                |
|:-------------|:-------------|-------------------|
| `page`       | `0`           | 조회할 페이지 번호 (0부터 시작) |
| `size`       | `5`           | 한 페이지에 표시할 제품 수      |

#### 응답

위시리스트 목록과 페이지 정보, 총 수량 및 총액이 포함됩니다.

##### 응답 예시
```json
{
  "page": 0,
  "size": 5,
  "totalElements": 3,
  "totalPages": 1,
  "totalQuantity": 10,
  "totalPrice": 15000,
  "contents": [
    {
      "id": 1,
      "name": "테스트 제품 1",
      "price": 1000,
      "imageUrl": "이미지 URL 1",
      "quantity": 3,
      "subtotal": 3000,
      "createdAt": "2024-01-01T00:00:00",
      "updatedAt": "2024-01-01T00:00:00"
    },
    {
      "id": 2,
      "name": "테스트 제품 2",
      "price": 2000,
      "imageUrl": "이미지 URL 2",
      "quantity": 5,
      "subtotal": 10000,
      "createdAt": "2024-01-01T00:00:00",
      "updatedAt": "2024-01-01T00:00:00"
    }
  ]
}
```

### 2. 위시리스트 단건 조회

#### 요청

위시리스트의 특정 제품을 조회합니다.

```http
GET /api/wishes/{id} HTTP/1.1
Authorization: Bearer {token}
```

#### 응답

특정 제품의 상세 정보가 포함된 응답을 반환합니다.

##### 응답 예시
```json
{
  "id": 1,
  "name": "테스트 제품 1",
  "price": 1000,
  "imageUrl": "이미지 URL 1",
  "quantity": 3,
  "subtotal": 3000,
  "createdAt": "2024-01-01T00:00:00",
  "updatedAt": "2024-01-01T00:00:00"
}
```

### 3. 위시리스트 제품 추가

#### 요청

위시리스트에 새로운 제품을 추가합니다.

```http
POST /api/wishes HTTP/1.1
Content-Type: application/json
Authorization: Bearer {token}

{
  "productId": 1,
  "quantity": 3
}
```

#### 요청 필드

| 이름         | 필수 | 설명                |
|:-------------|:-----|-------------------|
| `productId`  | Y    | 추가할 제품 ID       |
| `quantity`   | N    | 추가할 제품의 수량 (기본값: 1) |

#### 응답

추가된 제품의 정보가 포함된 응답을 반환합니다.

##### 응답 예시
```json
{
  "id": 1,
  "name": "테스트 제품 1",
  "price": 1000,
  "imageUrl": "이미지 URL 1",
  "quantity": 3,
  "subtotal": 3000,
  "createdAt": "2024-01-01T00:00:00",
  "updatedAt": "2024-01-01T00:00:00"
}
```

### 4. 위시리스트 제품 수정

#### 요청

위시리스트의 특정 제품 수량을 수정합니다.

```http
PUT /api/wishes/{id} HTTP/1.1
Content-Type: application/json
Authorization: Bearer {token}

{
  "quantity": 5
}
```

#### 요청 필드

| 이름         | 필수 | 설명                |
|:-------------|:-----|-------------------|
| `quantity`   | Y    | 수정할 제품의 수량    |

#### 응답

수정된 제품의 정보가 포함된 응답을 반환합니다.

##### 응답 예시
```json
{
  "id": 1,
  "name": "테스트 제품 1",
  "price": 1000,
  "imageUrl": "이미지 URL 1",
  "quantity": 5,
  "subtotal": 5000,
  "createdAt": "2024-01-01T00:00:00",
  "updatedAt": "2024-01-01T00:00:00"
}
```

### 5. 위시리스트 제품 부분 수정

#### 요청

위시리스트의 특정 제품 수량을 증가/감소시킵니다.

```http
PATCH /api/wishes/{id} HTTP/1.1
Content-Type: application/json
Authorization: Bearer {token}

{
  "quantity": 1,
  "increment": true
}
```

#### 요청 필드

| 이름         | 필수 | 설명                |
|:-------------|:-----|-------------------|
| `quantity`   | N    | 증가/감소시킬 수량 (기본값: 1) |
| `increment`  | N    | 증가시킬지 여부 (true: 증가, false: 감소, 기본값: true) |

#### 응답

수정된 제품의 정보가 포함된 응답을 반환합니다. 수량이 0이 되면 HTTP 상태 코드 204 No Content를 반환합니다.

##### 응답 예시 (수량 증가)
```json
{
  "id": 1,
  "name": "테스트 제품 1",
  "price": 1000,
  "imageUrl": "이미지 URL 1",
  "quantity": 2,
  "subtotal": 2000,
  "createdAt": "2024-01-01T00:00:00",
  "updatedAt": "2024-01-01T00:00:00"
}
```

##### 응답 예시 (수량 0으로 감소)
```
HTTP/1.1 204 No Content
```

### 6. 위시리스트 제품 삭제

#### 요청

위시리스트의 특정 제품을 삭제합니다.

```http
DELETE /api/wishes/{id} HTTP/1.1
Authorization: Bearer {token}
```

#### 응답

삭제가 성공적으로 완료되면 HTTP 상태 코드 204 No Content를 반환합니다.

### 7. 위시리스트 전체 삭제

#### 요청

사용자의 위시리스트를 전체 삭제합니다.

```http
DELETE /api/wishes HTTP/1.1
Authorization: Bearer {token}
```

#### 응답

삭제가 성공적으로 완료되면 HTTP 상태 코드 204 No Content를 반환합니다.

### 8. 에러 응답
에러가 발생할 경우, RFC 7807 Problem Details 형식의 응답을 반환합니다.

#### 에러 응답 예시
존재하지 않는 제품 ID로 요청 시의 예시입니다.

```json
{
  "type": "https://example.com/errors/product-not-found",
  "title": "제품을 찾을 수 없음",
  "status": 404,
  "detail": "Id 999에 해당하는 제품이 존재하지 않습니다.",
  "instance": "/api/wishes/999",
  "timestamp": "2024-01-01T00:00:00"
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
  "instance": "/api/wishes",
  "timestamp": "2024-01-01T00:00:00",
  "validationErrors": [
    {
      "field": "productId",
      "message": "제품 ID는 필수입니다."
    },
    {
      "field": "quantity",
      "message": "수량은 0보다 커야 합니다."
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
  "instance": "/api/wishes",
  "timestamp": "2024-01-01T00:00:00"
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
  "instance": "/api/wishes",
  "timestamp": "2024-01-01T00:00:00"
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