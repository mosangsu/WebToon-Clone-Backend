package com.lezhin.clone.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.lezhin.clone.backend.enums.CoinType;
import com.lezhin.clone.backend.enums.EpisodeSchedule;
import com.lezhin.clone.backend.enums.RankingType;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @NonNull
    public static String[] allowedOrigins = {"http://localhost:3000"};

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins).allowedMethods("GET", "POST", "PUT", "DELETE").allowCredentials(true);
    }

    @Override
    public void addFormatters(@NonNull FormatterRegistry registry) {
        registry.addConverter(new EpisodeSchedule.EpisodeScheduleConverter());
        registry.addConverter(new RankingType.RankingTypeConverter());
        registry.addConverter(new CoinType.CoinTypeConverter());
    }
}