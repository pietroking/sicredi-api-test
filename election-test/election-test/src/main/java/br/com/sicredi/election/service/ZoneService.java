package br.com.sicredi.election.service;

import br.com.sicredi.election.data.ZoneData;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ZoneService {
    public Response findAll(){
        return
                given()
                        .when()
                        .get(ZoneData.GET_ZONE)
                ;
    }

    public Response createZone(String zone){
        return
                given()
                        .contentType(ContentType.JSON)
                        .body(zone)
                        .when()
                        .post(ZoneData.POST_ZONE)
                ;
    }

    public Response updateZone(String zone, Long id){
        return
                given()
                        .pathParam("id", id)
                        .contentType(ContentType.JSON)
                        .body(zone)
                        .when()
                        .put(ZoneData.PUT_ZONE + "{id}")
                ;
    }

    public Response deleteZone(Long id){
        return
                given()
                        .pathParam("id", id)
                        .when()
                        .delete(ZoneData.DELET_ZONE + "{id}")
                ;
    }
}
