package com.petrhalik.converter;

import com.petrhalik.converter.config.SwaggerConfig;
import com.petrhalik.converter.config.WebConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(value = { WebConfig.class, SwaggerConfig.class})
public class ConverterApplication {

  public static void main(String[] args) {
    SpringApplication.run(ConverterApplication.class, args);
  }

}
