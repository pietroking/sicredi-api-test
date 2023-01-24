package br.com.sicredi.election.builder;


import br.com.sicredi.election.dto.candidate.CandidateRequest;

public class CandidateBuilder {

    private CandidateRequest candidateRequest(){return CandidateRequest.builder().name("teste api").cpf("68344658047").number(101).party("API").build();}

    public CandidateRequest create_WhenCandidateRequestIsOk_ThenCandidateCreateSuccessfully(){
        return candidateRequest();
    }
    public CandidateRequest create_WhenCandidateRequestIsEmpty_ThenReturnMessageError(){
        CandidateRequest candidateRequestInvalid = candidateRequest();
        candidateRequestInvalid.setName(null);
        candidateRequestInvalid.setCpf(null);
        candidateRequestInvalid.setNumber(null);
        candidateRequestInvalid.setParty(null);
        return candidateRequestInvalid;
    }
    public CandidateRequest create_WhenCandidateRequestIsCpfInvalid_ThenReturnMessageCpfInvalid(){
        CandidateRequest candidateRequestInvalid = candidateRequest();
        candidateRequestInvalid.setCpf("11111111111");
        return candidateRequestInvalid;
    }
    public CandidateRequest update_WhenCandidateUpdateRequestIsOk_ThenCandidateUpdatedSuccessfully(){
        CandidateRequest candidateRequest = candidateRequest();
        candidateRequest.setNumber(99);
        candidateRequest.setParty("UP");
        return candidateRequest;
    }
    public CandidateRequest update_WhenCandidateUpdateRequestIsEmpty_ThenReturnMessageError(){
        CandidateRequest candidateRequestInvalid = candidateRequest();
        candidateRequestInvalid.setNumber(null);
        candidateRequestInvalid.setParty(null);
        return candidateRequestInvalid;
    }
    public CandidateRequest update_WhenCandidateUpdateRequestIsNumberNegative_ThenReturnMessageNegativeNumberError(){
        CandidateRequest candidateRequestInvalid = candidateRequest();
        candidateRequestInvalid.setNumber(-1);
        return candidateRequestInvalid;
    }
}
