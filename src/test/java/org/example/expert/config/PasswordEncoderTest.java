package org.example.expert.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
class PasswordEncoderTest {

    @InjectMocks
    private PasswordEncoder passwordEncoder;

    @Test
    void matches_메서드가_정상적으로_동작한다() {
        // given
        String rawPassword = "testPassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        /**
         * when
         * 처음에 코드를 잘 못 작성했겟다라고 생각하고 어디가 잘 못되었을까 많이 고민하였다.
         * 아무리 생각해도 테스트 코드 만 놓고 보면 문제 없었는데 다시 한 참 고민을 했었다.
         * 그래서 도저히 답을 찾을 수 없어서 PasswordEncoder 클래스에 가서 메서드를 확인하였다.
         * mateches메서드 매개변수 rawPassword, encodedPassword의 순서가 테스트 코드와 다르다는 것을 알았고
         * 테스트 코드를 수정하였더니 정상적으로 테스트에 통과하였다.
          */
        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);

        // then
        assertTrue(matches);
    }
}
