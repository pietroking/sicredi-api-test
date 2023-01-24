package br.com.sicredi.election.aceitacao.voter;

import br.com.sicredi.election.aceitacao.base.BaseTest;
import br.com.sicredi.election.builder.SessionBuilder;
import br.com.sicredi.election.builder.VoterBuilder;
import br.com.sicredi.election.builder.ZoneBuilder;
import br.com.sicredi.election.dto.session.SessionRequest;
import br.com.sicredi.election.dto.session.SessionResponse;
import br.com.sicredi.election.dto.voter.VoterRequest;
import br.com.sicredi.election.dto.voter.VoterResponse;
import br.com.sicredi.election.dto.zone.ZoneRequest;
import br.com.sicredi.election.dto.zone.ZoneResponse;
import br.com.sicredi.election.service.SessionService;
import br.com.sicredi.election.service.VoterService;
import br.com.sicredi.election.service.ZoneService;
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

@DisplayName("Eleitor")
@Epic("Atualizar eleitores")
@Feature("Eleitor")
public class PatchVoterTest extends BaseTest {
    VoterService voterService = new VoterService();
    VoterBuilder voterBuilder = new VoterBuilder();
    ZoneService zoneService = new ZoneService();
    ZoneBuilder zoneBuilder = new ZoneBuilder();
    SessionService sessionService = new SessionService();
    SessionBuilder sessionBuilder = new SessionBuilder();

    @Test
    @Tag("all")
    @Description("Deve atualizar um elitor com sucesso")
    public void update_WhenVoterUpdateRequestIsOk_ThenVoterUpdateSuccessfully(){
        ZoneRequest zoneRequest = zoneBuilder.create_ZoneIsOk();
        ZoneResponse zoneResponse = zoneService.createZone(Utils.convertZoneToJson(zoneRequest)).then().extract().as(ZoneResponse.class);

        SessionRequest sessionRequest = sessionBuilder.create_SessionIsOk(zoneResponse.getZoneId());
        SessionResponse sessionResponse = sessionService.createSession(Utils.convertSessionToJson(sessionRequest)).then().extract().as(SessionResponse.class);

        SessionRequest sessionRequest2 = sessionBuilder.create_SessionIsOk2(zoneResponse.getZoneId());
        SessionResponse sessionResponse2 = sessionService.createSession(Utils.convertSessionToJson(sessionRequest2)).then().extract().as(SessionResponse.class);

        VoterRequest voterRequest = voterBuilder.create_VoterIsOk(sessionResponse.getSessionId());
        VoterResponse voterResponse = voterService.createVoter(Utils.convertVoterToJson(voterRequest)).then().extract().as(VoterResponse.class);

        VoterRequest voterUpdateRequest = voterBuilder.create_VoterIsOk(sessionResponse2.getSessionId());
        voterService.updateVoter(Utils.convertVoterToJson(voterUpdateRequest),voterResponse.getVoterId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("voterId", is(voterResponse.getVoterId()))
                .body("name", is(voterResponse.getName()))
                .body("sessionId", is(sessionResponse2.getSessionId()))
                .body("cpf", is(voterResponse.getCpf()))
                .extract().as(VoterResponse.class)
                ;

        voterService.deleteVoter(voterResponse.getVoterId());
        sessionService.deleteSession(sessionResponse.getSessionId());
        sessionService.deleteSession(sessionResponse2.getSessionId());
        zoneService.deleteZone(zoneResponse.getZoneId());
    }

    @Test
    @Tag("all")
    @Description("Tentar atualizar um elitor passando seção vazia")
    public void update_WhenVoterUpdateRequestIsEmpty_ThenReturnMessageNullError(){
        ZoneRequest zoneRequest = zoneBuilder.create_ZoneIsOk();
        ZoneResponse zoneResponse = zoneService.createZone(Utils.convertZoneToJson(zoneRequest)).then().extract().as(ZoneResponse.class);

        SessionRequest sessionRequest = sessionBuilder.create_SessionIsOk(zoneResponse.getZoneId());
        SessionResponse sessionResponse = sessionService.createSession(Utils.convertSessionToJson(sessionRequest)).then().extract().as(SessionResponse.class);

        VoterRequest voterRequest = voterBuilder.create_VoterIsOk(sessionResponse.getSessionId());
        VoterResponse voterResponse = voterService.createVoter(Utils.convertVoterToJson(voterRequest)).then().extract().as(VoterResponse.class);

        VoterRequest voterUpdateRequest = voterBuilder.update_VoterIsSessionEmpty();
        voterService.updateVoter(Utils.convertVoterToJson(voterUpdateRequest),voterResponse.getVoterId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString("O sessionId não pode ser nulo"))
                ;

        voterService.deleteVoter(voterResponse.getVoterId());
        sessionService.deleteSession(sessionResponse.getSessionId());
        zoneService.deleteZone(zoneResponse.getZoneId());
    }

    @Test
    @Tag("all")
    @Description("Tentar atualizar um elitor passando seção não existente")
    public void update_WhenSessionIdIsInvalid_ThenReturnMessageSessionNotExist(){
        ZoneRequest zoneRequest = zoneBuilder.create_ZoneIsOk();
        ZoneResponse zoneResponse = zoneService.createZone(Utils.convertZoneToJson(zoneRequest)).then().extract().as(ZoneResponse.class);

        SessionRequest sessionRequest = sessionBuilder.create_SessionIsOk(zoneResponse.getZoneId());
        SessionResponse sessionResponse = sessionService.createSession(Utils.convertSessionToJson(sessionRequest)).then().extract().as(SessionResponse.class);

        VoterRequest voterRequest = voterBuilder.create_VoterIsOk(sessionResponse.getSessionId());
        VoterResponse voterResponse = voterService.createVoter(Utils.convertVoterToJson(voterRequest)).then().extract().as(VoterResponse.class);

        VoterRequest voterUpdateRequest = voterBuilder.update_VoterIsSessionInvalid();
        voterService.updateVoter(Utils.convertVoterToJson(voterUpdateRequest),voterResponse.getVoterId())
                .then()
                .body(containsString("A seção não existe."))
                ;

        voterService.deleteVoter(voterResponse.getVoterId());
        sessionService.deleteSession(sessionResponse.getSessionId());
        zoneService.deleteZone(zoneResponse.getZoneId());
    }
}
