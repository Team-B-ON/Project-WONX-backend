# Project-WONX-backend

# 실행방법
echo 'export PATH="/opt/homebrew/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc

export $(cat .env | xargs) && mvn spring-boot:run

# mysql 로컬 실행
mysql -u wonxuser -p
USE wonx;

exit
