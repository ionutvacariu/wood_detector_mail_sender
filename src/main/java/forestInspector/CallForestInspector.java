package forestInspector;

import lombok.Synchronized;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class CallForestInspector {


    private static int request = 0;
    public static final String FOREST_INSPECTOR_URI = "http://www.inspectorulpadurii.ro/SumalSatelit/Ajax/HartaAvizeWoodTracking";

    public static void main(String[] args) {

        new CallForestInspector().
                callForestInspector("IS10CDJ123");
    }

    public boolean callForestInspector(final String registrationNumber) {
        RestTemplate restTemplate = new RestTemplate();
        String postRequest = "{\"date0\":\"Wed, 22 Apr 2020 21:00:00 GMT\"," +
                "\"date1\":\"Fri, 22 May 2020 21:00:00 GMT\"," +
                "\"nrmasina\":\"" + registrationNumber + "\"," +
                "\"nrapv\":\"\"," +
                "\"transportLocRecoltare\":true," +
                "\"transportDepozit\":true," +
                "\"transportFrontiera\":true," +
                "\"transportTransbordare\":true," +
                "\"transportConfiscare\":true," +
                "\"transportAlteSituatii\":true}";


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity request2 = new HttpEntity(postRequest, headers);


        ForestInspectorResponse[] result = restTemplate.postForObject(FOREST_INSPECTOR_URI, request2, ForestInspectorResponse[].class);

        List<ForestInspectorResponse> forestInspectorResponses = Arrays.asList(result);

        return manageFIResponse(registrationNumber, forestInspectorResponses);

    }

    private boolean manageFIResponse(String
                                             registrationNumber, List<ForestInspectorResponse> forestInspectorResponses) {
        if (forestInspectorResponses.isEmpty()) {
            //System.out.println("Plate number " + registrationNumber + "have no notice !!!");
            return false;
        } else return true;

    }
}
