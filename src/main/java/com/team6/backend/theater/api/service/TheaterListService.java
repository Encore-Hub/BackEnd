package com.team6.backend.theater.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team6.backend.common.exception.ErrorCode;
import com.team6.backend.common.exception.TheaterException;
import com.team6.backend.theater.region.entity.Gugunnm;
import com.team6.backend.theater.region.entity.Sidonm;
import com.team6.backend.theater.region.repository.GugunnmRepository;
import com.team6.backend.theater.region.repository.SidonmRepository;
import com.team6.backend.theater.theater.entity.TheaterId;
import com.team6.backend.theater.theater.repository.TheaterIdRepository;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Service
public class TheaterListService {

    private static final Logger logger = LoggerFactory.getLogger(TheaterListService.class);

    @Autowired
    private TheaterIdRepository theaterIdRepository;

    @Autowired
    private SidonmRepository sidonmRepository;

    @Autowired
    private GugunnmRepository gugunnmRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${api.serviceKey}")
    private String serviceKey;

    @Value("${api.area}")
    private String area;

    private static final int PAGE_SIZE = 1000; // 페이지당 데이터 수
    private static final String BASE_URL = "http://www.kopis.or.kr/openApi/restful/prfplc";

    @Scheduled(fixedRate = 60000) // 매 60초마다 실행
    public void fetchAndSaveTheaterList() {
        int page = 1;
        boolean moreData = true;

        while (moreData) {
            try {
                // URL 생성
                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                        .queryParam("service", serviceKey)
                        .queryParam("cpage", page)
                        .queryParam("rows", PAGE_SIZE)
                        .queryParam("signgucode", area);

                // API 호출
                String response = restTemplate.getForObject(builder.toUriString(), String.class);

                if (response == null) {
                    logger.warn("API 응답이 null입니다.");
                    throw new TheaterException(ErrorCode.UNKNOWN_ACCESS_TOKEN_ERROR);
                }

                // XML을 JSON으로 변환
                JSONObject jsonObject = XML.toJSONObject(response);
                String jsonString = jsonObject.toString();

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode root = objectMapper.readTree(jsonString);
                JsonNode theatersNode = root.path("dbs").path("db");

                if (theatersNode.isMissingNode() || !theatersNode.isArray() || theatersNode.isEmpty()) {
                    logger.info("응답에서 극장 정보가 없습니다.");
                    break;
                }

                // 데이터베이스에서 기존 극장 목록 조회
                List<TheaterId> existingTheaterIds = theaterIdRepository.findAll();
                Set<String> existingMt10ids = new HashSet<>();
                for (TheaterId theaterId : existingTheaterIds) {
                    existingMt10ids.add(theaterId.getMt10id());
                }

                // 청크로 나누어 데이터 처리
                List<TheaterId> theaterIdBatch = new ArrayList<>();
                for (JsonNode node : theatersNode) {
                    try {
                        String mt10id = node.path("mt10id").asText();
                        String fcltynm = node.path("fcltynm").asText();
                        String fcltychartr = node.path("fcltychartr").asText();
                        String sidonm = node.path("sidonm").asText();
                        String gugunnm = node.path("gugunnm").asText();


                            // Sidonm 데이터 저장
                            Optional<Sidonm> sidonmOpt = sidonmRepository.findBySidonm(sidonm);
                            if (sidonmOpt.isEmpty()) {
                                sidonmRepository.save(new Sidonm(sidonm));
                            }

                            // Gugunnm 데이터 저장
                            Optional<Gugunnm> gugunnmOpt = gugunnmRepository.findById(gugunnm);
                            if (gugunnmOpt.isEmpty()) {
                                gugunnmRepository.save(new Gugunnm(gugunnm, sidonm));
                            }


                        if (!existingMt10ids.contains(mt10id)) {
                            TheaterId theaterId = new TheaterId(mt10id, fcltynm, fcltychartr, sidonm, gugunnm);
                            theaterIdBatch.add(theaterId);

                            // 청크 사이즈에 도달하면 데이터베이스에 저장
                            if (theaterIdBatch.size() >= PAGE_SIZE) {
                                theaterIdRepository.saveAll(theaterIdBatch);
                                logger.info("저장된 TheaterId 청크: " + theaterIdBatch.size());
                                theaterIdBatch.clear();
                            }
                        }
                    } catch (Exception e) {
                        logger.error("노드 파싱 오류: " + node.toString(), e);
                    }
                }

                // 마지막 청크 저장
                if (!theaterIdBatch.isEmpty()) {
                    theaterIdRepository.saveAll(theaterIdBatch);
                    logger.info("저장된 TheaterId 마지막 청크: " + theaterIdBatch.size());
                }

                // 다음 페이지로 이동
                page++;

                // 다음 페이지가 존재하지 않는 경우 루프 종료
                if (theatersNode.size() < PAGE_SIZE) {
                    moreData = false;
                }
            } catch (Exception e) {
                logger.error("Failed to fetch or process theater list data", e);
                throw new TheaterException(ErrorCode.UNKNOWN_ACCESS_TOKEN_ERROR);
            }
        }
    }
}
