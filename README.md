[프로젝트]
1. 언어: Java
3. Java 버전: 17
3. 프레임워크: Spring Boot
4. 스프링 버전: 3.2.2
---
[프로젝트 실행 시 주의사항]
1. Edit Configurations -> Active profiles: local 설정
---
[테스트 코드 실행 시 주의사항]
1. H2 실행
---
[H2]
1. 터미널
cd h2/bin
./h2.sh

2. 웹 페이지
JDBC URL: jdbc:h2:~/board
연결 버튼 클릭
연결 끊기 버튼 클릭

3. 터미널 새로운 창
cd ~
board.mv.db 파일 생성 확인

4. 웹 페이지
JDBC URL: jdbc:h2:tcp://localhost/~/datajpa
연결 버튼 클릭
---