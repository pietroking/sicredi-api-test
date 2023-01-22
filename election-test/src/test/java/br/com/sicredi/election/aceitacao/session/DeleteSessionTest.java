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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Seção")
@Epic("Deletar seções")
@Feature("Seção")
public class DeleteSessionTest extends BaseTest {
    ZoneService zoneService = new ZoneService();
    ZoneBuilder zoneBuilder = new ZoneBuilder();
    SessionService sessionService = new SessionService();
    SessionBuilder sessionBuilder = new SessionBuilder();

    @Test
    @Tag("all")
    @Description("Deve deletar uma seção com sucesso")
    public void deleteSessionIsOk(){
        ZoneRequest zoneRequest = zoneBuilder.create_ZoneIsOk();
        ZoneResponse zoneResponse = zoneService.createZone(Utils.convertZoneToJson(zoneRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .extract().as(ZoneResponse.class)
                ;

        SessionRequest sessionRequest = sessionBuilder.create_SessionIsOk(zoneResponse.getZoneId());
        SessionResponse sessionResponse = sessionService.createSession(Utils.convertSessionToJson(sessionRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .extract().as(SessionResponse.class)
                ;

        SessionResponse[] listSession = sessionService.findIdZone(zoneResponse.getZoneId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(SessionResponse[].class)
                ;
        assertNotNull(listSession);

        sessionService.deleteSession(sessionResponse.getSessionId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NO_CONTENT)
                ;
        String message = sessionService.findIdZone(zoneResponse.getZoneId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .extract().path("message")
                ;
        assertEquals("Não existem seções nesta zona.",message);

        zoneService.deleteZone(zoneResponse.getZoneId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NO_CONTENT)
        ;
    }

    @Test
    @Tag("all")
    @Description("Tentar deletar uma seção inexistente")
    public void deleteSessionIsError(){
        String message = sessionService.deleteSession(99999999999999L)
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .extract().path("message")
        ;
        assertEquals("A seção não existe.",message);
    }
}
