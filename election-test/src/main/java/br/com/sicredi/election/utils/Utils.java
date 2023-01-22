package br.com.sicredi.election.utils;

import br.com.sicredi.election.dto.session.SessionRequest;
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
}
