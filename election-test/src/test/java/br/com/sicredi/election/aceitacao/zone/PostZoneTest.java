package br.com.sicredi.election.aceitacao.zone;

import br.com.sicredi.election.aceitacao.base.BaseTest;
import br.com.sicredi.election.builder.ZoneBuilder;
import br.com.sicredi.election.dto.zone.ZoneRequest;
import br.com.sicredi.election.dto.zone.ZoneResponse;
import br.com.sicredi.election.service.ZoneService;
import br.com.sicredi.election.utils.Utils;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Zona")
@Epic("Cadastro zona")
@Feature("Zona")
public class PostZoneTest extends BaseTest {
    ZoneService zoneService = new ZoneService();
    ZoneBuilder zoneBuilder = new ZoneBuilder();

    @Test
    @Tag("all")
    @Description("Deve cadastrar uma zona com sucesso")
    public void createZoneIsOk(){
        ZoneRequest zoneRequest = zoneBuilder.create_ZoneIsOk();
        ZoneResponse zoneResponse = zoneService.createZone(Utils.convertZoneToJson(zoneRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .extract().as(ZoneResponse.class)
                ;
        assertEquals(zoneRequest.getNumber(), zoneResponse.getNumber());

        zoneService.deleteZone(zoneResponse.getZoneId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NO_CONTENT)
                ;
    }

    @Test
    @Tag("all")
    @Description("Tentar cadastrar uma zona com o numero ja existente")
    public void createZoneIsNumberExist(){
        ZoneRequest zoneRequest = zoneBuilder.create_ZoneIsOk();
        ZoneResponse zoneResponse = zoneService.createZone(Utils.convertZoneToJson(zoneRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .extract().as(ZoneResponse.class)
                ;
        String message = zoneService.createZone(Utils.convertZoneToJson(zoneRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract().path("message")
                ;
        assertEquals("Número da zona já existe.",message);


        zoneService.deleteZone(zoneResponse.getZoneId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NO_CONTENT)
        ;
    }

    @Test
    @Tag("all")
    @Description("Tentar cadastrar uma zona sem numero")
    public void createZoneIsNumberNull(){
        ZoneRequest zoneRequest = zoneBuilder.create_ZoneEmpty();
        String description = zoneService.createZone(Utils.convertZoneToJson(zoneRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract().path("description")
                ;
        assertEquals("[O number não pode ser nulo]", description);
    }

    @Test
    @Tag("all")
    @Description("Tentar cadastrar uma zona com numero negativo")
    public void createZoneIsNegativeNumber(){
        ZoneRequest zoneRequest = zoneBuilder.create_ZoneNegativeNumber();
        String description = zoneService.createZone(Utils.convertZoneToJson(zoneRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract().path("description")
                ;
        assertEquals("[deve ser maior ou igual a 0]", description);
    }
}
