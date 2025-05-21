package io.github.bon.wonx.domain.auth.mail;

import org.springframework.stereotype.Component;

@Component
public class MailContentBuilder {

    public String buildMagicLinkContent(String magicLink) {
        return """
            <html>
              <body style="margin: 0; padding: 0; background-color: #121212; font-family: 'Arial', sans-serif;">
                <div style="max-width: 600px; margin: auto; padding: 40px 30px; background-color: #1c1c1c; border-radius: 10px; color: #ffffff; text-align: center;">
                  
                  <h1 style="color: #e50914; font-size: 28px;">🍿 환영합니다, WONX에 오신 걸!</h1>
                  
                  <p style="font-size: 16px; margin: 20px 0;">
                    영화의 세계로 초대합니다!  
                    <br>아래 버튼을 눌러 지금 바로 로그인을 완료하고,  
                    <br><strong style="color:#e50914;">즐거운 시청</strong>을 시작해보세요.
                  </p>

                  <a href="%s"
                     style="display: inline-block; margin-top: 30px; padding: 14px 28px; background-color: #e50914; color: white; text-decoration: none; font-weight: bold; border-radius: 5px; font-size: 16px;">
                    🎬 지금 로그인하기
                  </a>

                  <p style="margin-top: 40px; font-size: 12px; color: #bbbbbb;">
                    만약 본인이 요청한 메일이 아니라면, 이 메일은 무시하셔도 괜찮아요!
                  </p>
                </div>
              </body>
            </html>
        """.formatted(magicLink);
    }
}
