package com.joyfarm.farmstival.global;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Utils { // 빈의 이름 - utils

    private final MessageSource messageSource; // 메시지 코드로부터 실제 메시지를 가져오는데 사용
    private final HttpServletRequest request;
    private final DiscoveryClient discoveryClient;

    /* Errors 객체를 받아서 필드명과 그에 대한 오류 메시지를 Map으로 반환하는 메서드 */
    public Map<String, List<String>> getErrorMessages(Errors errors) {
        // FieldErrors
        // FieldError 객체를 가져와서 필드명을 key로, 오류 메시지를 리스트 형태로 value로 가지는 Map으로 변환
        Map<String, List<String>> messages = errors.getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, e -> getCodeMessages(e.getCodes())));

        // GlobalErrors
        List<String> gMessages = errors.getGlobalErrors()
                .stream()
                .flatMap(e -> getCodeMessages(e.getCodes()).stream()).toList();

        if (!gMessages.isEmpty()) {
            messages.put("global", gMessages);
        }
        return messages;
    }

    /* 에러 코드 배열을 받아서 메시지로 변환하는 메서드 */
    public List<String> getCodeMessages(String[] codes) {
        ResourceBundleMessageSource ms = (ResourceBundleMessageSource) messageSource;
        ms.setUseCodeAsDefaultMessage(false);

        List<String> messages = Arrays.stream(codes)
                .map(c -> {
                    try {
                        return ms.getMessage(c, null, request.getLocale());
                    } catch (Exception e) {
                        return "";
                    }
                })
                .filter(s -> !s.isBlank())
                .toList();

        ms.setUseCodeAsDefaultMessage(true);
        return messages;
    }

    /* 코드로 해당하는 메시지를 조회할 수 있는 편의기능 */
    public String getMessage(String code) {
        List<String> messages = getCodeMessages(new String[] {code});

        return messages.isEmpty() ? code : messages.get(0); // 메시지가 없으면 코드 반환, 있으면 메시지 그대로 반환
    }

    public String url(String url) {
        List<ServiceInstance> instances = discoveryClient.getInstances("api-service");

        try{
            return String.format("%s%s", instances.get(0).getUri().toString(), url);
            // 각 서버에서 지원하는 정적 자원의 경로는 게이트웨이 쪽에서 접근하는 것이 바람직 하지 않다!
        }catch (Exception e) {
            return String.format("%s://%s:%d%s%s", request.getScheme(), request.getServerName(), request.getServerPort(), request.getContextPath(), url);
        }
    }

    public String adminUrl(String url){
        List<ServiceInstance> instances = discoveryClient.getInstances("admin-service");
        return String.format("%s%s", instances.get(0).getUri().toString(),url);
    }
}