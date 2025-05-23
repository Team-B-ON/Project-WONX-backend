# Project-WONX-backend

# 실행방법
echo 'export PATH="/opt/homebrew/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc

export $(cat .env | xargs) && mvn spring-boot:run

# mysql 로컬 실행
mysql -u wonxuser -p
USE wonx;

exit

# .jar 파일 만드는 방법
# .env에 있는 모든 파일을 하드코딩 후 mvn clean package
