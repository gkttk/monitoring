package com.gkttk.monitoring.configurations;

import graphql.language.StringValue;
import graphql.schema.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class GraphQLConfiguration {

  @Bean
  public GraphQLScalarType timestampScalarType() {
    return GraphQLScalarType.newScalar()
        .name("LocalDateTime")
        .description("LocalDateTime scalar")
        .coercing(
            new Coercing<LocalDateTime, String>() {
              @Override
              public String serialize(Object input) {
                if (input instanceof LocalDateTime) {
                  return ((LocalDateTime) input).format(DateTimeFormatter.ISO_DATE_TIME);
                } else {
                  throw new CoercingSerializeException("Not a valid DateTime");
                }
              }

              @Override
              public LocalDateTime parseValue(Object input) throws CoercingParseValueException {
                return LocalDateTime.parse(input.toString(), DateTimeFormatter.ISO_DATE_TIME);
              }

              @Override
              public LocalDateTime parseLiteral(Object input) throws CoercingParseLiteralException {
                if (input instanceof StringValue) {
                  return LocalDateTime.parse(
                      ((StringValue) input).getValue(), DateTimeFormatter.ISO_DATE_TIME);
                }

                throw new CoercingParseLiteralException("Value is not a valid ISO date time");
              }
            })
        .build();
  }

  @Bean
  public RuntimeWiringConfigurer runtimeWiringConfigurer() {
    return wiringBuilder -> wiringBuilder.scalar(timestampScalarType());
  }
}
