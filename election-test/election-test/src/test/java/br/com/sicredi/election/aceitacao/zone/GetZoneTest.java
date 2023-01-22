package br.com.sicredi.election.aceitacao.zone;

import br.com.sicredi.election.aceitacao.base.BaseTest;
import br.com.sicredi.election.dto.zone.ZoneResponse;
import br.com.sicredi.election.service.ZoneService;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Zona")
@Epic("Listar zonas")
@Feature("Zone")
public class GetZoneTest extends BaseTest {
    ZoneService zoneService = new ZoneService();

    @Test
    @Tag("all")
    @Description("Deve listar zonas registradas")
    public void findAllZoneIsOk(){
        ZoneResponse[] listZone = zoneService.findAll()
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(ZoneResponse[].class)
                ;
        assertNotNull(listZone);
    }
}
