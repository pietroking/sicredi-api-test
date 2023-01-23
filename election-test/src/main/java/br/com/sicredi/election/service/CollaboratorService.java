package br.com.sicredi.election.service;

import br.com.sicredi.election.data.CollaboratorData;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CollaboratorService {

    public Response findAll(){
        return
                given()
                        .when()
                        .get(CollaboratorData.GET_COLLABORATOR)
                ;
    }

    public Response findIdSession(Long id){
        return
                given()
                        .queryParam("idSession", id)
                        .when()
                        .get(CollaboratorData.GET_COLLABORATOR_SESSION + "find-by-idsession")
                ;
    }

    public Response createCollaborator(String collaborator){
        return
                given()
                        .contentType(ContentType.JSON)
                        .body(collaborator)
                        .when()
                        .post(CollaboratorData.POST_COLLABORATOR)
                ;
    }

    public Response updateCollaborator(String collaborator, Long id){
        return
                given()
                        .pathParam("id", id)
                        .contentType(ContentType.JSON)
                        .body(collaborator)
                        .when()
                        .patch(CollaboratorData.PATCH_COLLABORATOR + "{id}")
                ;
    }

    public Response deleteCollaborator(Long id){
        return
                given()
                        .pathParam("id", id)
                        .when()
                        .delete(CollaboratorData.DELET_COLLABORATOR + "{id}")
                ;
    }
}
