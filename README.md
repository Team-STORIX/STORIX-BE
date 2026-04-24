# STORIX 1.0 Backend

> 2025.10 ~ 2026.01

웹툰 및 웹소설 독자들을 위한 플랫폼 STORIX의 백엔드 레포지토리입니다. <br/>
신촌 연합 IT 창업동아리 CEOS 22기의 Team-STORIX에서 개발하였습니다.


👀 베타 서비스 출시 3일 만에 1,000명 이상의 유저 확보 <br/>
🏆 CEOS 22기 데모데이 최우수상 · 베스트 프로덕트상 수상


<br>

## 🛠️ 팀원 소개
| <img src="https://github.com/caminobelllo.png" width="160px"> | <img src="https://github.com/Immmii.png" width="160px"> |
|:-------------------------------------------------------------:|:-----------------------------------------------------------:|
|    __서가영<br>[@caminobelllo](https://github.com/caminobelllo)__    |          __이수아<br>[@Immmii](https://github.com/Immmii)__          |
|    AWS 기반 클라우드 인프라 구성 <br/> 실시간 채팅(토픽룸)·취향 분석·작품 검색·크롤링    |          인증/인가·온보딩 <br> 피드·리뷰·서재·프로필        |


<br>
<br>

## 🛠️ Tech Stack
### Framework & Language
- Java 17 
- Spring Boot 3.5.7
- Gradle

### Authentication
- JWT 
- OAuth 2.0 Client : 소셜 로그인 기반 인증

### Storage
- AWS RDS (MySQL 8.0)
- Redis 
- AWS S3 : Presigned URL 방식 업로드 지원

### Deployment
- Docker : Amazon Corretto 17 Alpine 기반 멀티스테이지 빌드
- AWS ECR : Docker 이미지 레지스트리
- AWS EC2 : 운영 서버 (Self-hosted Runner 구성)

### Docs & Tools
- Swagger / OpenAPI 
- Lombok

<br>
<br>

## 📂 프로젝트 구조 
```
STORIX-BE/
  ├── .github/
  │   └── workflows/
  │       ├── ci.yml                        # STORIX CI - Build & Push to ECR
  │       └── cd.yml                        # STORIX CD - Deploy to EC2
  ├── Dockerfile
  ├── domains/
  │   ├── domain/
  │   │   ├── adaptor/
  │   │   ├── application/port, usecase/
  │   │   ├── controller/
  │   │   ├── domain/
  │   │   ├── dto/
  │   │   ├── repository/
  │   │   └── service/
  │   │
  └── global/
```

<br>
<br>

## 🌐 서버 인프라
Self-hosted Runner를 사용하여 EC2 인스턴스 내부에서 CI/CD를 진행합니다.

### 1️⃣ 아키텍처 다이어그램
<img width="5760" height="3240" alt="백엔드 (스프링) 기술 스택" src="https://github.com/user-attachments/assets/4011340b-184e-4b87-bd59-e34a58c4dded" />

### 2️⃣ CI/CD 구조
GitHub Actions를 이용해 CI/CD 파이프라인을 구축했습니다. <br>
AWS ECR과 Docker Compose를 사용합니다.

<br>

<img width="680" height="960" alt="ff6483b0-84d1-40b7-a019-392e2ff38571" src="https://github.com/user-attachments/assets/0eea87e0-59ce-4271-88b7-9a16a95b7e3b" />

<br>
<br>

## 📦 Build & Run
 
### 요구 사항
- JDK 17
- MySQL, Redis (로컬 또는 원격)
- `application.yml` 등에 DB / Redis / OAuth / JWT / AWS 자격 증명 설정 필요
### 로컬 실행
 
```bash
# 빌드
./gradlew clean build
 
# 테스트 제외 빌드
./gradlew build -x test
 
# 실행
./gradlew bootRun
```
 
### Docker 빌드 & 실행
 
프로젝트 루트의 `Dockerfile`은 멀티스테이지 빌드로 구성되어 있습니다.
 
```bash
# 이미지 빌드
docker build -t storix-api .
 
# 컨테이너 실행
docker run -d -p 8080:8080 --name storix-api storix-api
```
 
내부적으로 다음 단계를 거칩니다.
 
1. `amazoncorretto:17-alpine-jdk` 기반 builder 스테이지에서 `./gradlew build -x test --no-daemon` 실행
2. 동일한 런타임 이미지에 빌드된 JAR만 복사하여 경량 이미지로 구성
3. `TZ=Asia/Seoul`, `-Duser.timezone=Asia/Seoul` 옵션 적용
