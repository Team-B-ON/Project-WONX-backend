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

# s3 관련 프론트에서 가져오는 방법
<video controls autoplay>
  <source src="https://{s3-presigned-url}" type="video/mp4" />
</video>
