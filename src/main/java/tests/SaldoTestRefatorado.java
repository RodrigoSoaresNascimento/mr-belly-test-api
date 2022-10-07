package tests;

import core.BaseTest;
import dto.TransacaoDTO;
import io.restassured.RestAssured;
import org.junit.Test;
import utils.DataUtils;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class SaldoTestRefatorado extends BaseTest {

    @Test
    public void deveTestarCalcularSaldoDasContas () {

        Integer CONTA_ID = getIdContaPeloNome("Conta para saldo");

        given()
                .when()
                .get("/saldo")
                .then()
                .statusCode(200)
                .body("find{it.conta_id =="+CONTA_ID+"}.saldo", is("534.00"))

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
