package br.com.sicredi.election.service;


import br.com.sicredi.election.data.VoterData;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class VoterService {

    public Response findAll(){
        return
                given()
                        .when()
                        .get(VoterData.GET_VOTER)
                ;
    }

    public Response createVoter(String voter){
        return
                given()
                        .contentType(ContentType.JSON)
                        .body(voter)
                        .when()
                        .post(VoterData.POST_VOTER)
                ;
    }

    public Response updateVoter(String voter, Long id){
        return
                given()
                        .pathParam("id", id)
                        .contentType(ContentType.JSON)
                        .body(voter)
                        .when()
                        .patch(VoterData.PATCH_VOTER + "{id}")
                ;
    }

    public Response deleteVoter(Long id){
        return
                given()
                        .pathParam("id", id)
                        .when()
                        .delete(VoterData.DELET_VOTER + "{id}")
                ;
    }
}
