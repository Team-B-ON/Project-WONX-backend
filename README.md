# 🎬 Wonx Backend

넷플릭스 스타일의 동영상 스트리밍 백엔드 서비스입니다.  
Magic Link 기반 이메일 인증 로그인과 JWT 토큰 인증, 구독 및 시청 기록 기능을 제공합니다.

---

## 📁 프로젝트 구조

```

src
├── main
│   ├── java/io/github/bon/wonx
│   │   ├── domain/
│   │   │   ├── auth/               # 이메일 인증, 로그인/회원가입
│   │   │   ├── user/               # 유저 도메인
│   │   │   ├── video/              # 동영상 관련 기능
│   │   │   └── subscription/       # 구독 관리
│   │   └── global/                 # 공통 설정 및 전역 처리
│   │       ├── config/             # Spring Security 등 설정
│   │       ├── jwt/                # JWT 필터, 프로바이더
│   │       ├── exception/          # 예외 처리
│   │       └── util/               # 유틸 클래스
│   └── resources/
│       └── application.properties

````

---

## 🛠️ 기술 스택

- **Java 21**
- **Spring Boot 3.4.5**
- **Spring Web** - REST API 개발
- **Spring Data JPA** - ORM 기반 DB 연동
- **H2 Database** - 개발/테스트용 인메모리 DB
- **MySQL** - 운영용 RDBMS
- **JWT (JJWT)** - 토큰 기반 인증 (Access/Refresh Token)
- **Lombok** - 보일러플레이트 코드 제거
- **Jakarta Validation** - DTO 유효성 검사
- **Maven** - 프로젝트 빌드 및 의존성 관리

---

## 🔐 인증 흐름 (Magic Link 기반)

1. 사용자가 이메일을 입력하면 인증 링크가 메일로 발송됩니다.
2. 사용자가 메일의 링크를 클릭하면 JWT가 발급되고 로그인 처리됩니다.
3. Access Token + Refresh Token을 함께 사용하여 인증을 유지합니다.

---

## 📦 주요 기능

- [x] 이메일 인증 기반 로그인 및 회원가입 (Magic Link)
- [x] JWT 기반 인증 및 인가
- [x] Refresh Token 기반 재발급
- [x] 로그아웃 처리
- [ ] 영상 스트리밍 및 접근 제어
- [ ] 이어보기, 좋아요, 북마크, 리뷰
- [ ] 구독 플랜 (체험판/유료)

---

## ✅ API 명세 (일부)

| 메서드 | URL | 설명 |
|--------|-----|------|
| POST   | `/api/auth/request-link` | 인증 메일 요청 |
| GET    | `/api/auth/callback`     | 인증 링크 접속 (로그인 처리) |
| POST   | `/api/auth/refresh`      | Access Token 재발급 |
| DELETE | `/api/auth/logout`       | 로그아웃 (Refresh Token 삭제) |
| GET    | `/api/videos/:id/stream` | 영상 스트리밍 (구독 필요) |

---

## 🧪 실행 방법

```bash
# 1. MySQL 실행 후 application.properties 설정
# 2. 빌드 및 실행
./mvnw spring-boot:run
````

```