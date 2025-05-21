package io.github.bon.wonx.domain.auth.mail;

import org.springframework.stereotype.Component;

@Component
public class MailContentBuilder {

    public String buildMagicLinkContent(String magicLink) {
        return """
            <html>
                <body>
                    <h2>원엑스에 오신 걸 환영합니다!</h2>
                    <p>아래 링크를 클릭하시면 로그인됩니다:</p>
                    <a href="%s">로그인하기</a>
                </body>
            </html>
        """.formatted(magicLink);
    }
}
