package br.com.sicredi.election.builder;

import br.com.sicredi.election.dto.session.SessionRequest;

public class SessionBuilder {

    private SessionRequest sessionRequestBuild(){
        return SessionRequest.builder().idZone(1L).number(101L).urnNumber(101L).build();
    }
    public SessionRequest create_SessionIsOk(Long idZone){
        SessionRequest sessionRequest = sessionRequestBuild();
        sessionRequest.setIdZone(idZone);
        return sessionRequest;
    }
    public SessionRequest create_SessionIsOk2(Long idZone){
        SessionRequest sessionRequest = sessionRequestBuild();
        sessionRequest.setIdZone(idZone);
        sessionRequest.setNumber(102L);
        sessionRequest.setUrnNumber(102L);
        return sessionRequest;
    }
    public SessionRequest create_SessionEmpty(){
        SessionRequest sessionRequestInvalid = sessionRequestBuild();
        sessionRequestInvalid.setIdZone(null);
        sessionRequestInvalid.setNumber(null);
        sessionRequestInvalid.setUrnNumber(null);
        return sessionRequestInvalid;
    }
    public SessionRequest create_SessionIdZoneError(){
        SessionRequest sessionRequestInvalid = sessionRequestBuild();
        sessionRequestInvalid.setIdZone(-1L);
        return sessionRequestInvalid;
    }
    public SessionRequest create_SessionNumberExist(){
        SessionRequest sessionRequestInvalid = sessionRequestBuild();
        sessionRequestInvalid.setNumber(1L);
        return sessionRequestInvalid;
    }
    public SessionRequest create_SessionUrnNumberExist(Long idZone){
        SessionRequest sessionRequestInvalid = sessionRequestBuild();
        sessionRequestInvalid.setIdZone(idZone);
        sessionRequestInvalid.setUrnNumber(2L);
        return sessionRequestInvalid;
    }
    public SessionRequest create_SessionNumberAndUrnNumberInvalid(){
        SessionRequest sessionRequestInvalid = sessionRequestBuild();
        sessionRequestInvalid.setNumber(-1L);
        sessionRequestInvalid.setUrnNumber(-1L);
        return sessionRequestInvalid;
    }
    public SessionRequest update_SessionUrnNumberIsOk(){
        SessionRequest sessionRequestInvalid = sessionRequestBuild();
        sessionRequestInvalid.setUrnNumber(99L);
        return sessionRequestInvalid;
    }
    public SessionRequest update_SessionUrnNumberNegative(){
        SessionRequest sessionRequestInvalid = sessionRequestBuild();
        sessionRequestInvalid.setUrnNumber(-1L);
        return sessionRequestInvalid;
    }
    public SessionRequest update_SessionUrnNumberNull(){
        SessionRequest sessionRequestInvalid = sessionRequestBuild();
        sessionRequestInvalid.setUrnNumber(null);
        return sessionRequestInvalid;
    }
}
