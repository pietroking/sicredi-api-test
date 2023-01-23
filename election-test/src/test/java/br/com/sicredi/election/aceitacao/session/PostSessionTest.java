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
@Epic("Cadastrar seções")
@Feature("Seção")
public class PostSessionTest extends BaseTest {
    ZoneService zoneService = new ZoneService();
    ZoneBuilder zoneBuilder = new ZoneBuilder();
    SessionService sessionService = new SessionService();
    SessionBuilder sessionBuilder = new SessionBuilder();

    @Test
    @Tag("all")
    @Description("Deve cadastrar uma seção com sucesso")
    public void createSessionIsOk(){
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
        assertEquals(zoneResponse.getZoneId(),sessionResponse.getZoneId());
        assertEquals(sessionRequest.getNumber(),sessionResponse.getNumber());
        assertEquals(sessionRequest.getUrnNumber(),sessionResponse.getUrnNumber());

        sessionService.deleteSession(sessionResponse.getSessionId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NO_CONTENT)
        ;

        zoneService.deleteZone(zoneResponse.getZoneId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NO_CONTENT)
        ;
    }

    @Test
    @Tag("all")
    @Description("Tentar cadastrar uma seção com vazia")
    public void createSessionIsEmpty(){

        SessionRequest sessionRequest = sessionBuilder.create_SessionEmpty();
        sessionService.createSession(Utils.convertSessionToJson(sessionRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString("O idZone não pode ser nulo"))
                .body(containsString("O number não pode ser nulo"))
                .body(containsString("O urnNumber não pode ser nulo"))
                ;
    }

    @Test
    @Tag("all")
    @Description("Tentar cadastrar uma seção em uma zona inexistente")
    public void createSessionIsZoneIsError(){

        SessionRequest sessionRequest = sessionBuilder.create_SessionIdZoneError();
        String message = sessionService.createSession(Utils.convertSessionToJson(sessionRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .extract().path("message")
                ;
        assertEquals("A zona não existe.",message);
    }

    @Test
    @Tag("all")
    @Description("Tentar cadastrar uma seção com numero ja existente")
    public void createSessionIsNumberExist(){

        SessionRequest sessionRequest = sessionBuilder.create_SessionNumberExist();
        String message = sessionService.createSession(Utils.convertSessionToJson(sessionRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract().path("message")
                ;
        assertEquals("Número da seção já existe.",message);
    }

    @Test
    @Tag("all")
    @Description("Tentar cadastrar uma seção com numero da urna ja existente")
    public void createSessionIsUrnNumberExist(){
        ZoneRequest zoneRequest = zoneBuilder.create_ZoneIsOk();
        ZoneResponse zoneResponse = zoneService.createZone(Utils.convertZoneToJson(zoneRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .extract().as(ZoneResponse.class)
                ;

        SessionRequest sessionRequest = sessionBuilder.create_SessionUrnNumberExist(zoneResponse.getZoneId());
        String message = sessionService.createSession(Utils.convertSessionToJson(sessionRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract().path("message")
                ;
        assertEquals("Número da urna já existe.",message);

        zoneService.deleteZone(zoneResponse.getZoneId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NO_CONTENT)
        ;
    }

    @Test
    @Tag("all")
    @Description("Tentar cadastrar uma seção e numero da urna negativos")
    public void createSessionIsNumberAndUrnNumberNegative(){

        SessionRequest sessionRequest = sessionBuilder.create_SessionNumberAndUrnNumberInvalid();
        String description = sessionService.createSession(Utils.convertSessionToJson(sessionRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract().path("description")
                ;
        assertEquals("[deve ser maior ou igual a 0, deve ser maior ou igual a 0]",description);
    }
}
