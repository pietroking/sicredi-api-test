package br.com.sicredi.election.aceitacao.voter;

import br.com.sicredi.election.aceitacao.base.BaseTest;
import br.com.sicredi.election.builder.SessionBuilder;
import br.com.sicredi.election.builder.VoterBuilder;
import br.com.sicredi.election.builder.ZoneBuilder;
import br.com.sicredi.election.dto.session.SessionRequest;
import br.com.sicredi.election.dto.session.SessionResponse;
import br.com.sicredi.election.dto.voter.VoterRequest;
import br.com.sicredi.election.dto.voter.VoterResponse;
import br.com.sicredi.election.dto.zone.ZoneRequest;
import br.com.sicredi.election.dto.zone.ZoneResponse;
import br.com.sicredi.election.service.SessionService;
import br.com.sicredi.election.service.VoterService;
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

@DisplayName("Eleitor")
@Epic("Cadastrar eleitores")
@Feature("Eleitor")
public class PostVoterTest extends BaseTest {
    VoterService voterService = new VoterService();
    VoterBuilder voterBuilder = new VoterBuilder();
    ZoneService zoneService = new ZoneService();
    ZoneBuilder zoneBuilder = new ZoneBuilder();
    SessionService sessionService = new SessionService();
    SessionBuilder sessionBuilder = new SessionBuilder();

    @Test
    @Tag("all")
    @Description("Deve cadastrar um elitor com sucesso")
    public void create_WhenVoterRequestIsOk_ThenVoterCreateSuccessfully(){
        ZoneRequest zoneRequest = zoneBuilder.create_ZoneIsOk();
        ZoneResponse zoneResponse = zoneService.createZone(Utils.convertZoneToJson(zoneRequest)).then().extract().as(ZoneResponse.class);

        SessionRequest sessionRequest = sessionBuilder.create_SessionIsOk(zoneResponse.getZoneId());
        SessionResponse sessionResponse = sessionService.createSession(Utils.convertSessionToJson(sessionRequest)).then().extract().as(SessionResponse.class);

        VoterRequest voterRequest = voterBuilder.create_VoterIsOk(sessionResponse.getSessionId());
        VoterResponse voterResponse = voterService.createVoter(Utils.convertVoterToJson(voterRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .body("sessionId",is(sessionResponse.getSessionId()))
                .body("name",is(voterRequest.getName()))
                .body("cpf",is(voterRequest.getCpf()))
                .extract().as(VoterResponse.class)
                ;

        voterService.deleteVoter(voterResponse.getVoterId());

        sessionService.deleteSession(sessionResponse.getSessionId());

        zoneService.deleteZone(zoneResponse.getZoneId());
    }

    @Test
    @Tag("all")
    @Description("Tentar cadastrar um eleitor com vazio")
    public void create_WhenVoterRequestIsEmpty_ThenReturnMessageNullError(){

        VoterRequest voterRequest = voterBuilder.create_VoterEmpty();
        voterService.createVoter(Utils.convertVoterToJson(voterRequest))
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
    @Description("Tentar cadastrar um eleitor em uma seção inexistente")
    public void create_WhenVoterRequestIsSessionInvalid_ThenReturnMessageSessionNotExist(){

        VoterRequest voterRequest = voterBuilder.create_VoterSessionIdError();
        voterService.createVoter(Utils.convertVoterToJson(voterRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body(containsString("A seção não existe."))
                ;

    }

    @Test
    @Tag("all")
    @Description("Tentar cadastrar um eleitor com cpf invalido")
    public void create_WhenVoterRequestIsCpfInvalid_ThenReturnMessageCpfInvalid(){

        VoterRequest voterRequest = voterBuilder.create_VoterCpfInvalid();
        voterService.createVoter(Utils.convertVoterToJson(voterRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString("O CPF está inválido"))
                ;
    }
}
