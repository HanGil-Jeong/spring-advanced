package org.example.expert.client;

import org.example.expert.client.dto.WeatherDto;
import org.example.expert.domain.common.exception.ServerException;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class WeatherClient {

    private final RestTemplate restTemplate;

    public WeatherClient(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public String getTodayWeather() {
        ResponseEntity<WeatherDto[]> responseEntity =
                restTemplate.getForEntity(buildWeatherApiUri(), WeatherDto[].class);

        /**
         * if-else문은 if문의 조건이 거짓이면 else문이 작동하는 원리로 알고 있습니다.
         * 리팩도링 전의 코드는 날씨 데이터를 가져오는 것에 실패 여부는 날씨 데이터가 정상적으러 있어도
         * 기타 다른 문제로 인해 실패할 수 있어서 날씨 데이터의 유무랑
         * if-else문으로 역어서 표현하지 않아도 될 것 같아서 else문을 해체하여 가독성을 높혔습니다.
         */
        WeatherDto[] weatherArray = responseEntity.getBody();
        if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            throw new ServerException("날씨 데이터를 가져오는데 실패했습니다. 상태 코드: " + responseEntity.getStatusCode());
        }

        if (weatherArray == null || weatherArray.length == 0) {
            throw new ServerException("날씨 데이터가 없습니다.");
        }


        String today = getCurrentDate();

        for (WeatherDto weatherDto : weatherArray) {
            if (today.equals(weatherDto.getDate())) {
                return weatherDto.getWeather();
            }
        }

        throw new ServerException("오늘에 해당하는 날씨 데이터를 찾을 수 없습니다.");
    }

    private URI buildWeatherApiUri() {
        return UriComponentsBuilder
                .fromUriString("https://f-api.github.io")
                .path("/f-api/weather.json")
                .encode()
                .build()
                .toUri();
    }

    private String getCurrentDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");
        return LocalDate.now().format(formatter);
    }
}
