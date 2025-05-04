# 🎬 wonx - 넷플릭스 클론 프로젝트

## 🔧 Tech Stack

- **Backend**: Java 17, Spring Boot, Maven
- **Auth**: JWT (Access / Refresh), Magic Link 기반 이메일 로그인
- **Media**: 동영상 스트리밍 구현 (S3 또는 파일 응답)
- **Database**: MySQL
- **Infra**: AWS, Postman 테스트 기반 API 설계

## ✅ 주요 기능 (담당 파트 기준)

### 🔐 인증/인가

- Magic Link 이메일 로그인/회원가입
- JWT Access / Refresh Token 발급 및 검증
- 구독 여부 체크 및 스트리밍 접근 제어
- 로그아웃 시 Refresh Token 무효화

### 📺 동영상 스트리밍

- 구독 상태 확인 후 스트리밍 허용
- Content-Type 대응 및 파일 범위 처리

## 📂 디렉토리 구조 (예시)

src/
└── main/
    ├── java/
    │   └── com.bon.wonx/
    │       ├── auth/
    │       ├── video/
    │       └── user/
    └── resources/
        └── application.yml


## 💡 실행 방법

bash
./mvnw spring-boot:run