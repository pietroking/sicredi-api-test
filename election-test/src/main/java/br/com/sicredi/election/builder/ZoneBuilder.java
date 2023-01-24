package br.com.sicredi.election.builder;

import br.com.sicredi.election.dto.zone.ZoneRequest;

public class ZoneBuilder {
    private ZoneRequest zoneRequestBuild(){
        return ZoneRequest.builder().number(101).build();
    }

    public ZoneRequest create_ZoneIsOk(){
        return zoneRequestBuild();
    }
    public ZoneRequest create_ZoneEmpty(){
        ZoneRequest zoneRequestInvalid = zoneRequestBuild();
        zoneRequestInvalid.setNumber(null);
        return zoneRequestInvalid;
    }
    public ZoneRequest create_ZoneNegativeNumber(){
        ZoneRequest zoneRequestInvalid = zoneRequestBuild();
        zoneRequestInvalid.setNumber(-1);
        return zoneRequestInvalid;
    }

    public ZoneRequest update_ZoneIsOk(){
        ZoneRequest zoneUpdate = zoneRequestBuild();
        zoneUpdate.setNumber(99);
        return zoneUpdate;
    }
}
