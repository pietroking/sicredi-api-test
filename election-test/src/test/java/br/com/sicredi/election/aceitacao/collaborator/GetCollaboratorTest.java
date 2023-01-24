package br.com.sicredi.election.aceitacao.collaborator;

import br.com.sicredi.election.aceitacao.base.BaseTest;
import br.com.sicredi.election.builder.SessionBuilder;
import br.com.sicredi.election.builder.ZoneBuilder;
import br.com.sicredi.election.dto.collaborator.CollaboratorResponse;
import br.com.sicredi.election.dto.session.SessionRequest;
import br.com.sicredi.election.dto.session.SessionResponse;
import br.com.sicredi.election.dto.zone.ZoneRequest;
import br.com.sicredi.election.dto.zone.ZoneResponse;
import br.com.sicredi.election.service.CollaboratorService;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Colaborador")
@Epic("Listar colaboradores")
@Feature("Colaborador")
public class GetCollaboratorTest extends BaseTest {
    CollaboratorService collaboratorService = new CollaboratorService();
    ZoneService zoneService = new ZoneService();
    ZoneBuilder zoneBuilder = new ZoneBuilder();
    SessionService sessionService = new SessionService();
    SessionBuilder sessionBuilder = new SessionBuilder();

    @Test
    @Tag("all")
    @Description("Deve listar colaboradores registrados")
    public void findAllCollaborator_WhenCollaborator_ThenReturnListOfCollaboratorCreated(){
        CollaboratorResponse[] listCollaborator = collaboratorService.findAll()
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(CollaboratorResponse[].class)
                ;
        assertNotNull(listCollaborator);
    }

    @Test
    @Tag("all")
    @Description("Deve listar colaboradores registrados em uma determinada seção")
    public void findCollaboratorBySession_WhenSessionHasCollaborator_ThenReturnListOfSessionCollaborator(){
        CollaboratorResponse[] listCollaborator = collaboratorService.findIdSession(8)
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(CollaboratorResponse[].class)
                ;
        assertNotNull(listCollaborator);
    }

    @Test
    @Tag("all")
    @Description("Tentar listar colaboradores em uma seção inexistente")
    public void findCollaboratorBySession_WhenSessionInvalid_ThenReturnMessageSessionIsNotExist(){
        collaboratorService.findIdSession(999999999)
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body(containsString("A seção não existe."))
                ;
    }

    @Test
    @Tag("all")
    @Description("Tentar listar colaboradores em uma seção sem colaboradores")
    public void findCollaboratorBySession_WhenSessionHasNoCollaborator_ThenReturnMessageSessionWithoutCollaborador(){
        ZoneRequest zoneRequest = zoneBuilder.create_ZoneIsOk();
        ZoneResponse zoneResponse = zoneService.createZone(Utils.convertZoneToJson(zoneRequest)).then().extract().as(ZoneResponse.class);

        SessionRequest sessionRequest = sessionBuilder.create_SessionIsOk(zoneResponse.getZoneId());
        SessionResponse sessionResponse = sessionService.createSession(Utils.convertSessionToJson(sessionRequest)).then().extract().as(SessionResponse.class);

        collaboratorService.findIdSession(sessionResponse.getSessionId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body(containsString("Não existem colaboradores nesta seção."))
                ;

        sessionService.deleteSession(sessionResponse.getSessionId());

        zoneService.deleteZone(zoneResponse.getZoneId());
    }
}
