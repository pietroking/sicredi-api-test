package br.com.sicredi.election.builder;

import br.com.sicredi.election.dto.collaborator.CollaboratorRequest;

public class CollaboratorBuilder {

    private CollaboratorRequest collaboratorRequestBuild(){return CollaboratorRequest.builder().name("teste api").sessionId(1).cpf("68344658047").build();}

    public CollaboratorRequest create_CollaboratorIsOk(Integer idSession){
        CollaboratorRequest collaboratorRequest = collaboratorRequestBuild();
        collaboratorRequest.setSessionId(idSession);
        return collaboratorRequest;
    }
    public CollaboratorRequest create_CollaboratorEmpty(){
        CollaboratorRequest collaboratorRequestInvalid = collaboratorRequestBuild();
        collaboratorRequestInvalid.setName(null);
        collaboratorRequestInvalid.setSessionId(null);
        collaboratorRequestInvalid.setCpf(null);
        return collaboratorRequestInvalid;
    }
    public CollaboratorRequest create_CollaboratorSessionIdError(){
        CollaboratorRequest collaboratorRequestInvalid = collaboratorRequestBuild();
        collaboratorRequestInvalid.setSessionId(-1);
        return collaboratorRequestInvalid;
    }
    public CollaboratorRequest create_CollaboratorCpfInvalid(){
        CollaboratorRequest collaboratorRequestInvalid = collaboratorRequestBuild();
        collaboratorRequestInvalid.setCpf("111111111111");
        return collaboratorRequestInvalid;
    }
    public CollaboratorRequest update_CollaboratorIsOk(){
        return CollaboratorRequest.builder().sessionId(1).build();
    }
    public CollaboratorRequest update_CollaboratorIsSessionEmpty(){
        return CollaboratorRequest.builder().sessionId(null).build();
    }
    public CollaboratorRequest update_CollaboratorIsSessionInvalid(){
        return CollaboratorRequest.builder().sessionId(-1).build();
    }
}
