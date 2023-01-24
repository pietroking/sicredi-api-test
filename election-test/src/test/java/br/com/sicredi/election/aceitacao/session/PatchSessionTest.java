package br.com.sicredi.election.aceitacao.session;

import br.com.sicredi.election.aceitacao.base.BaseTest;
import br.com.sicredi.election.builder.SessionBuilder;
import br.com.sicredi.election.builder.ZoneBuilder;
import br.com.sicredi.election.dto.session.SessionRequest;
import br.com.sicredi.election.dto.session.SessionResponse;
import br.com.sicredi.election.dto.zone.ZoneRequest;
import br.com.sicredi.election.dto.zone.ZoneResponse;
import br.com.sicredi.election.service.SessionService;
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

@DisplayName("Seção")
@Epic("Atualizar seções")
@Feature("Seção")
public class PatchSessionTest extends BaseTest {
    ZoneService zoneService = new ZoneService();
    ZoneBuilder zoneBuilder = new ZoneBuilder();
    SessionService sessionService = new SessionService();
    SessionBuilder sessionBuilder = new SessionBuilder();

    @Test
    @Tag("all")
    @Description("Deve atualizar seção com sucesso")
    public void update_WhenSessionUpdateRequestIsOk_ThenSessionUpdateSuccessfully(){
        ZoneRequest zoneRequest = zoneBuilder.create_ZoneIsOk();
        ZoneResponse zoneResponse = zoneService.createZone(Utils.convertZoneToJson(zoneRequest)).then().extract().as(ZoneResponse.class);

        SessionRequest sessionRequest = sessionBuilder.create_SessionIsOk(zoneResponse.getZoneId());
        SessionResponse sessionResponse = sessionService.createSession(Utils.convertSessionToJson(sessionRequest)).then().extract().as(SessionResponse.class);

        SessionRequest sessionUpdateRequest = sessionBuilder.update_SessionUrnNumberIsOk();
        sessionService.updateSession(Utils.convertSessionToJson(sessionUpdateRequest),sessionResponse.getSessionId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("sessionId",is(sessionResponse.getSessionId()))
                .body("zoneId",is(sessionResponse.getZoneId()))
                .body("number",is(sessionResponse.getNumber()))
                .body("urnNumber",is(sessionUpdateRequest.getUrnNumber()))
                ;

        sessionService.deleteSession(sessionResponse.getSessionId());

        zoneService.deleteZone(zoneResponse.getZoneId());
    }

    @Test
    @Tag("all")
    @Description("Tentar atualizar uma seção com numero da urna vazia")
    public void update_WhenUrnNumberUpdateRequestIsEmpty_ThenReturnMessageNullError(){
        ZoneRequest zoneRequest = zoneBuilder.create_ZoneIsOk();
        ZoneResponse zoneResponse = zoneService.createZone(Utils.convertZoneToJson(zoneRequest)).then().extract().as(ZoneResponse.class);

        SessionRequest sessionRequest = sessionBuilder.create_SessionIsOk(zoneResponse.getZoneId());
        SessionResponse sessionResponse = sessionService.createSession(Utils.convertSessionToJson(sessionRequest)).then().extract().as(SessionResponse.class);

        SessionRequest sessionUpdateRequest = sessionBuilder.update_SessionUrnNumberNull();
        sessionService.updateSession(Utils.convertSessionToJson(sessionUpdateRequest),sessionResponse.getSessionId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString("O urnNumber não pode ser nulo"))
                ;

        sessionService.deleteSession(sessionResponse.getSessionId());

        zoneService.deleteZone(zoneResponse.getZoneId());
    }

    @Test
    @Tag("all")
    @Description("Tentar atualizar uma seção com numero da urna negativo")
    public void update_WhenUrnNumberUpdateRequestIsNumberInvalid_ThenReturnMessageUrnNumberInvalid(){
        ZoneRequest zoneRequest = zoneBuilder.create_ZoneIsOk();
        ZoneResponse zoneResponse = zoneService.createZone(Utils.convertZoneToJson(zoneRequest)).then().extract().as(ZoneResponse.class);

        SessionRequest sessionRequest = sessionBuilder.create_SessionIsOk(zoneResponse.getZoneId());
        SessionResponse sessionResponse = sessionService.createSession(Utils.convertSessionToJson(sessionRequest)).then().extract().as(SessionResponse.class);

        SessionRequest sessionUpdateRequest = sessionBuilder.update_SessionUrnNumberNegative();
        sessionService.updateSession(Utils.convertSessionToJson(sessionUpdateRequest),sessionResponse.getSessionId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString("O urnNumber não pode ser negativo"))
                ;

        sessionService.deleteSession(sessionResponse.getSessionId());

        zoneService.deleteZone(zoneResponse.getZoneId());
    }

    @Test
    @Tag("all")
    @Description("Tentar atualizar uma seção inexistente")
    public void update_WhenSessionIdIsInvalid_ThenReturnMessageSessionNotExist(){

        SessionRequest sessionUpdateRequest = sessionBuilder.update_SessionUrnNumberIsOk();
        sessionService.updateSession(Utils.convertSessionToJson(sessionUpdateRequest),999999999)
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body(containsString("A seção não existe."))
                ;
    }
}
