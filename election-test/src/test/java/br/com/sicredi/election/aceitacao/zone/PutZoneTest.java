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

import static org.junit.jupiter.api.Assertions.assertEquals;
@DisplayName("Zona")
@Epic("Atualizar zona")
@Feature("Zona")
public class PutZoneTest extends BaseTest {
    ZoneService zoneService = new ZoneService();
    ZoneBuilder zoneBuilder = new ZoneBuilder();

    @Test
    @Tag("all")
    @Description("Deve atualizar uma zona com sucesso")
    public void updadeZoneIsOk(){
        ZoneRequest zoneRequest = zoneBuilder.create_ZoneIsOk();
        ZoneResponse zoneResponse = zoneService.createZone(Utils.convertZoneToJson(zoneRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .extract().as(ZoneResponse.class)
                ;

        ZoneRequest zoneUpdateRequest = zoneBuilder.update_ZoneIsOk();
        ZoneResponse zoneUpdate = zoneService.updateZone(Utils.convertZoneToJson(zoneUpdateRequest), zoneResponse.getZoneId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(ZoneResponse.class)
                ;
        assertEquals(zoneResponse.getZoneId(),zoneUpdate.getZoneId());
        assertEquals(zoneUpdateRequest.getNumber(),zoneUpdate.getNumber());

        zoneService.deleteZone(zoneResponse.getZoneId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NO_CONTENT)
        ;
    }

    @Test
    @Tag("all")
    @Description("Tentar atualizar uma zona com numero já existente")
    public void updadeZoneIsNumberExist(){
        ZoneRequest zoneRequest = zoneBuilder.create_ZoneIsOk();
        ZoneResponse zoneResponse = zoneService.createZone(Utils.convertZoneToJson(zoneRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .extract().as(ZoneResponse.class)
                ;

        String message = zoneService.updateZone(Utils.convertZoneToJson(zoneRequest), zoneResponse.getZoneId())
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
    @Description("Tentar atualizar uma zona com numero vazio")
    public void updadeZoneIsNull(){
        ZoneRequest zoneRequest = zoneBuilder.create_ZoneIsOk();
        ZoneResponse zoneResponse = zoneService.createZone(Utils.convertZoneToJson(zoneRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .extract().as(ZoneResponse.class)
                ;

        ZoneRequest zoneUpdate = zoneBuilder.create_ZoneEmpty();
        String description = zoneService.updateZone(Utils.convertZoneToJson(zoneUpdate), zoneResponse.getZoneId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract().path("description")
                ;
        assertEquals("[O number não pode ser nulo]", description);

        zoneService.deleteZone(zoneResponse.getZoneId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NO_CONTENT)
        ;
    }

    @Test
    @Tag("all")
    @Description("Tentar atualizar uma zona com numero negativo")
    public void updadeZoneIsNegativeNumber(){
        ZoneRequest zoneRequest = zoneBuilder.create_ZoneIsOk();
        ZoneResponse zoneResponse = zoneService.createZone(Utils.convertZoneToJson(zoneRequest))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .extract().as(ZoneResponse.class)
                ;

        ZoneRequest zoneUpdate = zoneBuilder.create_ZoneNegativeNumber();
        String description = zoneService.updateZone(Utils.convertZoneToJson(zoneUpdate), zoneResponse.getZoneId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract().path("description")
                ;
        assertEquals("[deve ser maior ou igual a 0]", description);

        zoneService.deleteZone(zoneResponse.getZoneId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NO_CONTENT)
        ;
    }

    @Test
    @Tag("all")
    @Description("Tentar atualizar uma zona com id inexistente")
    public void updadeZoneIsZoneIdError(){
        ZoneRequest zoneRequest = zoneBuilder.create_ZoneIsOk();
        String message = zoneService.updateZone(Utils.convertZoneToJson(zoneRequest), 99999999999999L)
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .extract().path("message")
                ;
        assertEquals("A zona não existe.",message);
    }
}
