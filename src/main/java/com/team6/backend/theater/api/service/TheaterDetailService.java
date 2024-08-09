package com.team6.backend.theater.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team6.backend.pfmc.entity.Pfmc;
import com.team6.backend.pfmc.repository.PfmcRepository;
import com.team6.backend.theater.api.dto.TheaterDetailDto;
import com.team6.backend.theater.theater.entity.TheaterDetail;
import com.team6.backend.theater.theater.entity.TheaterId;
import com.team6.backend.theater.theater.repository.TheaterDetailRepository;
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
public class TheaterDetailService {

    @Autowired
    private TheaterDetailRepository theaterDetailRepository;

    @Autowired
    private TheaterIdRepository theaterIdRepository;

    @Autowired
    private PfmcRepository pfmcRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${api.serviceKey}")
    private String serviceKey;

    private static final int CHUNK_SIZE = 50; // 청크 크기

    public void fetchAndSaveTheaterDetails() throws Exception {
        List<TheaterId> theaterIds = theaterIdRepository.findAll();
        System.out.println("Total theater IDs fetched from DB: " + theaterIds.size());

        ObjectMapper objectMapper = new ObjectMapper();

        for (int i = 0; i < theaterIds.size(); i += CHUNK_SIZE) {
            int end = Math.min(i + CHUNK_SIZE, theaterIds.size());
            List<TheaterId> chunk = theaterIds.subList(i, end);

            for (TheaterId theaterId : chunk) {
                processTheaterDetail(theaterId, objectMapper);
            }
        }
    }

    private void processTheaterDetail(TheaterId theaterId, ObjectMapper objectMapper) {
        String mt10id = theaterId.getMt10id();
        System.out.println("Fetching details for Theater ID: " + mt10id);

        String url = "http://www.kopis.or.kr/openApi/restful/prfplc/" + mt10id;
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("service", serviceKey)
                .queryParam("newsql", "Y");
        String requestUrl = builder.toUriString();

        try {
            String response = restTemplate.getForObject(requestUrl, String.class);
            if (response == null || response.isEmpty()) {
                System.err.println("Empty or null response for Theater ID: " + mt10id);
                return;
            }

            JSONObject jsonObject = XML.toJSONObject(response);
            String jsonString = jsonObject.toString();

            JsonNode jsonNode = objectMapper.readTree(jsonString);
            JsonNode dbsNode = jsonNode.path("dbs");
            JsonNode dbNode = dbsNode.path("db");
            if (dbNode.isMissingNode() || dbNode.isEmpty()) {
                System.err.println("Missing or empty 'db' node for Theater ID: " + mt10id);
                return;
            }

            TheaterDetailDto theaterDetailDto = objectMapper.treeToValue(dbNode, TheaterDetailDto.class);

            // Pfmc 객체를 조회합니다.
            String pfmcId = theaterDetailDto.getRelateurl(); // Assume relateurl contains pfmc_id
            Pfmc pfmc = pfmcRepository.findById(pfmcId).orElse(null);

            Optional<TheaterDetail> existingTheaterOpt = theaterDetailRepository.findById(mt10id);

            if (existingTheaterOpt.isPresent()) {
                TheaterDetail existingTheater = existingTheaterOpt.get();
                TheaterDetail updatedTheater = updateTheater(existingTheater, theaterDetailDto, theaterId);

                if (!existingTheater.equals(updatedTheater)) {
                    theaterDetailRepository.save(updatedTheater);
                    System.out.println("Updated Theater: " + updatedTheater);
                } else {
                    System.out.println("No changes for Theater ID: " + mt10id);
                }
            } else {
                TheaterDetail newTheater = TheaterDetail.builder()
                        .mt10id(theaterId.getMt10id())
                        .fcltychartr(theaterDetailDto.getFcltychartr())
                        .sidonm(theaterId.getSidonm())
                        .gugunnm(theaterId.getGugunnm())
                        .fcltynm(theaterDetailDto.getFcltynm())
                        .seatscale(theaterDetailDto.getSeatscale())
                        .mt13cnt(theaterDetailDto.getMt13cnt())
                        .telno(theaterDetailDto.getTelno())
                        .relateurl(theaterDetailDto.getRelateurl())
                        .adres(theaterDetailDto.getAdres())
                        .la(theaterDetailDto.getLa())
                        .lo(theaterDetailDto.getLo())
                        .parkinglot(theaterDetailDto.getParkinglot())
                        .build();

                TheaterDetail savedTheater = theaterDetailRepository.save(newTheater);
                System.out.println("Saved new Theater: " + savedTheater);
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch or process details for Theater ID: " + mt10id);
            e.printStackTrace();
        }
    }

    private TheaterDetail updateTheater(TheaterDetail existingTheater, TheaterDetailDto theaterDetailDto, TheaterId theaterId) {
        return existingTheater.toBuilder()
                .fcltychartr(!existingTheater.getFcltychartr().equals(theaterDetailDto.getFcltychartr()) ? theaterDetailDto.getFcltychartr() : existingTheater.getFcltychartr())
                .sidonm(!existingTheater.getSidonm().equals(theaterId.getSidonm()) ? theaterId.getSidonm() : existingTheater.getSidonm())
                .gugunnm(!existingTheater.getGugunnm().equals(theaterId.getGugunnm()) ? theaterId.getGugunnm() : existingTheater.getGugunnm())
                .fcltynm(!existingTheater.getFcltynm().equals(theaterDetailDto.getFcltynm()) ? theaterDetailDto.getFcltynm() : existingTheater.getFcltynm())
                .seatscale(existingTheater.getSeatscale() != theaterDetailDto.getSeatscale() ? theaterDetailDto.getSeatscale() : existingTheater.getSeatscale())
                .mt13cnt(existingTheater.getMt13cnt() != theaterDetailDto.getMt13cnt() ? theaterDetailDto.getMt13cnt() : existingTheater.getMt13cnt())
                .telno(!existingTheater.getTelno().equals(theaterDetailDto.getTelno()) ? theaterDetailDto.getTelno() : existingTheater.getTelno())
                .relateurl(!existingTheater.getRelateurl().equals(theaterDetailDto.getRelateurl()) ? theaterDetailDto.getRelateurl() : existingTheater.getRelateurl())
                .adres(!existingTheater.getAdres().equals(theaterDetailDto.getAdres()) ? theaterDetailDto.getAdres() : existingTheater.getAdres())
                .la(existingTheater.getLa() != theaterDetailDto.getLa() ? theaterDetailDto.getLa() : existingTheater.getLa())
                .lo(existingTheater.getLo() != theaterDetailDto.getLo() ? theaterDetailDto.getLo() : existingTheater.getLo())
                .parkinglot(!existingTheater.getParkinglot().equals(theaterDetailDto.getParkinglot()) ? theaterDetailDto.getParkinglot() : existingTheater.getParkinglot())
                .build();
    }
}
