package br.com.sicredi.election.service;

import br.com.sicredi.election.data.CandidateData;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CandidateService {

    public Response findAll(){
        return
                given()
                        .when()
                        .get(CandidateData.GET_CANDIDATE)
                ;
    }

    public Response findContVotes(){
        return
                given()
                        .when()
                        .get(CandidateData.GET_CANDIDATE_VOTES)
                ;
    }

    public Response findByName(String name){
        return
                given()
                        .queryParam("name", name)
                        .when()
                        .get(CandidateData.GET_CANDIDATE_NAME)
                ;
    }

    public Response createCandidate(String candidate){
        return
                given()
                        .contentType(ContentType.JSON)
                        .body(candidate)
                        .when()
                        .post(CandidateData.POST_CANDIDATE)
                ;
    }

    public Response updateCandidate(String candidate, Integer id){
        return
                given()
                        .pathParam("id", id)
                        .contentType(ContentType.JSON)
                        .body(candidate)
                        .when()
                        .patch(CandidateData.PATCH_CANDIDATE + "{id}")
                ;
    }

    public Response deleteCandidate(Integer id){
        return
                given()
                        .pathParam("id", id)
                        .when()
                        .delete(CandidateData.DELET_CANDIDATE + "{id}")
                ;
    }
}
