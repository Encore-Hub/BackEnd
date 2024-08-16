package com.team6.backend.theater.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team6.backend.pfmc.entity.Pfmc;
import com.team6.backend.pfmc.repository.PfmcRepository;
import com.team6.backend.theater.api.dto.TheaterPfmcDetailDto;
import com.team6.backend.theater.theater.entity.TheaterPfmcDetail;
import com.team6.backend.theater.theater.entity.TheaterId;
import com.team6.backend.theater.theater.repository.TheaterPfmcDetailRepository;
import com.team6.backend.theater.theater.repository.TheaterIdRepository;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@Service
public class TheaterPfmcDetailService {

    @Autowired
    private TheaterPfmcDetailRepository theaterPfmcDetailRepository;

    @Autowired
    private PfmcRepository pfmcRepository;

    @Autowired
    private TheaterIdRepository theaterIdRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${api.serviceKey}")
    private String serviceKey;

    private static final int CHUNK_SIZE = 50; // Chunk size

    public void fetchAndSaveTheaterPfmcDetails() throws Exception {
        List<Pfmc> pfmcs = pfmcRepository.findAll();
        System.out.println("Total theater IDs fetched from DB: " + pfmcs.size());

        ObjectMapper objectMapper = new ObjectMapper();

        int processedCount = 0; // Initialize counter

        for (int i = 0; i < pfmcs.size(); i += CHUNK_SIZE) {
            int end = Math.min(i + CHUNK_SIZE, pfmcs.size());
            List<Pfmc> chunk = pfmcs.subList(i, end);
            for (Pfmc pfmc : chunk) {
                processTheaterDetail(pfmc, objectMapper);
                processedCount++; // Increment counter for each processed ID
            }
        }

        System.out.println("Total Theater IDs processed: " + processedCount); // Print the total count
    }

    private void processTheaterDetail(Pfmc pfmc, ObjectMapper objectMapper) {
        String mt10id = pfmc.getMt10id();
        System.out.println("Fetching details for Theater ID: " + mt10id);

        String requestUrl = buildRequestUrl(mt10id);

        try {
            String response = fetchResponse(requestUrl);
            if (response == null || response.isEmpty()) {
                System.err.println("Empty or null response for Theater ID: " + mt10id);
                return;
            }

            TheaterPfmcDetailDto theaterDetailData = parseResponse(response, objectMapper);
            saveOrUpdateTheaterDetails(mt10id, theaterDetailData);

        } catch (Exception e) {
            System.err.println("Failed to fetch or process details for Theater ID: " + mt10id);
            e.printStackTrace();
        }
    }

    private String buildRequestUrl(String mt10id) {
        String url = "http://www.kopis.or.kr/openApi/restful/prfplc/" + mt10id;
        return UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("service", serviceKey)
                .queryParam("newsql", "Y")
                .toUriString();
    }

    private String fetchResponse(String requestUrl) {
        return restTemplate.getForObject(requestUrl, String.class);
    }

    private TheaterPfmcDetailDto parseResponse(String response, ObjectMapper objectMapper) throws Exception {
        JSONObject jsonObject = XML.toJSONObject(response);
        String jsonString = jsonObject.toString();
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        JsonNode dbNode = jsonNode.path("dbs").path("db");

        if (dbNode.isMissingNode() || dbNode.isEmpty()) {
            throw new RuntimeException("Missing or empty 'db' node");
        }

        return objectMapper.treeToValue(dbNode, TheaterPfmcDetailDto.class);
    }

    private void saveOrUpdateTheaterDetails(String mt10id, TheaterPfmcDetailDto theaterDetailData) {
        Optional<TheaterId> theaterIdOpt = theaterIdRepository.findByMt10id(mt10id);
        if (theaterIdOpt.isEmpty()) {
            System.err.println("No TheaterId found for mt10id: " + mt10id);
            return;
        }

        TheaterId theaterId = theaterIdOpt.get();

        Optional<TheaterPfmcDetail> existingTheaterOpt = theaterPfmcDetailRepository.findById(mt10id);

        if (existingTheaterOpt.isPresent()) {
            TheaterPfmcDetail existingTheater = existingTheaterOpt.get();
            TheaterPfmcDetail updatedTheater = updateTheater(existingTheater, theaterDetailData, theaterId);

            if (!existingTheater.equals(updatedTheater)) {
                theaterPfmcDetailRepository.save(updatedTheater);
                System.out.println("Updated Theater: " + updatedTheater);
            } else {
                System.out.println("No changes for Theater ID: " + mt10id);
            }
        } else {
            TheaterPfmcDetail newTheater = TheaterPfmcDetail.builder()
                    .mt10id(mt10id)
                    .fcltychartr(theaterDetailData.getFcltychartr())
                    .sidonm(theaterId.getSidonm())
                    .gugunnm(theaterId.getGugunnm())
                    .fcltynm(theaterDetailData.getFcltynm())
                    .seatscale(theaterDetailData.getSeatscale())
                    .mt13cnt(theaterDetailData.getMt13cnt())
                    .telno(theaterDetailData.getTelno())
                    .adres(theaterDetailData.getAdres())
                    .la(theaterDetailData.getLa())
                    .lo(theaterDetailData.getLo())
                    .parkinglot(theaterDetailData.getParkinglot())
                    .build();

            TheaterPfmcDetail savedTheater = theaterPfmcDetailRepository.save(newTheater);
            System.out.println("Saved new Theater: " + savedTheater);
        }
    }

    private TheaterPfmcDetail updateTheater(TheaterPfmcDetail existingTheater, TheaterPfmcDetailDto theaterDetailData, TheaterId theaterId) {
        return existingTheater.toBuilder()
                .fcltychartr(theaterDetailData.getFcltychartr())
                .sidonm(theaterId.getSidonm()) // Update with the value from TheaterId
                .gugunnm(theaterId.getGugunnm()) // Update with the value from TheaterId
                .fcltynm(theaterDetailData.getFcltynm())
                .seatscale(theaterDetailData.getSeatscale())
                .mt13cnt(theaterDetailData.getMt13cnt())
                .telno(theaterDetailData.getTelno())
                .adres(theaterDetailData.getAdres())
                .la(theaterDetailData.getLa())
                .lo(theaterDetailData.getLo())
                .parkinglot(theaterDetailData.getParkinglot())
                .build();
    }
}
