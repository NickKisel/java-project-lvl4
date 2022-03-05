package hexlet.code;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AppTest {
    @Test
    void testApp() {
        assertThat(App.greetings()).isEqualTo("Hello world!");
    }
}
