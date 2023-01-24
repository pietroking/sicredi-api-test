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
import static org.hamcrest.Matchers.is;
@DisplayName("Zona")
@Epic("Atualizar zona")
@Feature("Zona")
public class PutZoneTest extends BaseTest {
    ZoneService zoneService = new ZoneService();
    ZoneBuilder zoneBuilder = new ZoneBuilder();

    @Test
    @Tag("all")
    @Description("Deve atualizar uma zona com sucesso")
    public void update_WhenZoneUpdateRequestIsOk_ThenZoneUpdateSuccessfully(){
        ZoneRequest zoneRequest = zoneBuilder.create_ZoneIsOk();
        ZoneResponse zoneResponse = zoneService.createZone(Utils.convertZoneToJson(zoneRequest)).then().extract().as(ZoneResponse.class);

        ZoneRequest zoneUpdateRequest = zoneBuilder.update_ZoneIsOk();
        zoneService.updateZone(Utils.convertZoneToJson(zoneUpdateRequest), zoneResponse.getZoneId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("zoneId",is(zoneResponse.getZoneId()))
                .body("number",is(zoneUpdateRequest.getNumber()))
                ;

        zoneService.deleteZone(zoneResponse.getZoneId());
    }

    @Test
    @Tag("all")
    @Description("Tentar atualizar uma zona com numero já existente")
    public void update_WhenZoneNumberExist_ThenReturnMessageZoneNumberIsExist(){
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
    public void update_WhenZoneUpdateRequestIsEmpty_ThenReturnMessageNullError(){
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
    public void update_WhenZoneUpdateRequestIsNumberInvalid_ThenReturnMessageNumberInvalid(){
        ZoneRequest zoneRequest = zoneBuilder.create_ZoneIsOk();
        ZoneResponse zoneResponse = zoneService.createZone(Utils.convertZoneToJson(zoneRequest)).then().extract().as(ZoneResponse.class);

        ZoneRequest zoneUpdate = zoneBuilder.create_ZoneNegativeNumber();
        zoneService.updateZone(Utils.convertZoneToJson(zoneUpdate), zoneResponse.getZoneId())
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString("O number não pode ser negativo"))
                ;

        zoneService.deleteZone(zoneResponse.getZoneId());
    }

    @Test
    @Tag("all")
    @Description("Tentar atualizar uma zona com id inexistente")
    public void update_WhenZoneIdIsInvalid_ThenReturnMessageZoneNotExist(){
        ZoneRequest zoneRequest = zoneBuilder.create_ZoneIsOk();
        zoneService.updateZone(Utils.convertZoneToJson(zoneRequest), 999999999)
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body(containsString("A zona não existe."))
                ;
    }
}
