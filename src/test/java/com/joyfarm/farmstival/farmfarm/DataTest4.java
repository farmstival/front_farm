package com.joyfarm.farmstival.farmfarm;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joyfarm.farmstival.activity.entities.Activity;
import com.joyfarm.farmstival.activity.entities.ActivityTag;
import com.joyfarm.farmstival.activity.repositories.ActivityRepository;
import com.joyfarm.farmstival.activity.repositories.ActivityTagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StringUtils;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@SpringBootTest
//@ActiveProfiles("test")
public class DataTest4 {

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ActivityRepository repository;

    @Autowired
    private ActivityTagRepository tagRepository;


    @Test
    @DisplayName("휴양 마을 api 데이터 조회")
    void test01() throws Exception {

        File file = new File("D:/data/farmData.json");

        Map<String, List<Map<String, String>>> data = om.readValue(file, new TypeReference<>() {
        });

        System.out.println(data);
    }


    @Test
    @DisplayName("휴양 마을 api 데이터 DB에 저장")
    void test02() throws Exception {
        File file = new File("D:/data/farmData.json");

        // JSON을 Map 형태로 읽어오기
        Map<String, Object> jsonData = om.readValue(file, new TypeReference<Map<String, Object>>() {});

        // 'records' 키에 있는 데이터를 추출하여 List로 변환
        List<Map<String, String>> records = (List<Map<String, String>>) jsonData.get("records");

        records.forEach(d -> {
            String tagsTmp = d.get("체험프로그램구분");
            List<ActivityTag> tags = null;
            if (StringUtils.hasText(tagsTmp)) {
                tags = Arrays.stream(tagsTmp.split("\\+"))
                        .map(s -> ActivityTag.builder().tag(s).build())
                        .toList();
                tagRepository.saveAllAndFlush(tags);
            }
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
            Activity items = Activity.builder()
                    .acTags(tags)
                    .townName(Objects.requireNonNullElse(d.get("체험마을명"), ""))
                    .sido(d.get("시도명"))
                    .sigungu(d.get("시군구명"))
                    .activityName(d.get("체험프로그램명"))
                    .facilityInfo(d.get("보유시설정보"))
                    .townArea(d.get("체험휴양마을면적"))
                    .townImage(d.get("체험휴양마을사진"))
                    .doroAddress(d.get("소재지도로명주소"))
                    .ownerName(d.get("대표자명"))
                    .ownerTel(d.get("대표전화번호"))
                    .wwwAddress(d.get("홈페이지주소"))
                    .manageAgency(d.get("관리기관명"))
                    .latitude(d.get("위도") == null ? null : Double.valueOf(d.get("위도")))
                    .longitude(d.get("경도") == null ? null : Double.valueOf(d.get("경도")))
                    .uploadedData(d.get("데이터기준일자") == null ? null : LocalDate.parse(d.get("데이터기준일자"), formatter))
                    .providerCode(d.get("제공기관코드"))
                    .providerName(d.get("제공기관명"))
                    .build();

            repository.saveAndFlush(items);
        });
    }
}


