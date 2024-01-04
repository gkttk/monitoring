package com.gkttk.monitoring.services.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gkttk.monitoring.components.mappers.SystemConfigurationMapper;
import com.gkttk.monitoring.dao.repositories.SystemConfigurationRepository;
import com.gkttk.monitoring.exceptions.EntityNotFound;
import com.gkttk.monitoring.models.dtos.SystemConfigurationDto;
import com.gkttk.monitoring.models.entities.SystemConfiguration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SystemSystemConfigurationServiceImplTest {

  private static final Long ID_1 = 1L;
  private static final String KEY_1 = "Key_1";
  private static final String VALUE_1 = "Value_1";

  private static final Long ID_2 = 2L;
  private static final String KEY_2 = "Key_2";
  private static final String VALUE_2 = "Value_2";
  private static SystemConfiguration configuration1;
  private static SystemConfiguration configuration2;
  @Mock private SystemConfigurationRepository repository;
  @Mock private SystemConfigurationMapper mapper;
  @InjectMocks private SystemSystemConfigurationServiceImpl service;

  @BeforeAll
  static void beforeAll() {
    configuration1 = new SystemConfiguration();
    configuration1.setId(ID_1);
    configuration1.setKey(KEY_1);
    configuration1.setValue(VALUE_1);

    configuration2 = new SystemConfiguration();
    configuration2.setId(ID_2);
    configuration2.setKey(KEY_2);
    configuration2.setValue(VALUE_2);
  }

  @Test
  public void testGetByKeysShouldRequestSystemConfigurationsWithProvidedKeys() {
    // given
    List<String> listOfKeys = List.of(KEY_1, KEY_2);
    List<SystemConfiguration> expectedConfigList = List.of(configuration1, configuration2);
    when(repository.findByKeyIn(listOfKeys)).thenReturn(expectedConfigList);
    // when
    Map<String, SystemConfiguration> result = service.getByKeys(listOfKeys);
    // then
    assertEquals(expectedConfigList.size(), result.size());
    verify(repository).findByKeyIn(listOfKeys);
  }

  @Test
  public void testFindAllShouldRequestAllSystemConfigurations() {
    // given
    List<SystemConfiguration> expectedConfigList = List.of(configuration1, configuration2);
    when(repository.findAll()).thenReturn(expectedConfigList);
    // when
    List<SystemConfigurationDto> result = service.getAll();
    // then
    assertEquals(expectedConfigList.size(), result.size());
    verify(repository).findAll();
  }

  @Test
  public void
      testUpdateValueByIdShouldRequestAllSystemConfigurationsIfEntityWithProvidedIdExists() {
    // given
    String newValue = "new_value";

    SystemConfiguration updatedConfiguration = new SystemConfiguration();
    updatedConfiguration.setId(ID_1);
    updatedConfiguration.setKey(KEY_1);
    updatedConfiguration.setValue(newValue);

    SystemConfigurationDto updatedConfigurationDto = new SystemConfigurationDto();
    updatedConfigurationDto.setId(ID_1);
    updatedConfigurationDto.setKey(KEY_1);
    updatedConfigurationDto.setValue(newValue);

    when(repository.findById(ID_1)).thenReturn(Optional.of(configuration1));
    when(repository.save(updatedConfiguration)).thenReturn(updatedConfiguration);
    when(mapper.mapToDto(updatedConfiguration)).thenReturn(updatedConfigurationDto);
    // when
    SystemConfigurationDto result = service.updateValueById(ID_1, newValue);
    // then
    assertEquals(ID_1, result.getId());
    assertEquals(KEY_1, result.getKey());
    assertEquals(newValue, result.getValue());
    verify(repository).findById(ID_1);
    verify(repository).save(updatedConfiguration);
    verify(mapper).mapToDto(updatedConfiguration);
  }

  @Test
  public void testUpdateValueByIdShouldThrowAnExceptionWhenAnEntityWithProvidedIdIsNotFound() {
    // given
    String newValue = "new_value";
    when(repository.findById(ID_1)).thenReturn(Optional.empty());
    // when
    assertThrows(EntityNotFound.class, () -> service.updateValueById(ID_1, newValue));
    // then
    verify(repository).findById(ID_1);
  }

  @Test
  public void testGetValueByKeyShouldRequestValueByProvidedKeyIfTheKeyExistsInTheDb() {
    // given
    String expectedValue = configuration1.getValue();
    when(repository.findByKey(KEY_1)).thenReturn(Optional.of(configuration1));
    // when
    Optional<String> resultOpt = service.getValueByKey(KEY_1);
    // then
    assertTrue(resultOpt.isPresent());
    resultOpt.ifPresent(
        result -> {
          assertEquals(expectedValue, result);
        });
    verify(repository).findByKey(KEY_1);
  }

  @Test
  public void testGetValueByKeyShouldReturnAnEmptyOptionalIfTheKeyDoesNotExistInTheDb() {
    // given
    when(repository.findByKey(KEY_1)).thenReturn(Optional.empty());
    // when
    Optional<String> resultOpt = service.getValueByKey(KEY_1);
    // then
    assertTrue(resultOpt.isEmpty());
    verify(repository).findByKey(KEY_1);
  }
}
