package hexlet.code;

import hexlet.code.domain.query.QUrl;
import hexlet.code.domain.Url;
import io.ebean.DB;
import io.ebean.Transaction;
import io.javalin.Javalin;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import static org.assertj.core.api.Assertions.assertThat;

class AppTest {

    private static Javalin app;
    private static String baseUrl;
    private static Transaction transaction;

    @BeforeAll
    public static void beforeAll() {
        app = App.getApp();
        app.start(0);
        int port = app.port();
        baseUrl = "http://localhost:" + port;
    }

    @AfterAll
    public static void afterAll() {
        app.stop();
    }

    @BeforeEach
    void beforeEach() {
        transaction = DB.beginTransaction();
    }

    @AfterEach
    void afterEach() {
        transaction.rollback();
    }

    @Test
    void testWelcome() {
        HttpResponse<String> response = Unirest.get(baseUrl).asString();
        assertThat(response.getStatus()).isEqualTo(200);

        String content = response.getBody();
        assertThat(content).contains("Анализатор страниц");
        assertThat(content).contains("Бесплатно проверяйте сайты на SEO пригодность");
        assertThat(content).contains("Nick Kisel");
    }

    @Test
    void testCreateValidUrl() {
        String name = "https://ru.wikipedia.org";
        HttpResponse<String> responsePost = Unirest
                .post(baseUrl + "/urls")
                .field("url", name)
                        .asEmpty();

        assertThat(responsePost.getStatus()).isEqualTo(302);
        assertThat(responsePost.getHeaders().getFirst("Location")).isEqualTo("/urls");

        Url actualUrl = new QUrl()
                .name.equalTo(name)
                .findOne();
        assertThat(actualUrl).isNotNull();
        assertThat(actualUrl.getName()).isEqualTo(name);

        HttpResponse<String> responseGet = Unirest
                .get(baseUrl + "/urls")
                .asString();

        String body = responseGet.getBody();

        assertThat(body).contains("Страница успешно добавлена");
    }

    @Test
    void testCreateInvalidUrl() {
        String inputUrl = "Qwerty123";
        HttpResponse<String> responsePost = Unirest
                .post(baseUrl + "/urls")
                .field("url", inputUrl)
                .asEmpty();

        assertThat(responsePost.getStatus()).isEqualTo(302);
        assertThat(responsePost.getHeaders().getFirst("Location")).isEqualTo("/");

        HttpResponse<String> response = Unirest
                .get(baseUrl + "/urls")
                .asString();
        String body = response.getBody();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(body).doesNotContain(inputUrl);
        assertThat(body).contains("Некорректный URL");

        Url actualUrl = new QUrl()
                .name.equalTo(inputUrl)
                .findOne();

        assertThat(actualUrl).isNull();
    }

    @Test
    void testCreateExistUrl() {
        String url1 = "https://ru.wikipedia.org";
        HttpResponse<String> responsePost1 = Unirest
                .post(baseUrl + "/urls")
                .field("url", url1)
                .asEmpty();
        assertThat(responsePost1.getStatus()).isEqualTo(302);

        Url checkUrl = new QUrl()
                .name.equalTo(url1)
                .findOne();

        assertThat(checkUrl.getName()).isNotNull();
        assertThat(checkUrl.getName()).isEqualTo(url1);

        HttpResponse<String> responsePost2 = Unirest
                .post(baseUrl + "/urls")
                .field("url", url1)
                .asEmpty();
        assertThat(responsePost2.getStatus()).isEqualTo(302);
        assertThat(responsePost2.getHeaders().getFirst("Location")).isEqualTo("/urls");

        HttpResponse<String> responseGet1 = Unirest
                .get(baseUrl + "/urls")
                .asString();

        String body = responseGet1.getBody();

        assertThat(body).contains("Страница уже существует");
    }

    @Test
    void testShowUrls() {
        HttpResponse<String> postResponse1 = Unirest
                .post(baseUrl + "/urls")
                .field("url", "https://ru.wikipedia.org")
                .asEmpty();
        HttpResponse<String> postResponse2 = Unirest
                .post(baseUrl + "/urls")
                .field("url", "https://ru.hexlet.io")
                .asEmpty();
        HttpResponse<String> response = Unirest.get(baseUrl + "/urls").asString();
        assertThat(response.getStatus()).isEqualTo(200);
        String content = response.getBody();
        assertThat(content).contains("https://ru.wikipedia.org");
        assertThat(content).contains("https://ru.hexlet.io");
    }
}
