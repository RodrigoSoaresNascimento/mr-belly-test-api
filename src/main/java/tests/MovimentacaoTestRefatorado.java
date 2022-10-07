package tests;

import core.BaseTest;
import dto.TransacaoDTO;
import io.restassured.RestAssured;
import org.junit.Test;
import utils.DataUtils;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class MovimentacaoTestRefatorado extends BaseTest {

    @Test
    public void deveTestarInserirMovimentacaoComSucesso () {


        TransacaoDTO transacaoDTO = getTransacao();

        given()
                .body(transacaoDTO)
                .when()
                .post("/transacoes")
                .then()
                .statusCode(201)
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
        transacaoDTO.setData_transacao(DataUtils.getDataDiferenteDias(2));

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

        Integer CONTA_ID = getIdContaPeloNome("Conta com movimentacao");

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
    public void deveTestarDeveRemoverTransacaoComSucesso () {

        Integer MOVIMENTACAO_ID = getIdMovimentacaiPelaDescricao("Movimentacao para exclusao");

        given()
                //tem um espaço no final do nome JWT
                .pathParam("id", MOVIMENTACAO_ID)
                .when()
                .delete("/transacoes/{id}")
                .then()
                .statusCode(204)

        ;
    }

    public static Integer getIdContaPeloNome (String nome){
        return RestAssured.get("/contas?nome="+nome).then().extract().path("id[0]");
    }

    public static Integer getIdMovimentacaiPelaDescricao (String descricao){
        return RestAssured.get("/transacoes?descricao="+descricao).then().extract().path("id[0]");
    }

    private static TransacaoDTO getTransacao () {
        TransacaoDTO transacaoDTO = new TransacaoDTO();
        transacaoDTO.setConta_id(getIdContaPeloNome("Conta para movimentacoes"));
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
