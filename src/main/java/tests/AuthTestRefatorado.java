package tests;

import core.BaseTest;
import io.restassured.RestAssured;
import io.restassured.specification.FilterableRequestSpecification;
import org.junit.Test;

import static io.restassured.RestAssured.*;

public class AuthTestRefatorado extends BaseTest {

    @Test
    public void deveTestarNaoDeveAcessarSemUmToken () {


        FilterableRequestSpecification req = (FilterableRequestSpecification) RestAssured.requestSpecification;
        req.removeHeader("Authorization");

        String mensagem = given()
                .when()
                .post("/contas")
                .then()
                .statusCode(401)
                .extract().body().asString();

        ;

        System.out.println(mensagem);
    }

}
