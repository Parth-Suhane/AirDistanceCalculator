package com.example.AirDistanceCalculator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class AirDistanceController {
    @Value("${opencage.api.key}")
    private String openCageApiKey;

    @GetMapping("/calculate")
    public String calculateAirDistance(
            @RequestParam("pointA") String pointA,
            @RequestParam("pointB") String pointB) {
        String[] pointACoordinates = pointA.split(",");
        String[] pointBCoordinates = pointB.split(",");
        double latitudeA = Double.parseDouble(pointACoordinates[0]);
        double longitudeA = Double.parseDouble(pointACoordinates[1]);
        double latitudeB = Double.parseDouble(pointBCoordinates[0]);
        double longitudeB = Double.parseDouble(pointBCoordinates[1]);

        double distance = calculateDistance(latitudeA, longitudeA, latitudeB, longitudeB);

        return String.format("Distance: %.2f km", distance);
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
    	double earthRadius = 6371;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadius * c;
    }

    @GetMapping("/geocode")
    public ResponseEntity<GeocodeResponse> geocodeAddress(@RequestParam("address") String address) {
        String url = "https://api.opencagedata.com/geocode/v1/json?key=" + openCageApiKey + "&q=" + address;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<GeocodeResponse> response = restTemplate.getForEntity(url, GeocodeResponse.class);
        return response;
    }
}
