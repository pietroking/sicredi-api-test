package br.com.sicredi.election.aceitacao.voter;

import br.com.sicredi.election.aceitacao.base.BaseTest;
import br.com.sicredi.election.dto.voter.VoterResponse;
import br.com.sicredi.election.service.VoterService;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Eleitor")
@Epic("Listar eleitores")
@Feature("Eleitor")
public class GetVoterTest extends BaseTest {
    VoterService voterService = new VoterService();

    @Test
    @Tag("all")
    @Description("Deve listar colaboradores registrados")
    public void findAllCollaboratorIsOk(){
        VoterResponse[] listVoter = voterService.findAll()
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(VoterResponse[].class)
                ;
        assertNotNull(listVoter);
    }
}
