package br.com.sicredi.election.aceitacao.vote;

import br.com.sicredi.election.aceitacao.base.BaseTest;
import br.com.sicredi.election.builder.*;
import br.com.sicredi.election.dto.candidate.CandidateRequest;
import br.com.sicredi.election.dto.candidate.CandidateResponse;
import br.com.sicredi.election.dto.session.SessionRequest;
import br.com.sicredi.election.dto.session.SessionResponse;
import br.com.sicredi.election.dto.vote.VoteRequest;
import br.com.sicredi.election.dto.vote.VoteResponse;
import br.com.sicredi.election.dto.voter.VoterRequest;
import br.com.sicredi.election.dto.voter.VoterResponse;
import br.com.sicredi.election.dto.zone.ZoneRequest;
import br.com.sicredi.election.dto.zone.ZoneResponse;
import br.com.sicredi.election.service.*;
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

@DisplayName("Voto")
@Epic("Cadastrar voto")
@Feature("Voto")
public class PostVoteTest extends BaseTest {
    VoteService voteService = new VoteService();
    VoteBuilder voteBuilder = new VoteBuilder();
    VoterService voterService = new VoterService();
    VoterBuilder voterBuilder = new VoterBuilder();
    ZoneService zoneService = new ZoneService();
    ZoneBuilder zoneBuilder = new ZoneBuilder();
    SessionService sessionService = new SessionService();
    SessionBuilder sessionBuilder = new SessionBuilder();
    CandidateService candidateService = new CandidateService();
    CandidateBuilder candidateBuilder = new CandidateBuilder();

    @Test
    @Tag("all")
    @Description("Deve cadastrar um voto do elitor com sucesso")
    public void create_WhenVoteRequestIsOk_ThenVoteCreatedSuccessfully(){
        ZoneRequest zoneRequest = zoneBuilder.create_ZoneIsOk();
        ZoneResponse zoneResponse = zoneService.createZone(Utils.convertZoneToJson(zoneRequest)).then().extract().as(ZoneResponse.class);

        SessionRequest sessionRequest = sessionBuilder.create_SessionIsOk(zoneResponse.getZoneId());
        SessionResponse sessionResponse = sessionService.createSession(Utils.convertSessionToJson(sessionRequest)).then().extract().as(SessionResponse.class);

        VoterRequest voterRequest = voterBuilder.create_VoterIsOk(sessionResponse.getSessionId());
        VoterResponse voterResponse = voterService.createVoter(Utils.convertVoterToJson(voterRequest)).then().extract().as(VoterResponse.class);

        CandidateRequest candidateRequest = candidateBuilder.create_WhenCandidateRequestIsOk_ThenCandidateCreateSuccessfully();
        CandidateResponse candidateResponse = candidateService.createCandidate(Utils.convertCandidateToJson(candidateRequest)).then().extract().as(CandidateResponse.class);

        VoteRequest voteRequest = voteBuilder.create_VoteIsOk(candidateResponse.getNumber(),voterResponse.getSessionId(),voterResponse.getCpf());
        VoteResponse voteResponse = voteService.createVote(Utils.convertVoteToJson(voteRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .body("number",is(candidateResponse.getNumber()))
                .body("sessionId",is(voterResponse.getSessionId()))
                .extract().as(VoteResponse.class)
                ;

        voteService.deleteVote(voteResponse.getVoteId());

        voterService.deleteVoter(voterResponse.getVoterId());

        sessionService.deleteSession(sessionResponse.getSessionId());

        candidateService.deleteCandidate(candidateResponse.getCandidateId());

        zoneService.deleteZone(zoneResponse.getZoneId());
    }

    @Test
    @Tag("all")
    @Description("Tentar cadastrar um voto do elitor que ja realizou o voto")
    public void create_WhenVoteRequestIsVoterHasAlreadyVoted_ThenReturnMessageThisVoterHasAlreadyVoted(){
        ZoneRequest zoneRequest = zoneBuilder.create_ZoneIsOk();
        ZoneResponse zoneResponse = zoneService.createZone(Utils.convertZoneToJson(zoneRequest)).then().extract().as(ZoneResponse.class);

        SessionRequest sessionRequest = sessionBuilder.create_SessionIsOk(zoneResponse.getZoneId());
        SessionResponse sessionResponse = sessionService.createSession(Utils.convertSessionToJson(sessionRequest)).then().extract().as(SessionResponse.class);

        VoterRequest voterRequest = voterBuilder.create_VoterIsOk(sessionResponse.getSessionId());
        VoterResponse voterResponse = voterService.createVoter(Utils.convertVoterToJson(voterRequest)).then().extract().as(VoterResponse.class);

        CandidateRequest candidateRequest = candidateBuilder.create_WhenCandidateRequestIsOk_ThenCandidateCreateSuccessfully();
        CandidateResponse candidateResponse = candidateService.createCandidate(Utils.convertCandidateToJson(candidateRequest)).then().extract().as(CandidateResponse.class);

        VoteRequest voteRequest = voteBuilder.create_VoteIsOk(candidateResponse.getNumber(),voterResponse.getSessionId(),voterResponse.getCpf());
        VoteResponse voteResponse = voteService.createVote(Utils.convertVoteToJson(voteRequest)).then().extract().as(VoteResponse.class);
        voteService.createVote(Utils.convertVoteToJson(voteRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString("Eleitor já votou."))
        ;

        voteService.deleteVote(voteResponse.getVoteId());

        voterService.deleteVoter(voterResponse.getVoterId());

        sessionService.deleteSession(sessionResponse.getSessionId());

        candidateService.deleteCandidate(candidateResponse.getCandidateId());

        zoneService.deleteZone(zoneResponse.getZoneId());
    }

    @Test
    @Tag("all")
    @Description("Tentar cadastrar um voto vazio")
    public void create_WhenVoteRequestIsEmpty_ThenReturnMessageNullError(){

        VoteRequest voteRequest = voteBuilder.create_VoteIsEmpty();
        voteService.createVote(Utils.convertVoteToJson(voteRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString("O number não pode ser nulo"))
                .body(containsString("O sessionId não pode ser nulo"))
                .body(containsString("O cpf não pode ser nulo"))
        ;
    }

    @Test
    @Tag("all")
    @Description("Tentar cadastrar um voto em um candidato inexistente")
    public void create_WhenVoteRequestIsCandidateNumberInvalid_ThenReturnMessageCandidateNoExist(){
        ZoneRequest zoneRequest = zoneBuilder.create_ZoneIsOk();
        ZoneResponse zoneResponse = zoneService.createZone(Utils.convertZoneToJson(zoneRequest)).then().extract().as(ZoneResponse.class);

        SessionRequest sessionRequest = sessionBuilder.create_SessionIsOk(zoneResponse.getZoneId());
        SessionResponse sessionResponse = sessionService.createSession(Utils.convertSessionToJson(sessionRequest)).then().extract().as(SessionResponse.class);

        VoterRequest voterRequest = voterBuilder.create_VoterIsOk(sessionResponse.getSessionId());
        VoterResponse voterResponse = voterService.createVoter(Utils.convertVoterToJson(voterRequest)).then().extract().as(VoterResponse.class);

        CandidateRequest candidateRequest = candidateBuilder.create_WhenCandidateRequestIsOk_ThenCandidateCreateSuccessfully();
        CandidateResponse candidateResponse = candidateService.createCandidate(Utils.convertCandidateToJson(candidateRequest)).then().extract().as(CandidateResponse.class);

        VoteRequest voteRequest = voteBuilder.create_VoteIsNumberInvalid(voterResponse.getSessionId(),voterResponse.getCpf());
        voteService.createVote(Utils.convertVoteToJson(voteRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body(containsString("Candidato com esse número não existe."))
                ;


        voterService.deleteVoter(voterResponse.getVoterId());

        sessionService.deleteSession(sessionResponse.getSessionId());

        candidateService.deleteCandidate(candidateResponse.getCandidateId());

        zoneService.deleteZone(zoneResponse.getZoneId());
    }

    @Test
    @Tag("all")
    @Description("Tentar cadastrar um voto passando uma seção invalida")
    public void create_WhenVoteRequestIsVoterSessionInvalid_ThenReturnMessageSessionNoExist(){
        ZoneRequest zoneRequest = zoneBuilder.create_ZoneIsOk();
        ZoneResponse zoneResponse = zoneService.createZone(Utils.convertZoneToJson(zoneRequest)).then().extract().as(ZoneResponse.class);

        SessionRequest sessionRequest = sessionBuilder.create_SessionIsOk(zoneResponse.getZoneId());
        SessionResponse sessionResponse = sessionService.createSession(Utils.convertSessionToJson(sessionRequest)).then().extract().as(SessionResponse.class);

        VoterRequest voterRequest = voterBuilder.create_VoterIsOk(sessionResponse.getSessionId());
        VoterResponse voterResponse = voterService.createVoter(Utils.convertVoterToJson(voterRequest)).then().extract().as(VoterResponse.class);

        CandidateRequest candidateRequest = candidateBuilder.create_WhenCandidateRequestIsOk_ThenCandidateCreateSuccessfully();
        CandidateResponse candidateResponse = candidateService.createCandidate(Utils.convertCandidateToJson(candidateRequest)).then().extract().as(CandidateResponse.class);

        VoteRequest voteRequest = voteBuilder.create_VoteIsSessionInvalid(candidateResponse.getNumber(),voterResponse.getCpf());
        voteService.createVote(Utils.convertVoteToJson(voteRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body(containsString("A seção não existe."))
        ;

        voterService.deleteVoter(voterResponse.getVoterId());

        sessionService.deleteSession(sessionResponse.getSessionId());

        candidateService.deleteCandidate(candidateResponse.getCandidateId());

        zoneService.deleteZone(zoneResponse.getZoneId());
    }

    @Test
    @Tag("all")
    @Description("Tentar cadastrar um voto de um eleitor com cpf invalido na seção")
    public void create_WhenVoteRequestIsVoterCpfInvalid_ThenReturnMessageVoter(){
        ZoneRequest zoneRequest = zoneBuilder.create_ZoneIsOk();
        ZoneResponse zoneResponse = zoneService.createZone(Utils.convertZoneToJson(zoneRequest)).then().extract().as(ZoneResponse.class);

        SessionRequest sessionRequest = sessionBuilder.create_SessionIsOk(zoneResponse.getZoneId());
        SessionResponse sessionResponse = sessionService.createSession(Utils.convertSessionToJson(sessionRequest)).then().extract().as(SessionResponse.class);

        VoterRequest voterRequest = voterBuilder.create_VoterIsOk(sessionResponse.getSessionId());
        VoterResponse voterResponse = voterService.createVoter(Utils.convertVoterToJson(voterRequest)).then().extract().as(VoterResponse.class);

        CandidateRequest candidateRequest = candidateBuilder.create_WhenCandidateRequestIsOk_ThenCandidateCreateSuccessfully();
        CandidateResponse candidateResponse = candidateService.createCandidate(Utils.convertCandidateToJson(candidateRequest)).then().extract().as(CandidateResponse.class);

        VoteRequest voteRequest = voteBuilder.create_VoteIsCpfInvalid(candidateResponse.getNumber(),voterResponse.getSessionId());
        voteService.createVote(Utils.convertVoteToJson(voteRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body(containsString("CPF do eleitor não encontrado nessa sessão."))
        ;

        voterService.deleteVoter(voterResponse.getVoterId());

        sessionService.deleteSession(sessionResponse.getSessionId());

        candidateService.deleteCandidate(candidateResponse.getCandidateId());

        zoneService.deleteZone(zoneResponse.getZoneId());
    }
}
