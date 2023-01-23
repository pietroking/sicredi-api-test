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

import static org.hamcrest.Matchers.containsString;
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
        ZoneResponse zoneResponse = zoneService.createZone(Utils.convertZoneToJson(zoneRequest)).then().extract().as(ZoneResponse.class);

        ZoneRequest zoneUpdateRequest = zoneBuilder.update_ZoneIsOk();
        ZoneResponse zoneUpdate = zoneService.updateZone(Utils.convertZoneToJson(zoneUpdateRequest), zoneResponse.getZoneId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(ZoneResponse.class)
                ;
        assertEquals(zoneResponse.getZoneId(),zoneUpdate.getZoneId());
        assertEquals(zoneUpdateRequest.getNumber(),zoneUpdate.getNumber());

        zoneService.deleteZone(zoneResponse.getZoneId());
    }

    @Test
    @Tag("all")
    @Description("Tentar atualizar uma zona com numero já existente")
    public void updadeZoneIsNumberExist(){
        ZoneRequest zoneRequest = zoneBuilder.create_ZoneIsOk();
        ZoneResponse zoneResponse = zoneService.createZone(Utils.convertZoneToJson(zoneRequest)).then().extract().as(ZoneResponse.class);

        zoneService.updateZone(Utils.convertZoneToJson(zoneRequest), zoneResponse.getZoneId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString("Número da zona já existe."))
                ;

        zoneService.deleteZone(zoneResponse.getZoneId());
    }

    @Test
    @Tag("all")
    @Description("Tentar atualizar uma zona com numero vazio")
    public void updadeZoneIsNull(){
        ZoneRequest zoneRequest = zoneBuilder.create_ZoneIsOk();
        ZoneResponse zoneResponse = zoneService.createZone(Utils.convertZoneToJson(zoneRequest)).then().extract().as(ZoneResponse.class);

        ZoneRequest zoneUpdate = zoneBuilder.create_ZoneEmpty();
        zoneService.updateZone(Utils.convertZoneToJson(zoneUpdate), zoneResponse.getZoneId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString("O number não pode ser nulo"))
                ;

        zoneService.deleteZone(zoneResponse.getZoneId());
    }

    @Test
    @Tag("all")
    @Description("Tentar atualizar uma zona com numero negativo")
    public void updadeZoneIsNegativeNumber(){
        ZoneRequest zoneRequest = zoneBuilder.create_ZoneIsOk();
        ZoneResponse zoneResponse = zoneService.createZone(Utils.convertZoneToJson(zoneRequest)).then().extract().as(ZoneResponse.class);

        ZoneRequest zoneUpdate = zoneBuilder.create_ZoneNegativeNumber();
        zoneService.updateZone(Utils.convertZoneToJson(zoneUpdate), zoneResponse.getZoneId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString("deve ser maior ou igual a 0"))
                ;

        zoneService.deleteZone(zoneResponse.getZoneId());
    }

    @Test
    @Tag("all")
    @Description("Tentar atualizar uma zona com id inexistente")
    public void updadeZoneIsZoneIdError(){
        ZoneRequest zoneRequest = zoneBuilder.create_ZoneIsOk();
        zoneService.updateZone(Utils.convertZoneToJson(zoneRequest), 99999999999999L)
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body(containsString("A zona não existe."))
                ;
    }
}
