package br.com.sicredi.election.builder;

import br.com.sicredi.election.dto.session.SessionRequest;

public class SessionBuilder {

    private SessionRequest sessionRequestBuild(){
        return SessionRequest.builder().idZone(1).number(101).urnNumber(101).build();
    }
    public SessionRequest create_SessionIsOk(Integer idZone){
        SessionRequest sessionRequest = sessionRequestBuild();
        sessionRequest.setIdZone(idZone);
        return sessionRequest;
    }
    public SessionRequest create_SessionIsOk2(Integer idZone){
        SessionRequest sessionRequest = sessionRequestBuild();
        sessionRequest.setIdZone(idZone);
        sessionRequest.setNumber(102);
        sessionRequest.setUrnNumber(102);
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
        sessionRequestInvalid.setIdZone(-1);
        return sessionRequestInvalid;
    }
    public SessionRequest create_SessionNumberExist(){
        SessionRequest sessionRequestInvalid = sessionRequestBuild();
        sessionRequestInvalid.setNumber(1);
        return sessionRequestInvalid;
    }
    public SessionRequest create_SessionUrnNumberExist(Integer idZone){
        SessionRequest sessionRequestInvalid = sessionRequestBuild();
        sessionRequestInvalid.setIdZone(idZone);
        sessionRequestInvalid.setUrnNumber(2);
        return sessionRequestInvalid;
    }
    public SessionRequest create_SessionNumberAndUrnNumberInvalid(){
        SessionRequest sessionRequestInvalid = sessionRequestBuild();
        sessionRequestInvalid.setNumber(-1);
        sessionRequestInvalid.setUrnNumber(-1);
        return sessionRequestInvalid;
    }
    public SessionRequest update_SessionUrnNumberIsOk(){
        SessionRequest sessionRequestInvalid = sessionRequestBuild();
        sessionRequestInvalid.setUrnNumber(99);
        return sessionRequestInvalid;
    }
    public SessionRequest update_SessionUrnNumberNegative(){
        SessionRequest sessionRequestInvalid = sessionRequestBuild();
        sessionRequestInvalid.setUrnNumber(-1);
        return sessionRequestInvalid;
    }
    public SessionRequest update_SessionUrnNumberNull(){
        SessionRequest sessionRequestInvalid = sessionRequestBuild();
        sessionRequestInvalid.setUrnNumber(null);
        return sessionRequestInvalid;
    }
}
