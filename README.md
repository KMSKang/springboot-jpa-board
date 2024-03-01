## 사용 기술
- Java 17
- Gradle 8.5
- Spring Boot 3.2.2
- Spring Security
- Spring Batch
- JPA, H2, MySQL
- Data JPA, QueryDSL
- Thymeleaf
- Redis
- Kafka
- Junit5
- MockMvc
---

## [!] 프로젝트 실행 전
<details>
    <summary>Edit Configurations -> Active profiles: local 설정</summary>

![1.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Freadme%2Factive_profiles%2F1.png)
![2.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Freadme%2Factive_profiles%2F2.png)

</details>

---
## [!] 테스트 코드 실행 전

<details>
    <summary>H2 실행</summary>

**1. 터미널**
```
cd [폴더 경로]/bin
./h2.sh
```
![1.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Freadme%2Fh2%2F1.png)

**2. 웹**
```
도메인 변경 -> localhost
```
![2.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Freadme%2Fh2%2F2.png)

---
```
JDBC URL 변경 -> jdbc:h2:~/board
```
![3.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Freadme%2Fh2%2F3.png)

---
```
board.mv.db 파일 생성 확인

cd ~
ls -al | grep ['문자열']
```
![4.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Freadme%2Fh2%2F4.png)

---
```
연결 버튼 클릭하여 접속
```
![5.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Freadme%2Fh2%2F5.png)
![6.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Freadme%2Fh2%2F6.png)
</details>
