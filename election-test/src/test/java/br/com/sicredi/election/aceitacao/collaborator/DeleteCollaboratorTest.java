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
@Epic("Deletar colaboradores")
@Feature("Colaborador")
public class DeleteCollaboratorTest extends BaseTest {
    CollaboratorService collaboratorService = new CollaboratorService();
    CollaboratorBuilder collaboratorBuilder = new CollaboratorBuilder();
    ZoneService zoneService = new ZoneService();
    ZoneBuilder zoneBuilder = new ZoneBuilder();
    SessionService sessionService = new SessionService();
    SessionBuilder sessionBuilder = new SessionBuilder();

    @Test
    @Tag("all")
    @Description("Deve deletar um colaborador com sucesso")
    public void deleteCollaboratorIsOk(){
        ZoneRequest zoneRequest = zoneBuilder.create_ZoneIsOk();
        ZoneResponse zoneResponse = zoneService.createZone(Utils.convertZoneToJson(zoneRequest)).then().extract().as(ZoneResponse.class);

        SessionRequest sessionRequest = sessionBuilder.create_SessionIsOk(zoneResponse.getZoneId());
        SessionResponse sessionResponse = sessionService.createSession(Utils.convertSessionToJson(sessionRequest)).then().extract().as(SessionResponse.class);

        CollaboratorRequest collaboratorRequest = collaboratorBuilder.create_CollaboratorIsOk(sessionResponse.getSessionId());
        CollaboratorResponse collaboratorResponse = collaboratorService.createCollaborator(Utils.convertCollaboratorToJson(collaboratorRequest)).then().extract().as(CollaboratorResponse.class);

        collaboratorService.deleteCollaborator(collaboratorResponse.getCollaboratorId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NO_CONTENT)
        ;

        collaboratorService.findIdSession(sessionResponse.getSessionId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body(containsString("Não existem colaboradores nesta seção."))
                ;

        sessionService.deleteSession(sessionResponse.getSessionId());

        zoneService.deleteZone(zoneResponse.getZoneId());
    }

    @Test
    @Tag("all")
    @Description("Tentar deletar um colaborador inexistente")
    public void deleteCollaboratorIsError(){

        collaboratorService.deleteCollaborator(99999999999999L)
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body(containsString("O colaborador não existe."))
                ;
    }
}
