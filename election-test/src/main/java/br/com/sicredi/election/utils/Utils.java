package br.com.sicredi.election.utils;

import br.com.sicredi.election.dto.candidate.CandidateRequest;
import br.com.sicredi.election.dto.collaborator.CollaboratorRequest;
import br.com.sicredi.election.dto.session.SessionRequest;
import br.com.sicredi.election.dto.vote.VoteRequest;
import br.com.sicredi.election.dto.voter.VoterRequest;
import br.com.sicredi.election.dto.zone.ZoneRequest;
import com.google.gson.Gson;
import net.datafaker.Faker;

import java.util.Locale;

public class Utils {
    public static Faker faker = new Faker(new Locale("pt-BR"));
    public static String getBaseUrl() {
        String baseUrl = "http://localhost:8080/api/v1/";
        return baseUrl;
    }
    public static String convertZoneToJson(ZoneRequest zoneRequest) {
        return new Gson().toJson(zoneRequest);
    }
    public static String convertSessionToJson(SessionRequest sessionRequest) {
        return new Gson().toJson(sessionRequest);
    }
    public static String convertCollaboratorToJson(CollaboratorRequest collaboratorRequest) {
        return new Gson().toJson(collaboratorRequest);
    }
    public static String convertVoterToJson(VoterRequest voterRequest) {
        return new Gson().toJson(voterRequest);
    }
    public static String convertCandidateToJson(CandidateRequest candidateRequest) {
        return new Gson().toJson(candidateRequest);
    }
    public static String convertVoteToJson(VoteRequest voteRequest) {
        return new Gson().toJson(voteRequest);
    }
}
