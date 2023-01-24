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
import static org.hamcrest.Matchers.is;

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
    public void create_WhenCollaboratorRequestIsOk_ThenCollaboratorCreateSuccessfully(){
        ZoneRequest zoneRequest = zoneBuilder.create_ZoneIsOk();
        ZoneResponse zoneResponse = zoneService.createZone(Utils.convertZoneToJson(zoneRequest)).then().extract().as(ZoneResponse.class);

        SessionRequest sessionRequest = sessionBuilder.create_SessionIsOk(zoneResponse.getZoneId());
        SessionResponse sessionResponse = sessionService.createSession(Utils.convertSessionToJson(sessionRequest)).then().extract().as(SessionResponse.class);

        CollaboratorRequest collaboratorRequest = collaboratorBuilder.create_CollaboratorIsOk(sessionResponse.getSessionId());
        CollaboratorResponse collaboratorResponse = collaboratorService.createCollaborator(Utils.convertCollaboratorToJson(collaboratorRequest))
            .then()
            .log().all()
            .statusCode(HttpStatus.SC_CREATED)
            .body("sessionId",is(sessionResponse.getSessionId()))
            .body("name",is(collaboratorRequest.getName()))
            .body("cpf",is(collaboratorRequest.getCpf()))
            .extract().as(CollaboratorResponse.class)
            ;

        collaboratorService.deleteCollaborator(collaboratorResponse.getCollaboratorId());

        sessionService.deleteSession(sessionResponse.getSessionId());

        zoneService.deleteZone(zoneResponse.getZoneId());
    }

    @Test
    @Tag("all")
    @Description("Tentar cadastrar um colaborador com vazio")
    public void create_WhenCollaboratorRequestIsEmpty_ThenReturnMessageNullError(){

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
    public void create_WhenCollaboratorRequestIsSessionInvalid_ThenReturnMessageSessionNotExist(){

        CollaboratorRequest collaboratorRequest = collaboratorBuilder.create_CollaboratorSessionIdError();
        collaboratorService.createCollaborator(Utils.convertCollaboratorToJson(collaboratorRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body(containsString("A seção não existe."))
                ;
    }

    @Test
    @Tag("all")
    @Description("Tentar cadastrar um colaborado com cpf invalido")
    public void create_WhenCollaboratorRequestIsCpfInvalid_ThenReturnMessageCpfInvalid(){

        CollaboratorRequest collaboratorRequest = collaboratorBuilder.create_CollaboratorCpfInvalid();
        collaboratorService.createCollaborator(Utils.convertCollaboratorToJson(collaboratorRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString("O CPF está inválido"))
                ;
    }
}
