package com.coco3x.jnu_course_seat_alert.util;

import com.coco3x.jnu_course_seat_alert.config.exception.ValidationCrawlerException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Component
public class CourseValidationCrawler {
    private static final String cmd = "python3";
    private static final String fileName = "course_validation.py";
    private static final String filePath = "./src/main/java/com/coco3x/jnu_course_seat_alert/util/crawler";
    private final BufferedReader br;
    private final BufferedWriter bw;

    private CourseValidationCrawler(){
        ProcessBuilder pb = new ProcessBuilder(cmd, fileName);
        pb.directory(new File(filePath));
        try {
            Process process = pb.start();
            br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            bw = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //성공 시 과목 이름 반환(사용자가 입력한 이름은 정확하지 않기 때문에 더 많은 결과가 나오는 것을 방지)
    public String validate(String code, String name, String grade){
        try {
            Map<String, String> commandMap = new HashMap<>();
            commandMap.put("code", code);
            commandMap.put("name", name);
            commandMap.put("grade", grade);

            bw.write(CustomJson.convertObject2JsonString(commandMap));
            bw.newLine();
            bw.flush();

            JsonNode response = CustomJson.convertJsonString2JsonNode(br.readLine());
            int errorType = response.get("errorType").asInt();

            if (errorType == 0)
                return response.get("name").asText();
            else if (errorType == 2)
                throw new IllegalArgumentException("유효하지 않은 과목입니다.");
            else
                throw new ValidationCrawlerException("잠시 후 다시 시도해주세요.");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
