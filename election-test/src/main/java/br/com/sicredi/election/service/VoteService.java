package br.com.sicredi.election.service;

import br.com.sicredi.election.data.VoteData;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class VoteService {

    public Response createVote(String vote){
        return
                given()
                        .contentType(ContentType.JSON)
                        .body(vote)
                        .when()
                        .post(VoteData.POST_VOTE)
                ;
    }

    public Response deleteVote(Integer id){
        return
                given()
                        .pathParam("id", id)
                        .when()
                        .delete(VoteData.DELETE_VOTE + "{id}")
                ;
    }
}
