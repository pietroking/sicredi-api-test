package br.com.sicredi.election.aceitacao.session;

import br.com.sicredi.election.aceitacao.base.BaseTest;
import br.com.sicredi.election.builder.ZoneBuilder;
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
@Epic("Listar seções")
@Feature("Seção")
public class GetSessionTest extends BaseTest {
    SessionService sessionService = new SessionService();
    ZoneService zoneService = new ZoneService();
    ZoneBuilder zoneBuilder = new ZoneBuilder();

    @Test
    @Tag("all")
    @Description("Deve listar seções registradas")
    public void findAllSessionIsOk(){
        SessionResponse[] listSession = sessionService.findAll()
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(SessionResponse[].class)
                ;
        assertNotNull(listSession);
    }

    @Test
    @Tag("all")
    @Description("Deve listar seções registradas de uma determinada zona")
    public void findSessionByZoneIsOk(){
        SessionResponse[] listSession = sessionService.findIdZone(1L)
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(SessionResponse[].class)
                ;
        assertNotNull(listSession);
    }

    @Test
    @Tag("all")
    @Description("Tentar listar seções registradas de uma zona inexistente")
    public void findSessionByZoneIsError(){
        String message = sessionService.findIdZone(99999999999999L)
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .extract().path("message")
                ;
        assertEquals("A zona não existe.",message);
    }

    @Test
    @Tag("all")
    @Description("Tentar listar seções de uma zona sem seções")
    public void findSessionByZoneSessionIsEmpty(){
        ZoneRequest zoneRequest = zoneBuilder.create_ZoneIsOk();
        ZoneResponse zoneResponse = zoneService.createZone(Utils.convertZoneToJson(zoneRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .extract().as(ZoneResponse.class)
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

}
