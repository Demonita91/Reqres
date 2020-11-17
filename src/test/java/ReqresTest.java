import models.User;
import org.apache.http.protocol.HTTP;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;

public class ReqresTest {
    String URL = "https://reqres.in";

    @Test
    public void getListUsersTest() {

        given()
        .when()
                .get(URL + "/api/users?page=2")
        .then()
                .log().body()
                .statusCode(200)
                .body("total", equalTo(12));
    }
    @Test
    public void getSingleUserTest() {
        given()
        .when()
                .get(URL + "/api/users/2")
        .then()
                .log().body()
                .statusCode(200)
                .body("ad.company", equalTo("Slack"));
    }
    @Test
    public void getSingleUserNotFoundTest() {
        given()
        .when()
                .get(URL + "/api/users/23")
        .then()
                .log().body()
                .statusCode(404);
            }
    @Test
    public void getListResourceTest() {
        given()
        .when()
                .get(URL + "/api/unknown")
        .then()
                .log().body()
                .statusCode(200)
                .body("total", equalTo(12))
                .body("data[0].name", equalTo("cerulean"));
    }
    @Test
    public void getSingleResourceTest() {
        given()
        .when()
                .get(URL + "/api/unknown/2")
        .then()
                .log().body()
                .statusCode(200)
                .body("ad.company", equalTo("Squarespace"));
    }

    @Test
    public void getSingleResourceNotFoundTest() {
        given()
        .when()
                .get(URL + "/api/unknown/23")
        .then()
                .log().body()
                .statusCode(404);
    }
    @Test
    public void postCreateTest() {
        User user = User.builder()
                .job("QA")
                .name("Dmitry")
                .build();
        given()
                .body(user)
                .log().all()
        .when()
                .post(URL + "/api/users")
        .then()
                .log().all()
                .statusCode(201)
                .body("$", hasKey("id"));
    }
    @Test
    public void putUpdateTest() {
        User user = User.builder()
                .name("morpheus")
                .job("zion resident")
                .build();
        given()
                .body(user)
                .log().all()
        .when()
                .put(URL + "/api/users/2")
        .then()
                .log().body()
                .statusCode(200);
    }

    @Test
    public void patchUpdatePatchTest() {
        User user = User.builder()
                .name("morpheus")
                .job("zion resident")
                .build();
        given()
                .body(user)
                .log().all()
        .when()
                .patch(URL + "/api/users/2")
        .then()
                .log().body()
                .statusCode(200);
    }

    @Test
    public void deleteTest() {

        given()
        .when()
                .delete(URL + "/api/register")
        .then()
                .log().all()
                .statusCode(204);
    }
    @Test
    public void postRegisterSuccessfulTest() {
        User user = User.builder()
                .email("eve.holt@reqres.in")
                .password("pistol")
                .build();
        given()
                .header(HTTP.CONTENT_TYPE, "application/json")
                .body(user)
                .log().all()
        .when()
                .post(URL + "/api/register")
        .then()
                .log().all()
                .statusCode(200)
                .body("token", equalTo("QpwL5tke4Pnpja7X4"))
                .body("id" , equalTo(4));
    }
    @Test
    public void postRegisterUnSuccessfulTest() {
        User user = User.builder()
                .email("sydney@fife")
                .build();
        given()
                .header(HTTP.CONTENT_TYPE, "application/json")
                .body(user)
                .log().all()
        .when()
                .post(URL + "/api/register")
        .then()
                .log().all()
                .statusCode(400)
                .body("error",equalTo("Missing password"));
    }
    @Test
    public void postLoginSuccessfulTest() {
        User user = User.builder()
                .email("eve.holt@reqres.in")
                .password("cityslicka")
                .build();
        given()
                .header(HTTP.CONTENT_TYPE, "application/json")
                .body(user)
                .log().all()
        .when()
                .post(URL + "/api/login")
        .then()
                .log().all()
                .statusCode(200);
    }
    @Test
    public void postLoginUnSuccessfulTest() {
        User user = User.builder()
                .email("peter@klaven")
                .build();
        given()
                .header(HTTP.CONTENT_TYPE, "application/json")
                .body(user)
                .log().all()
        .when()
                .post(URL + "/api/login")
        .then()
                .log().all()
                .statusCode(400)
                .body("error",equalTo("Missing password"));
    }
    @Test
    public void getDelayedResponseTest() {
        given()
        .when()
                .get(URL + "/api/users?delay=3")
        .then()
                .log().all()
                .statusCode(200)
                .body("total", equalTo(12))
                .body("data[0].first_name", equalTo("George"));
    }

}

