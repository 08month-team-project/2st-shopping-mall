# 2st-shopping-mall-BE



## Introduction

__FE와 AWS를 연동해 웹 서버를 배포한 프로젝트__
- **진행기간**:  2024.09.30 ~ 2024.10.11
- **진행 방식**
  - Notion 가이드 요구사항 및 기능 확인
  - 기능별 역할 분배
  - API 명세서 작성
  - ERD 설계
  - 프로젝트 기본 세팅
  - 기능 구현 및 테스트
  - Github PR 및 팀원 Review 후 Merge
  - AWS EC2를 통해 웹 서버 배포
  - 배포후 발생한 Bug Fix

- **프로젝트 전체 방향성**
  - FE팀과 소통할 인터페이스를 협의하고 정의하는 역량 향상
  - BE팀 내 적극적인 소통을 통해 적절한 업무 분배 역량 향상
  - AWS를 통해 서버 배포 경험
  

## Feature


### 🌐 회원
- #### 회원가입
  - ###### 이메일 중복 확인

- #### 로그인
  
- #### 로그아웃
 
- #### 판매자로 역할 변경

- #### 회원탈퇴
  
- #### 내 정보 조회

- #### 내 정보 수정
   - ###### 프로필 이미지 수정

### 🌐 물품
- #### 물품 등록
   - ###### 이미지 업로드
   - ###### 사이즈 조회
   - ###### 카테고리 조회

- #### 목록 조회
   - ###### 카테고리
   - ###### 이름
   - ###### 상태
   - ###### 정렬
   - ###### 페이지 번호

- #### 상세 조회
   - ###### 이미지 조회
   - ###### 상세정보 조회

- #### 물품 수정
   - ###### 수량 변경

### 🌐 장바구니
- #### 물품 담기

- #### 등록된 물품 조회

- #### 등록된 물품 수정

- #### 등록된 물품 삭제

### 🌐 주문
- #### 물품 주문

## Stack


<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">

[//]: # (스프링 관련)
<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white">
<img src="https://img.shields.io/badge/JSON Web Tokens-000000?style=for-the-badge&logo=JSON Web Tokens&logoColor=white">

<img src="https://img.shields.io/badge/Spring data jpa-6DB33F?style=for-the-badge&logo=Spring&logoColor=white">

<img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">

[//]: # (데이터베이스 관련)
<img src="https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white">
<img src="https://img.shields.io/badge/Redis-FF4438?style=for-the-badge&logo=Redis&logoColor=white">


[//]: # (깃 관련)
<img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
<img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">

[//]: # (노션)
<img src="https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=Notion&logoColor=white">

[//]: # (테스트 관련)
<img src="https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=Postman&logoColor=white">

[//]: # (인텔리제이)
<img src="https://img.shields.io/badge/IntelliJ IDEA-000000?style=for-the-badge&logo=IntelliJ IDEA&logoColor=white">

[//]: # (호스팅)
<img src="https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white">
<img src="https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white">

## Architecture


### Package structure


```
├─main
│  ├─java
│  │  └─com
│  │      └─example
│  │          └─shoppingmall
│  │              ├─domain
│  │              │  ├─cart
│  │              │  │  ├─api
│  │              │  │  ├─application
│  │              │  │  ├─dao
│  │              │  │  ├─domain
│  │              │  │  ├─dto
│  │              │  │  └─excepction
│  │              │  ├─common
│  │              │  ├─item
│  │              │  │  ├─api
│  │              │  │  ├─application
│  │              │  │  ├─dao
│  │              │  │  ├─domain
│  │              │  │  ├─dto
│  │              │  │  ├─excepction
│  │              │  │  └─type
│  │              │  ├─order
│  │              │  │  ├─api
│  │              │  │  ├─application
│  │              │  │  ├─dao
│  │              │  │  ├─domain
│  │              │  │  ├─dto
│  │              │  │  ├─excepction
│  │              │  │  └─type
│  │              │  └─user
│  │              │      ├─api
│  │              │      ├─application
│  │              │      ├─dao
│  │              │      ├─domain
│  │              │      ├─dto
│  │              │      ├─excepction
│  │              │      └─type
│  │              ├─global
│  │              │  ├─annotation
│  │              │  ├─config
│  │              │  ├─exception
│  │              │  ├─lock
│  │              │  └─security
│  │              │      ├─detail
│  │              │      ├─dto
│  │              │      ├─filter
│  │              │      └─util
│  │              └─test

```


## Erd

![](2st-shopping-mall-erd.png)


## Member



| 이름  | 역할         | Github |
|-----|------------|---|
| 이수연 | 회원 인증 | https://github.com/lsy28901 |
| 유도경 | 물품 조회/구매 | https://github.com/DokyungYou |
| 유종화 | 물품 등록/판매 | https://github.com/YOOJONGHWA |
| 임승진 | 서버 배포 (AWS) | https://github.com/TestSeung |


