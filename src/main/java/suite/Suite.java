package suite;

import core.BaseTest;
import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import tests.AuthTestRefatorado;
import tests.BarrigaTestRefatorado;
import tests.MovimentacaoTestRefatorado;
import tests.SaldoTestRefatorado;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@RunWith(org.junit.runners.Suite.class)
@org.junit.runners.Suite.SuiteClasses({
        BarrigaTestRefatorado.class,
        MovimentacaoTestRefatorado.class,
        SaldoTestRefatorado.class,
        AuthTestRefatorado.class
})
public class Suite extends BaseTest {

    @BeforeClass
    public static void logarUsuario() {
        Map<String, String> login = new HashMap<>();
        login.put("email", "rodrigosnascimento1@gmail.com");
        login.put("senha", "Ir@nmam98");

        String token = given()
                .body(login)
                .when()
                .post("/signin")
                .then()
                .statusCode(200)
                .extract().path("token")
                ;

        RestAssured.requestSpecification.header("Authorization","JWT " + token);

        RestAssured.get("/reset").then().statusCode(200);

    }

}
