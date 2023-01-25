package br.com.sicredi.election.aceitacao.candidate;

import br.com.sicredi.election.aceitacao.base.BaseTest;
import br.com.sicredi.election.builder.CandidateBuilder;
import br.com.sicredi.election.dto.candidate.CandidateRequest;
import br.com.sicredi.election.dto.candidate.CandidateResponse;
import br.com.sicredi.election.service.CandidateService;
import br.com.sicredi.election.utils.Utils;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

@DisplayName("Candidatos")
@Epic("Cadastrar candidatos")
@Feature("Candidatos")
public class PostCandidateTest extends BaseTest {
    CandidateService candidateService = new CandidateService();
    CandidateBuilder candidateBuilder = new CandidateBuilder();

    @Test
    @Tag("all")
    @Description("Deve cadastrar candidato com sucesso")
    public void create_WhenCandidateRequestIsOk_ThenCandidateCreateSuccessfully(){
        CandidateRequest candidateRequest = candidateBuilder.create_WhenCandidateRequestIsOk_ThenCandidateCreateSuccessfully();
        CandidateResponse candidateResponse = candidateService.createCandidate(Utils.convertCandidateToJson(candidateRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .body("name",is(candidateRequest.getName()))
                .body("cpf",is(candidateRequest.getCpf()))
                .body("number",is(candidateRequest.getNumber()))
                .body("party",is(candidateRequest.getParty()))
                .extract().as(CandidateResponse.class)
                ;

        candidateService.deleteCandidate(candidateResponse.getCandidateId());
    }

    @Test
    @Tag("all")
    @Description("Tentar cadastrar candidato vazio")
    public void create_WhenCandidateRequestIsEmpty_ThenReturnMessageNullError(){
        CandidateRequest candidateRequest = candidateBuilder.create_WhenCandidateRequestIsEmpty_ThenReturnMessageError();
        candidateService.createCandidate(Utils.convertCandidateToJson(candidateRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString("O number não pode ser nulo"))
                .body(containsString("O party não pode ser nulo"))
                .body(containsString("O cpf não pode ser nulo"))
                .body(containsString("O nome não pode ser nulo"))
                ;
    }

    @Test
    @Tag("all")
    @Description("Tentar cadastrar candidato com cpf invalido")
    public void create_WhenCandidateRequestIsCpfInvalid_ThenReturnMessageCpfInvalid(){
        CandidateRequest candidateRequest = candidateBuilder.create_WhenCandidateRequestIsCpfInvalid_ThenReturnMessageCpfInvalid();
        candidateService.createCandidate(Utils.convertCandidateToJson(candidateRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString("O CPF está inválido"))
        ;
    }

    @Test
    @Tag("all")
    @Description("Tentar cadastrar candidato com number invalido")
    public void create_WhenCandidateRequestIsNumberInvalid_ThenReturnMessageNumberInvalid(){
        CandidateRequest candidateRequest = candidateBuilder.update_WhenCandidateUpdateRequestIsNumberNegative_ThenReturnMessageNegativeNumberError();
        candidateService.createCandidate(Utils.convertCandidateToJson(candidateRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString("O number não pode ser negativo"))
        ;
    }
}
