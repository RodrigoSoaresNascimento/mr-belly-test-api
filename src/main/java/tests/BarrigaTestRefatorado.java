package tests;

import core.BaseTest;
import io.restassured.RestAssured;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class BarrigaTestRefatorado extends BaseTest {

    @Test
    public void deveTestarIncluirContaComSucesso () {

        given()
                .body("{ \"nome\": \"Conta inserida\" }")
                .when()
                .post("/contas")
                .then()
                .statusCode(201)
        ;

    }

    @Test
    public void deveTestarAlterarContaComSucesso () {

        Integer CONTA_ID = getIdContaPeloNome("Conta para alterar");

        given()
                .body("{ \"nome\": \"Conta alterado\" }")
                .pathParam("id", CONTA_ID)
                .when()
                .put("/contas/{id}")
                .then()
                .statusCode(200)
                .body("nome", is("Conta alterado"))
        ;

    }

    @Test
    public void deveTestarNaoIncluirContaComMesmoNome () {

        given()
                .body("{ \"nome\": \"Conta mesmo nome\" }")
                .when()
                .post("/contas")
                .then()
                .statusCode(400)
                .body("error", is("Já existe uma conta com esse nome!"))
        ;

    }

    public Integer getIdContaPeloNome (String nome){
        return RestAssured.get("/contas?nome="+nome).then().extract().path("id[0]");
    }

}
