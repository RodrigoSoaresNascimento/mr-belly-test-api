package tests;

import core.BaseTest;
import dto.TransacaoDTO;
import io.restassured.RestAssured;
import io.restassured.specification.FilterableRequestSpecification;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.DataUtils;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class BarrigaTest extends BaseTest {

    private static String CONTA_NAME = "Conta " + System.nanoTime();
    private static Integer CONTA_ID;

    private static Integer MOVIMENTACAO_ID;


    @Test
    public void deveTestarIncluirContaComSucesso () {

        CONTA_ID = given()
                    .body("{ \"nome\": \""+CONTA_NAME+ "\" }")
                .when()
                    .post("/contas")
                .then()
                    .statusCode(201)
                .extract().path("id")
        ;

    }

    @Test
    public void deveTestarAlterarContaComSucesso () {

        given()
                .body("{ \"nome\": \""+CONTA_NAME+ "alterado\" }")
                .pathParam("id", CONTA_ID)
                .when()
                .put("/contas/{id}")
                .then()
                .statusCode(200)
                .body("nome", is(CONTA_NAME+ "alterado"))
        ;

    }

    @Test
    public void deveTestarNaoIncluirContaComMesmoNome () {

        given()
                .body("{ \"nome\": \""+CONTA_NAME+ "alterado\" }")
                .when()
                .post("/contas")
                .then()
                .statusCode(400)
                .body("error", is("Já existe uma conta com esse nome!"))
        ;

    }

    @Test
    public void deveTestarInserirMovimentacaoComSucesso () {


        TransacaoDTO transacaoDTO = getTransacao();

         MOVIMENTACAO_ID = given()
                .when()
                .post("/transacoes")
                .then()
                .statusCode(201)
                .extract().path("id")
        ;

    }

    @Test
    public void deveTestarValidarCamposObrigatoriosComSucesso () {


        given()
                .body("{}")
                .when()
                .post("/transacoes")
                .then()
                .statusCode(400)
                .body("$", hasSize(8))
                .body("msg", hasItems("Data da Movimentação é obrigatório",
                        "Data do pagamento é obrigatório",
                        "Descrição é obrigatório",
                        "Interessado é obrigatório",
                        "Valor é obrigatório",
                        "Valor deve ser um número",
                        "Conta é obrigatório",
                        "Situação é obrigatório"))
        ;

    }

    @Test
    public void deveTestarNaoDeveCadastrarMovimentacaoComDataFutura () {

        TransacaoDTO transacaoDTO = getTransacao();
        transacaoDTO.setData_transacao(DataUtils.getDataDiferenteDias(10));

        given()
                .body(transacaoDTO)
                .when()
                .post("/transacoes")
                .then()
                .statusCode(400)
                .body("msg", hasItem("Data da Movimentação deve ser menor ou igual à data atual"))

        ;
    }

    @Test
    public void deveTestarNaoDeveRemoverContaComTransacao () {

        given()
                .pathParam("id", CONTA_ID)
                .when()
                .delete("/contas/{id}")
                .then()
                .statusCode(500)
                .body("constraint" , is("transacoes_conta_id_foreign"))

        ;
    }

    @Test
    public void deveTestarCalcularSaldoDasContas () {

        given()
                .when()
                .get("/saldo")
                .then()
                .statusCode(200)
                .body("find{it.conta_id =="+CONTA_ID+"}.saldo", is("2.50"))

        ;
    }

    @Test
    public void deveTestarDeveRemoverTransacaoComSucesso () {

        given()
                //tem um espaço no final do nome JWT
                .pathParam("id", MOVIMENTACAO_ID)
                .when()
                .delete("/transacoes/{id}")
                .then()
                .statusCode(204)

        ;
    }

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

    }

    private static TransacaoDTO getTransacao () {
        TransacaoDTO transacaoDTO = new TransacaoDTO();
        transacaoDTO.setConta_id(CONTA_ID);
        //transacaoDTO.setUsuario_id();
        transacaoDTO.setDescricao("alguma compra qualquer");
        transacaoDTO.setEnvolvido("rodrigo");
        transacaoDTO.setStatus(true);
        transacaoDTO.setValor(2.500f);
        transacaoDTO.setData_transacao(DataUtils.getDataDiferenteDias(-1));
        transacaoDTO.setData_pagamento(DataUtils.getDataDiferenteDias(10));
        transacaoDTO.setTipo("REC");
        return transacaoDTO;
    }

}
