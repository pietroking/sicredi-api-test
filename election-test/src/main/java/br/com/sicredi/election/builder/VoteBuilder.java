package br.com.sicredi.election.builder;

import br.com.sicredi.election.dto.vote.VoteRequest;

public class VoteBuilder {
    private VoteRequest voteRequestBuild(){return VoteRequest.builder().number(null).sessionId(null).cpf(null).build();}

    public VoteRequest create_VoteIsOk(Integer number, Integer sessionId, String cpf){
        VoteRequest voteRequest = voteRequestBuild();
        voteRequest.setNumber(number);
        voteRequest.setSessionId(sessionId);
        voteRequest.setCpf(cpf);
        return voteRequest;
    }

    public VoteRequest create_VoteIsEmpty(){
        return voteRequestBuild();
    }

    public VoteRequest create_VoteIsNumberInvalid( Integer sessionId, String cpf){
        VoteRequest voteRequest = voteRequestBuild();
        voteRequest.setNumber(-1);
        voteRequest.setSessionId(sessionId);
        voteRequest.setCpf(cpf);
        return voteRequest;
    }

    public VoteRequest create_VoteIsSessionInvalid(Integer number, String cpf){
        VoteRequest voteRequest = voteRequestBuild();
        voteRequest.setNumber(number);
        voteRequest.setSessionId(-1);
        voteRequest.setCpf(cpf);
        return voteRequest;
    }

    public VoteRequest create_VoteIsCpfInvalid(Integer number, Integer sessionId){
        VoteRequest voteRequest = voteRequestBuild();
        voteRequest.setNumber(number);
        voteRequest.setSessionId(sessionId);
        voteRequest.setCpf("111111111111");
        return voteRequest;
    }
}
