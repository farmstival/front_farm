package com.joyfarm.farmstival.farmfarm;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joyfarm.farmstival.farmfarm.entities.Festival;
import com.joyfarm.farmstival.farmfarm.repositories.TourPlaceRepository;
import com.joyfarm.farmstival.farmfarm.repositories.TourPlaceTagRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
@ActiveProfiles("test")
public class DataTest2 {

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TourPlaceRepository repository;

    @Autowired
    private TourPlaceTagRepository tagRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    void test1() throws Exception {
        File file = new File("D:/data/fest1.json");

        Map<String, Object> m1 = om.readValue(file, new TypeReference<Map<String, Object>>(){});

        List<Map<String, String>> records = (List<Map<String, String>>) m1.get("records");

        List<Festival> items = records.stream()
                .map(r -> Festival.builder()
                        .title(r.get("축제명"))
                        .latitude(parseDouble(r.get("위도")))
                        .longitude(parseDouble(r.get("경도")))
                        .tel(r.get("전화번호"))
                        .address(r.get("소재지도로명주소"))
                        .location(r.get("개최장소"))
                        .content(r.get("축제내용"))
                        .hostMain(r.get("주최기관명"))
                        .hostSub(r.get("주관기관명"))
                        .startDate(LocalDate.parse(r.get("축제시작일자")))
                        .endDate(LocalDate.parse(r.get("축제종료일자")))
                        .pageLink(r.get("홈페이지주소"))
                        .build()
                ).collect(Collectors.toList());

        items.forEach(System.out::println);
    }

    private Double parseDouble(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null; // 또는 기본값, 예: 0.0
        }
        return Double.valueOf(value);
    }


    @Test
    void test2(){
        String url = "https://apis.data.go.kr/B551011/KorService1/searchFestival1?MobileOS=AND&MobileApp=test&_type=json&eventStartDate=20240101&serviceKey=0Sgv0AKMOayMo1mD5VMSUVb%2BUcFF8gNjLps7PmIgLMFiD8YDLAZ9NDzmF7863XkTC0DkRBCSzqM4RROiej%2BrIw%3D%3D";

        ResponseEntity<String> response = restTemplate.getForEntity(URI.create(url), String.class);

        System.out.println(response);
    }
}
