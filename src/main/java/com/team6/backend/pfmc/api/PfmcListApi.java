package com.team6.backend.pfmc.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team6.backend.pfmc.repository.PfmcListRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import java.text.ParseException;

@Component
@Order(1)
@RequiredArgsConstructor
public class PfmcListApi {

    @Value("${api.serviceKey}")
    private String apiKey;

    private final PfmcListRepository pfmcListRepository;
    private final ObjectMapper objectMapper;

    public void callPfmcListApiJson() {
        StringBuffer result = new StringBuffer();

        try {
            // 날짜 형식을 지정
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            Date currentDate = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(currentDate);
            cal.add(Calendar.MONTH, 9); // 몇개월
            String stdate = formatter.format(currentDate);
            String eddate = formatter.format(cal.getTime());

            // API 요청 URL 구성
            String urlStr = "http://www.kopis.or.kr/openApi/restful/pblprfr?service=" +
                    apiKey +  // API KEY
                    "&cpage=1" + // 페이지
                    "&rows=300" + // 데이터 갯수
                    "&stdate=" + stdate + // 데이터 수집시작날짜
                    "&eddate=" + eddate + // 데이터 수집 종료날짜
                    "&shstate=N" + // 어린이를 위한 공연 : NO
                    "&shcate=GGGA" + // 장르코드 : GGGA
                    "&signgucode=11"; // 지역 코드 : 서울

            // HTTP 연결 및 데이터 읽기
            URL url = new URL(urlStr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"))) {
                String returnLine;
                while ((returnLine = br.readLine()) != null) {
                    result.append(returnLine);
                }
            }

            // XML을 JSON으로 변환
            JSONObject jsonObject = XML.toJSONObject(result.toString());
            String jsonString = jsonObject.toString();

            // JSON 파싱
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            JsonNode dbsNode = jsonNode.path("dbs");
            JsonNode dbNode = dbsNode.path("db");

            // 기존 데이터를 삭제
            pfmcListRepository.deleteAll();

            // 새로운 데이터를 변환하여 저장
            List<com.team6.backend.pfmc.entity.PfmcList> pfmcList = objectMapper.convertValue(dbNode, new TypeReference<List<com.team6.backend.pfmc.entity.PfmcList>>() {});
            pfmcListRepository.saveAll(pfmcList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

