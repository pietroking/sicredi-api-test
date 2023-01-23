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
import static org.junit.jupiter.api.Assertions.assertEquals;
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
    public void updateSessionIsOk(){
        ZoneRequest zoneRequest = zoneBuilder.create_ZoneIsOk();
        ZoneResponse zoneResponse = zoneService.createZone(Utils.convertZoneToJson(zoneRequest)).then().extract().as(ZoneResponse.class);

        SessionRequest sessionRequest = sessionBuilder.create_SessionIsOk(zoneResponse.getZoneId());
        SessionResponse sessionResponse = sessionService.createSession(Utils.convertSessionToJson(sessionRequest)).then().extract().as(SessionResponse.class);

        SessionRequest sessionUpdateRequest = sessionBuilder.update_SessionUrnNumberIsOk();
        SessionResponse sessionUpdate = sessionService.updateSession(Utils.convertSessionToJson(sessionUpdateRequest),sessionResponse.getSessionId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(SessionResponse.class)
                ;
        assertEquals(sessionResponse.getSessionId(),sessionUpdate.getSessionId());
        assertEquals(sessionResponse.getZoneId(),sessionUpdate.getZoneId());
        assertEquals(sessionResponse.getNumber(),sessionUpdate.getNumber());
        assertEquals(sessionUpdateRequest.getUrnNumber(),sessionUpdate.getUrnNumber());

        sessionService.deleteSession(sessionResponse.getSessionId());

        zoneService.deleteZone(zoneResponse.getZoneId());
    }

    @Test
    @Tag("all")
    @Description("Tentar atualizar uma seção com numero da urna vazia")
    public void updateSessionIsUrnNumberEmpty(){
        ZoneRequest zoneRequest = zoneBuilder.create_ZoneIsOk();
        ZoneResponse zoneResponse = zoneService.createZone(Utils.convertZoneToJson(zoneRequest)).then().extract().as(ZoneResponse.class);

        SessionRequest sessionRequest = sessionBuilder.create_SessionIsOk(zoneResponse.getZoneId());
        SessionResponse sessionResponse = sessionService.createSession(Utils.convertSessionToJson(sessionRequest))
                .then().extract().as(SessionResponse.class);

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
    public void updateSessionIsUrnNumberNegative(){
        ZoneRequest zoneRequest = zoneBuilder.create_ZoneIsOk();
        ZoneResponse zoneResponse = zoneService.createZone(Utils.convertZoneToJson(zoneRequest)).then().extract().as(ZoneResponse.class);

        SessionRequest sessionRequest = sessionBuilder.create_SessionIsOk(zoneResponse.getZoneId());
        SessionResponse sessionResponse = sessionService.createSession(Utils.convertSessionToJson(sessionRequest)).then().extract().as(SessionResponse.class);

        SessionRequest sessionUpdateRequest = sessionBuilder.update_SessionUrnNumberNegative();
        sessionService.updateSession(Utils.convertSessionToJson(sessionUpdateRequest),sessionResponse.getSessionId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString("deve ser maior ou igual a 0"))
                ;

        sessionService.deleteSession(sessionResponse.getSessionId());

        zoneService.deleteZone(zoneResponse.getZoneId());
    }

    @Test
    @Tag("all")
    @Description("Tentar atualizar uma seção inexistente")
    public void updateSessionIsNumberError(){

        SessionRequest sessionUpdateRequest = sessionBuilder.update_SessionUrnNumberIsOk();
        sessionService.updateSession(Utils.convertSessionToJson(sessionUpdateRequest),99999999999999L)
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body(containsString("A seção não existe."))
                ;
    }
}
