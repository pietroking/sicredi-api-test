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
@DisplayName("Candidatos")
@Epic("Deletar candidatos")
@Feature("Candidatos")
public class DeleteCandidateTest extends BaseTest {
    CandidateService candidateService = new CandidateService();
    CandidateBuilder candidateBuilder = new CandidateBuilder();

    @Test
    @Tag("all")
    @Description("Deve deletar candidato com sucesso")
    public void delete_WhenCandidateIsOk_ThenCandidateDeletedSuccessfully(){
        CandidateRequest candidateRequest = candidateBuilder.create_WhenCandidateRequestIsOk_ThenCandidateCreateSuccessfully();
        CandidateResponse candidateResponse = candidateService.createCandidate(Utils.convertCandidateToJson(candidateRequest)).then().extract().as(CandidateResponse.class);

        candidateService.deleteCandidate(candidateResponse.getCandidateId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NO_CONTENT)
                ;
    }

    @Test
    @Tag("all")
    @Description("Tentar deletar candidato inexistente")
    public void delete_WhenCandidateIdInvalid_ThenReturnMessageError(){

        candidateService.deleteCandidate(999999999)
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body(containsString("O candidato não existe."))
        ;
    }
}
