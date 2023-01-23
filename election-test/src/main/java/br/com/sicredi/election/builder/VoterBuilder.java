package br.com.sicredi.election.builder;

import br.com.sicredi.election.dto.voter.VoterRequest;

public class VoterBuilder {

    private VoterRequest voterRequestBuild(){return VoterRequest.builder().name("teste api").sessionId(1L).cpf("68344658047").build();}

    public VoterRequest create_VoterIsOk(Long idSession){
        VoterRequest voterRequest = voterRequestBuild();
        voterRequest.setSessionId(idSession);
        return voterRequest;
    }
    public VoterRequest create_VoterEmpty(){
        VoterRequest voterRequestInvalid = voterRequestBuild();
        voterRequestInvalid.setName(null);
        voterRequestInvalid.setSessionId(null);
        voterRequestInvalid.setCpf(null);
        return voterRequestInvalid;
    }
    public VoterRequest create_VoterSessionIdError(){
        VoterRequest voterRequestInvalid = voterRequestBuild();
        voterRequestInvalid.setSessionId(-1L);
        return voterRequestInvalid;
    }
    public VoterRequest create_VoterCpfInvalid(){
        VoterRequest voterRequestInvalid = voterRequestBuild();
        voterRequestInvalid.setCpf("111111111111");
        return voterRequestInvalid;
    }
    public VoterRequest update_VoterIsOk(){
        return VoterRequest.builder().sessionId(1L).build();
    }
    public VoterRequest update_VoterIsSessionEmpty(){
        return VoterRequest.builder().sessionId(null).build();
    }
    public VoterRequest update_VoterIsSessionInvalid(){
        return VoterRequest.builder().sessionId(-1L).build();
    }
}
