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
@Epic("Atualizar candidatos")
@Feature("Candidatos")
public class PatchCandidateTest extends BaseTest {
    CandidateService candidateService = new CandidateService();
    CandidateBuilder candidateBuilder = new CandidateBuilder();

    @Test
    @Tag("all")
    @Description("Deve atualizar candidato com sucesso")
    public void update_WhenCandidateUpdateRequestIsOk_ThenCandidateUpdateSuccessfully(){
        CandidateRequest candidateRequest = candidateBuilder.create_WhenCandidateRequestIsOk_ThenCandidateCreateSuccessfully();
        CandidateResponse candidateResponse = candidateService.createCandidate(Utils.convertCandidateToJson(candidateRequest)).then().extract().as(CandidateResponse.class);

        CandidateRequest candidateUpdateRequest = candidateBuilder.update_WhenCandidateUpdateRequestIsOk_ThenCandidateUpdatedSuccessfully();
        candidateService.updateCandidate(Utils.convertCandidateToJson(candidateUpdateRequest),candidateResponse.getCandidateId())
                        .then()
                        .log().all()
                        .statusCode(HttpStatus.SC_OK)
                        .body("candidateId",is(candidateResponse.getCandidateId()))
                        .body("name",is(candidateResponse.getName()))
                        .body("cpf",is(candidateResponse.getCpf()))
                        .body("number",is(candidateUpdateRequest.getNumber()))
                        .body("party",is(candidateUpdateRequest.getParty()))
                        ;

        candidateService.deleteCandidate(candidateResponse.getCandidateId());
    }

    @Test
    @Tag("all")
    @Description("Tentar atualizar candidato passando dados vazio")
    public void update_WhenCandidateUpdateRequestIsEmpty_ThenReturnMessageNullError(){
        CandidateRequest candidateRequest = candidateBuilder.create_WhenCandidateRequestIsOk_ThenCandidateCreateSuccessfully();
        CandidateResponse candidateResponse = candidateService.createCandidate(Utils.convertCandidateToJson(candidateRequest)).then().extract().as(CandidateResponse.class);

        CandidateRequest candidateUpdateRequest = candidateBuilder.update_WhenCandidateUpdateRequestIsEmpty_ThenReturnMessageError();
        candidateService.updateCandidate(Utils.convertCandidateToJson(candidateUpdateRequest),candidateResponse.getCandidateId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString("O number não pode ser nulo"))
                .body(containsString("O party não pode ser nulo"))
        ;

        candidateService.deleteCandidate(candidateResponse.getCandidateId());
    }

    @Test
    @Tag("all")
    @Description("Tentar atualizar candidato com number invalido")
    public void update_WhenCandidateUpdateRequestIsNumberInvalid_ThenReturnMessageNumberInvalid(){
        CandidateRequest candidateRequest = candidateBuilder.create_WhenCandidateRequestIsOk_ThenCandidateCreateSuccessfully();
        CandidateResponse candidateResponse = candidateService.createCandidate(Utils.convertCandidateToJson(candidateRequest)).then().extract().as(CandidateResponse.class);

        CandidateRequest candidateUpdateRequest = candidateBuilder.update_WhenCandidateUpdateRequestIsNumberNegative_ThenReturnMessageNegativeNumberError();
        candidateService.updateCandidate(Utils.convertCandidateToJson(candidateUpdateRequest),candidateResponse.getCandidateId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString("O number não pode ser negativo"))
        ;

        candidateService.deleteCandidate(candidateResponse.getCandidateId());
    }

    @Test
    @Tag("all")
    @Description("Tentar atualizar candidato inexistente")
    public void update_WhenCandidateIdIsInvalid_ThenReturnMessageCandidateNotExist(){

        CandidateRequest candidateUpdateRequest = candidateBuilder.create_WhenCandidateRequestIsOk_ThenCandidateCreateSuccessfully();
        candidateService.updateCandidate(Utils.convertCandidateToJson(candidateUpdateRequest),999999999)
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body(containsString("O candidato não existe."))
        ;
    }
}
