package com.team6.backend.pfmc.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team6.backend.pfmc.entity.Pfmc;
import com.team6.backend.pfmc.entity.PfmcList;
import com.team6.backend.pfmc.entity.RelateInfo;
import com.team6.backend.pfmc.repository.PfmcListRepository;
import com.team6.backend.pfmc.repository.PfmcRepository;
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
import java.util.List;

@Component
@Order(2)
@RequiredArgsConstructor
public class PfmcApi {

    @Value("${api.serviceKey}")
    private String apiKey;

    private final PfmcRepository pfmcRepository;
    private final PfmcListRepository pfmcListRepository;
    private final ObjectMapper objectMapper;

    public void callPfmcApiJson() {
        List<PfmcList> performances = pfmcListRepository.findAll();

        if (!performances.isEmpty()) {
            for (PfmcList performance : performances) {
                String performanceId = performance.getPfmcId();
                if (performanceId != null && !performanceId.isEmpty()) {
                    String urlStr = "http://www.kopis.or.kr/openApi/restful/pblprfr/" + performanceId +
                            "?service=" + apiKey + "&newsql=Y";

                    try {
                        URL url = new URL(urlStr);
                        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                        urlConnection.connect();

                        StringBuffer result = new StringBuffer();
                        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
                        String returnLine;
                        while ((returnLine = br.readLine()) != null) {
                            result.append(returnLine);
                        }

                        // XML을 JSON으로 변환
                        JSONObject jsonObject = XML.toJSONObject(result.toString());
                        String jsonString = jsonObject.toString();

                        // JSON을 파싱
                        JsonNode jsonNode = objectMapper.readTree(jsonString);
                        JsonNode dbsNode = jsonNode.path("dbs");
                        JsonNode dbNode = dbsNode.path("db");

                        // Pfmc 엔티티에 모든 필드 매핑
                        Pfmc pfmc = new Pfmc();
                        pfmc.setPrfpdfrom(dbNode.path("prfpdfrom").asText());
                        pfmc.setFcltynm(dbNode.path("fcltynm").asText());
                        pfmc.setEntrpsnmH(dbNode.path("entrpsnmH").asText());
                        pfmc.setEntrpsnmA(dbNode.path("entrpsnmA").asText());
                        pfmc.setPrfpdto(dbNode.path("prfpdto").asText());
                        pfmc.setPcseguidance(dbNode.path("pcseguidance").asText());
                        pfmc.setDtguidance(dbNode.path("dtguidance").asText());
                        pfmc.setPrfcrew(dbNode.path("prfcrew").asText());
                        pfmc.setPrfage(dbNode.path("prfage").asText());
                        pfmc.setPrfstate(dbNode.path("prfstate").asText());
                        pfmc.setUpdatedate(dbNode.path("updatedate").asText());
                        pfmc.setEntrpsnm(dbNode.path("entrpsnm").asText());
                        pfmc.setMt10id(dbNode.path("mt10id").asText());
                        pfmc.setMusicallicense(dbNode.path("musicallicense").asText());
                        pfmc.setArea(dbNode.path("area").asText());
                        pfmc.setFestival(dbNode.path("festival").asText());
                        pfmc.setMusicalcreate(dbNode.path("musicalcreate").asText());
                        pfmc.setPrfcast(dbNode.path("prfcast").asText());
                        pfmc.setPrfruntime(dbNode.path("prfruntime").asText());
                        pfmc.setOpenrun(dbNode.path("openrun").asText());
                        pfmc.setDaehakro(dbNode.path("daehakro").asText());
                        pfmc.setEntrpsnmS(dbNode.path("entrpsnmS").asText());
                        pfmc.setMt20id(dbNode.path("mt20id").asText());
                        pfmc.setEntrpsnmP(dbNode.path("entrpsnmP").asText());
                        pfmc.setVisit(dbNode.path("visit").asText());
                        pfmc.setPrfnm(dbNode.path("prfnm").asText());
                        pfmc.setGenrenm(dbNode.path("genrenm").asText());
                        pfmc.setPoster(dbNode.path("poster").asText());
                        pfmc.setChild(dbNode.path("child").asText());

                        // 관련 링크(relates) 정보 설정
                        JsonNode relatesNode = dbNode.path("relates").path("relate");
                        if (relatesNode.isArray()) {
                            for (JsonNode relateNode : relatesNode) {
                                String relatenm = relateNode.path("relatenm").asText();
                                String relateurl = relateNode.path("relateurl").asText();
                                RelateInfo relateInfo = new RelateInfo(relatenm, relateurl);
                                pfmc.addRelateInfo(relateInfo);
                            }
                        }

                        // 스타일 URL(styurls) 정보 설정
                        JsonNode styurlsNode = dbNode.path("styurls").path("styurl");
                        if (styurlsNode.isArray()) {
                            for (JsonNode styurlNode : styurlsNode) {
                                pfmc.addStyurl(styurlNode.asText());
                            }
                        }
                        pfmcRepository.deleteAll();

                        // DB 저장
                        pfmcRepository.save(pfmc);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
