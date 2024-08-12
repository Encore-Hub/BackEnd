package com.team6.backend.boxOffice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.team6.backend.boxOffice.dto.BoxOfficeDto;
import com.team6.backend.boxOffice.entity.BoxOffice;
import com.team6.backend.boxOffice.repository.BoxOfficeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BoxOfficeApiService {

    private static final Logger logger = LoggerFactory.getLogger(BoxOfficeApiService.class);

    @Autowired
    private BoxOfficeRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${api.serviceKey}")
    private String serviceKey;

    @Value("${api.catecode}")
    private String catecode;

    @Value("${api.area}")
    private String area;

    private static final String API_URL = "http://www.kopis.or.kr/openApi/restful/boxoffice";
    private static final String STSTYPE = "month"; // "month" 또는 "week"로 조정 가능
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Scheduled(cron = "0 0 14 * * *") // 매일 오후 2시에 실행
    public void fetchAndSaveBoxOfficeData() {
        try {
            LocalDate date = LocalDate.now();
            String formattedDate = date.format(DATE_FORMATTER);

            // URL 생성
            String requestUrl = UriComponentsBuilder.fromHttpUrl(API_URL)
                    .queryParam("service", serviceKey)
                    .queryParam("ststype", STSTYPE)
                    .queryParam("date", formattedDate)
                    .queryParam("catecode", catecode)
                    .queryParam("area", area)
                    .toUriString();

            logger.info("Request URL: {}", requestUrl);

            // API 호출
            String response = restTemplate.getForObject(requestUrl, String.class);

            if (response == null) {
                logger.warn("API 응답이 null입니다.");
                return;
            }

            logger.debug("Raw API Response: {}", response);

            // XML을 JSON으로 변환 using Jackson XmlMapper
            XmlMapper xmlMapper = new XmlMapper();
            JsonNode rootNode = xmlMapper.readTree(response.getBytes());

            // 'boxofs'와 'boxof' 노드를 확인
            JsonNode boxofNode = rootNode.path("boxof");

            if (boxofNode.isMissingNode() || !boxofNode.isArray() || boxofNode.isEmpty()) {
                logger.info("응답에서 박스오피스 정보가 없습니다.");
                return;
            }

            // 순위 15위까지만 필터링
            List<BoxOfficeDto> boxOfficeDtoList = new ArrayList<>();
            for (JsonNode node : boxofNode) {
                if (node.path("rnum").asInt() > 15) continue;

                BoxOfficeDto boxOfficeDto = new BoxOfficeDto(
                        node.path("prfnm").asText(),
                        node.path("mt20id").asText(),
                        node.path("prfplcnm").asText(),
                        node.path("prfpd").asText(),
                        node.path("rnum").asText(),
                        node.path("poster").asText()
                );
                boxOfficeDtoList.add(boxOfficeDto);
            }

            // 기존 데이터 조회
            List<BoxOffice> existingData = repository.findAll();
            Map<String, BoxOffice> existingDataMap = existingData.stream()
                    .collect(Collectors.toMap(BoxOffice::getMt20id, boxOffice -> boxOffice));

            List<BoxOffice> boxOfficeList = new ArrayList<>();
            for (BoxOfficeDto dto : boxOfficeDtoList) {
                BoxOffice existingBoxOffice = existingDataMap.get(dto.getMt20id());

                // 바뀐 데이터만 추가
                if (existingBoxOffice == null || !existingBoxOffice.toDto().equals(dto)) {
                    boxOfficeList.add(BoxOffice.fromDto(dto));
                }
            }

            // 데이터베이스에 저장
            if (!boxOfficeList.isEmpty()) {
                repository.saveAll(boxOfficeList);
                logger.info("저장된 BoxOffice 데이터 수: {}", boxOfficeList.size());
            } else {
                logger.info("저장할 BoxOffice 데이터가 없습니다.");
            }

        } catch (Exception e) {
            logger.error("Failed to fetch or process box office data", e);
        }
    }
}
