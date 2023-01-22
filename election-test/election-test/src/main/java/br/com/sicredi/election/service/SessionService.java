package br.com.sicredi.election.service;

import br.com.sicredi.election.data.SessionData;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class SessionService {

    public Response findAll(){
        return
                given()
                        .when()
                        .get(SessionData.GET_SESSION)
                ;
    }

    public Response findIdZone(Long id){
        return
                given()
                        .queryParam("idZone", id)
                        .when()
                        .get(SessionData.GET_SESSION_ZONE)
                ;
    }
    public Response createSession(String session){
        return
                given()
                        .contentType(ContentType.JSON)
                        .body(session)
                        .when()
                        .post(SessionData.POST_SESSION)
                ;
    }

    public Response updateSession(String session, Long id){
        return
                given()
                        .pathParam("id", id)
                        .contentType(ContentType.JSON)
                        .body(session)
                        .when()
                        .patch(SessionData.PATCH_SESSION + "{id}")
                ;
    }

    public Response deleteSession(Long id){
        return
                given()
                        .pathParam("id", id)
                        .when()
                        .delete(SessionData.DELET_SESSION + "{id}")
                ;
    }
}
