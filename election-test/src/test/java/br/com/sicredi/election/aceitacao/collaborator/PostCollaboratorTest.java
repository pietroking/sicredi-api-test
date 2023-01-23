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
@Epic("Cadastrar colaboradores")
@Feature("Colaborador")
public class PostCollaboratorTest extends BaseTest {
    CollaboratorService collaboratorService = new CollaboratorService();
    CollaboratorBuilder collaboratorBuilder = new CollaboratorBuilder();
    ZoneService zoneService = new ZoneService();
    ZoneBuilder zoneBuilder = new ZoneBuilder();
    SessionService sessionService = new SessionService();
    SessionBuilder sessionBuilder = new SessionBuilder();

    @Test
    @Tag("all")
    @Description("Deve cadastrar um colaborador com sucesso")
    public void createCollaboratorIsOk(){
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

        CollaboratorRequest collaboratorRequest = collaboratorBuilder.create_CollaboratorIsOk(sessionResponse.getSessionId());
        CollaboratorResponse collaboratorResponse = collaboratorService.createCollaborator(Utils.convertCollaboratorToJson(collaboratorRequest))
            .then()
            .log().all()
            .statusCode(HttpStatus.SC_CREATED)
            .extract().as(CollaboratorResponse.class)
            ;
        assertEquals(sessionResponse.getSessionId(),collaboratorResponse.getSessionId());
        assertEquals(collaboratorRequest.getName(),collaboratorResponse.getName());
        assertEquals(collaboratorRequest.getCpf(),collaboratorResponse.getCpf());

        collaboratorService.deleteCollaborator(collaboratorResponse.getCollaboratorId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NO_CONTENT)
        ;

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
    @Description("Tentar cadastrar um colaborador com vazio")
    public void createCollaboratorIsEmpty(){

        CollaboratorRequest collaboratorRequest = collaboratorBuilder.create_CollaboratorEmpty();
        collaboratorService.createCollaborator(Utils.convertCollaboratorToJson(collaboratorRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString("O sessionId não pode ser nulo"))
                .body(containsString("O cpf não pode ser nulo"))
                .body(containsString("O nome não pode ser nulo"))
        ;
    }

    @Test
    @Tag("all")
    @Description("Tentar cadastrar um colaborado em uma seção inexistente")
    public void createCollaboratorIsSessionError(){

        CollaboratorRequest collaboratorRequest = collaboratorBuilder.create_CollaboratorSessionIdError();
        String message = collaboratorService.createCollaborator(Utils.convertCollaboratorToJson(collaboratorRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .extract().path("message")
                ;
        assertEquals("A seção não existe.",message);
    }

    @Test
    @Tag("all")
    @Description("Tentar cadastrar um colaborado com cpf invalido")
    public void createCollaboratorIsCpfError(){

        CollaboratorRequest collaboratorRequest = collaboratorBuilder.create_CollaboratorCpfInvalid();
        String description = collaboratorService.createCollaborator(Utils.convertCollaboratorToJson(collaboratorRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract().path("description")
                ;
        assertEquals("[O CPF está inválido]",description);
    }
}
