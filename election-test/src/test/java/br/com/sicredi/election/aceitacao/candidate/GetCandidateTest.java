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

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Candidatos")
@Epic("Listar candidatos")
@Feature("Candidatos")
public class GetCandidateTest extends BaseTest {
    CandidateService candidateService = new CandidateService();
    CandidateBuilder candidateBuilder = new CandidateBuilder();

    @Test
    @Tag("all")
    @Description("Deve listar candidatos registrados")
    public void findAllCandidate_WhenCandidate_ThenReturnListOfCandidateCreated(){
        CandidateResponse[] listCandidate = candidateService.findAll()
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(CandidateResponse[].class)
                ;
        assertNotNull(listCandidate);
    }

    @Test
    @Tag("all")
    @Description("Deve listar candidatos buscados por um nome")
    public void findCandidateFindByName_WhenFindName_ThenReturnListOfCandidates(){
        CandidateRequest candidateRequest = candidateBuilder.create_WhenCandidateRequestIsOk_ThenCandidateCreateSuccessfully();
        CandidateResponse candidateResponse = candidateService.createCandidate(Utils.convertCandidateToJson(candidateRequest))
                .then()
                .extract().as(CandidateResponse.class)
                ;

        candidateService.findByName(candidateResponse.getName())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("name",hasItem(candidateResponse.getName()))
                ;

        candidateService.deleteCandidate(candidateResponse.getCandidateId());
    }

    @Test
    @Tag("all")
    @Description("Tentar listar candidatos buscados por um nome não existente na lista")
    public void findCandidateFindByName_WhenNameHasNoMention_ThenReturnMessageNoCandidateCreated(){
        candidateService.findByName("kkkkkkkkkk")
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body(containsString("Não existem candidatos criados."))
                ;
    }

    @Test
    @Tag("all")
    @Description("Deve listar contagem dos votos da eleição")
    public void findVotesResult_WhenElection_ThenReturnListOfCountingOfVotes(){
        CandidateResponse[] listCandidate = candidateService.findContVotes()
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(CandidateResponse[].class)
                ;
        assertNotNull(listCandidate);
    }
}
