package br.com.sicredi.election.aceitacao.collaborator;

import br.com.sicredi.election.aceitacao.base.BaseTest;
import br.com.sicredi.election.builder.CollaboratorBuilder;
import br.com.sicredi.election.builder.SessionBuilder;
import br.com.sicredi.election.builder.ZoneBuilder;
import br.com.sicredi.election.dto.collaborator.CollaboratorRequest;
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
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Colaborador")
@Epic("Atualizar colaboradores")
@Feature("Colaborador")
public class PatchCollaboratorTest extends BaseTest {
    CollaboratorService collaboratorService = new CollaboratorService();
    CollaboratorBuilder collaboratorBuilder = new CollaboratorBuilder();
    ZoneService zoneService = new ZoneService();
    ZoneBuilder zoneBuilder = new ZoneBuilder();
    SessionService sessionService = new SessionService();
    SessionBuilder sessionBuilder = new SessionBuilder();

    @Test
    @Tag("all")
    @Description("Deve atualizar um colaborador com sucesso")
    public void updateCollaboratorIsOk(){
        ZoneRequest zoneRequest = zoneBuilder.create_ZoneIsOk();
        ZoneResponse zoneResponse = zoneService.createZone(Utils.convertZoneToJson(zoneRequest)).then().extract().as(ZoneResponse.class);

        SessionRequest sessionRequest = sessionBuilder.create_SessionIsOk(zoneResponse.getZoneId());
        SessionResponse sessionResponse = sessionService.createSession(Utils.convertSessionToJson(sessionRequest)).then().extract().as(SessionResponse.class);

        SessionRequest sessionRequest2 = sessionBuilder.create_SessionIsOk2(zoneResponse.getZoneId());
        SessionResponse sessionResponse2 = sessionService.createSession(Utils.convertSessionToJson(sessionRequest2)).then().extract().as(SessionResponse.class);

        CollaboratorRequest collaboratorRequest = collaboratorBuilder.create_CollaboratorIsOk(sessionResponse.getSessionId());
        CollaboratorResponse collaboratorResponse = collaboratorService.createCollaborator(Utils.convertCollaboratorToJson(collaboratorRequest)).then().extract().as(CollaboratorResponse.class);

        CollaboratorRequest collaboratorUpdateRequest = collaboratorBuilder.create_CollaboratorIsOk(sessionResponse2.getSessionId());
        CollaboratorResponse collaboratorUpdate = collaboratorService.updateCollaborator(Utils.convertCollaboratorToJson(collaboratorUpdateRequest),collaboratorResponse.getCollaboratorId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(CollaboratorResponse.class)
                ;
        assertEquals(collaboratorResponse.getCollaboratorId(),collaboratorUpdate.getCollaboratorId());
        assertEquals(sessionResponse2.getSessionId(),collaboratorUpdate.getSessionId());
        assertEquals(collaboratorResponse.getName(),collaboratorUpdate.getName());
        assertEquals(collaboratorResponse.getCpf(),collaboratorUpdate.getCpf());

        collaboratorService.deleteCollaborator(collaboratorResponse.getCollaboratorId());

        sessionService.deleteSession(sessionResponse.getSessionId());

        sessionService.deleteSession(sessionResponse2.getSessionId());

        zoneService.deleteZone(zoneResponse.getZoneId());
    }

    @Test
    @Tag("all")
    @Description("Tentar atualizar um colaborador passando seção vazia")
    public void updateCollaboratorIsSessionEmpty(){
        ZoneRequest zoneRequest = zoneBuilder.create_ZoneIsOk();
        ZoneResponse zoneResponse = zoneService.createZone(Utils.convertZoneToJson(zoneRequest)).then().extract().as(ZoneResponse.class);

        SessionRequest sessionRequest = sessionBuilder.create_SessionIsOk(zoneResponse.getZoneId());
        SessionResponse sessionResponse = sessionService.createSession(Utils.convertSessionToJson(sessionRequest)).then().extract().as(SessionResponse.class);

        CollaboratorRequest collaboratorRequest = collaboratorBuilder.create_CollaboratorIsOk(sessionResponse.getSessionId());
        CollaboratorResponse collaboratorResponse = collaboratorService.createCollaborator(Utils.convertCollaboratorToJson(collaboratorRequest)).then().extract().as(CollaboratorResponse.class);

        CollaboratorRequest collaboratorUpdateRequest = collaboratorBuilder.update_CollaboratorIsSessionEmpty();
        collaboratorService.updateCollaborator(Utils.convertCollaboratorToJson(collaboratorUpdateRequest),collaboratorResponse.getCollaboratorId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString("O sessionId não pode ser nulo"))
                ;

        collaboratorService.deleteCollaborator(collaboratorResponse.getCollaboratorId());

        sessionService.deleteSession(sessionResponse.getSessionId());

        zoneService.deleteZone(zoneResponse.getZoneId());
    }

    @Test
    @Tag("all")
    @Description("Tentar atualizar um colaborador passando seção não existente")
    public void updateCollaboratorIsSessionError(){
        ZoneRequest zoneRequest = zoneBuilder.create_ZoneIsOk();
        ZoneResponse zoneResponse = zoneService.createZone(Utils.convertZoneToJson(zoneRequest)).then().extract().as(ZoneResponse.class);

        SessionRequest sessionRequest = sessionBuilder.create_SessionIsOk(zoneResponse.getZoneId());
        SessionResponse sessionResponse = sessionService.createSession(Utils.convertSessionToJson(sessionRequest)).then().extract().as(SessionResponse.class);

        CollaboratorRequest collaboratorRequest = collaboratorBuilder.create_CollaboratorIsOk(sessionResponse.getSessionId());
        CollaboratorResponse collaboratorResponse = collaboratorService.createCollaborator(Utils.convertCollaboratorToJson(collaboratorRequest)).then().extract().as(CollaboratorResponse.class);

        CollaboratorRequest collaboratorUpdateRequest = collaboratorBuilder.update_CollaboratorIsSessionInvalid();
        collaboratorService.updateCollaborator(Utils.convertCollaboratorToJson(collaboratorUpdateRequest),collaboratorResponse.getCollaboratorId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body(containsString("A seção não existe."))
                ;

        collaboratorService.deleteCollaborator(collaboratorResponse.getCollaboratorId());

        sessionService.deleteSession(sessionResponse.getSessionId());

        zoneService.deleteZone(zoneResponse.getZoneId());
    }
}
