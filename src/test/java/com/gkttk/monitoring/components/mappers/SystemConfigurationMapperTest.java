package com.gkttk.monitoring.components.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.gkttk.monitoring.models.dtos.SystemConfigurationDto;
import com.gkttk.monitoring.models.entities.SystemConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class SystemConfigurationMapperTest {

  private static final Long ID = 1L;
  private static final String KEY = "test_key";
  private static final String VALUE = "test_value";
  private final SystemConfigurationMapper mapper =
      Mappers.getMapper(SystemConfigurationMapper.class);

  @Test
  public void testMapperTurnEntityIntoDto() {
    // given
    SystemConfiguration configuration = new SystemConfiguration();
    configuration.setId(ID);
    configuration.setKey(KEY);
    configuration.setValue(VALUE);
    // when
    SystemConfigurationDto result = mapper.mapToDto(configuration);
    // then
    assertEquals(configuration.getId(), result.getId());
    assertEquals(configuration.getKey(), result.getKey());
    assertEquals(configuration.getValue(), result.getValue());
  }

  @Test
  public void testMapperTurnDtoIntoEntity() {
    // given
    SystemConfigurationDto dto = new SystemConfigurationDto();
    dto.setId(ID);
    dto.setKey(KEY);
    dto.setValue(VALUE);
    // when
    SystemConfiguration result = mapper.mapToEntity(dto);
    // then
    assertEquals(dto.getId(), result.getId());
    assertEquals(dto.getKey(), result.getKey());
    assertEquals(dto.getValue(), result.getValue());
  }
}
