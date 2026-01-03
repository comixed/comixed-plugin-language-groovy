/*
 * ComiXed - A digital comic book library management application.
 * Copyright (C) 2023, The ComiXed Project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses>
 */

package org.comixedproject.plugins;

import static junit.framework.TestCase.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.comixedproject.model.plugin.LibraryPlugin;
import org.comixedproject.model.plugin.LibraryPluginProperty;
import org.comixedproject.model.plugin.PluginType;
import org.comixedproject.plugins.groovy.GroovyPluginRuntime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GroovyLibraryPluginRuntimeTest {
  private static final String TEST_GOOD_PLUGIN = "src/test/resources/good.cxplugin";
  private static final String TEST_BROKEN_PLUGIN = "src/test/resources/broken.cxplugin";
  private static final String TEST_PLUGIN_NAME = "Good Plugin";
  private static final String TEST_PLUGIN_VERSION = "1.2.3.4";
  private static final String TEST_PROPERTY_NAME_1 = "test_property_1";
  private static final Integer TEST_PROPERTY_1_LENGTH = 32;
  private static final String TEST_PROPERTY_NAME_2 = "test_property_2";
  private static final Integer TEST_PROPERTY_2_LENGTH = 64;
  private static final Long TEST_COMIC_BOOK_ID = 717L;
  private static final List<Long> TEST_COMIC_BOOK_ID_LIST =
      new ArrayList<>(Arrays.asList(TEST_COMIC_BOOK_ID));

  @InjectMocks private GroovyPluginRuntime runner;
  @Mock private LibraryPlugin libraryPlugin;

  @Test
  void execute_single_helloWorld() {
    Mockito.when(libraryPlugin.getFilename()).thenReturn(TEST_GOOD_PLUGIN);

    runner.execute(libraryPlugin, TEST_COMIC_BOOK_ID);
  }

  @Test
  void execute_single_badScript() {
    Mockito.when(libraryPlugin.getFilename()).thenReturn(TEST_BROKEN_PLUGIN);

    runner.execute(libraryPlugin, TEST_COMIC_BOOK_ID);
  }

  @Test
  void execute_single_missingScript() {
    Mockito.when(libraryPlugin.getFilename()).thenReturn(TEST_BROKEN_PLUGIN.substring(1));

    runner.execute(libraryPlugin, TEST_COMIC_BOOK_ID);
  }

  @Test
  void execute_list_helloWorld() {
    Mockito.when(libraryPlugin.getFilename()).thenReturn(TEST_GOOD_PLUGIN);

    runner.execute(libraryPlugin, TEST_COMIC_BOOK_ID_LIST);
  }

  @Test
  void execute_list_badScript() {
    Mockito.when(libraryPlugin.getFilename()).thenReturn(TEST_BROKEN_PLUGIN);

    runner.execute(libraryPlugin, TEST_COMIC_BOOK_ID_LIST);
  }

  @Test
  void execute_list_missingScript() {
    Mockito.when(libraryPlugin.getFilename()).thenReturn(TEST_BROKEN_PLUGIN.substring(1));

    runner.execute(libraryPlugin, TEST_COMIC_BOOK_ID_LIST);
  }

  @Test
  void getName() {
    final String result = runner.getName(TEST_GOOD_PLUGIN);

    assertNotNull(result);
    assertEquals(TEST_PLUGIN_NAME, result);
  }

  @Test
  void getName_badScript() {
    final String result = runner.getName(TEST_BROKEN_PLUGIN);

    assertNotNull(result);
    assertEquals("", result);
  }

  @Test
  void getName_missingPlugin() {
    final String result = runner.getName(TEST_BROKEN_PLUGIN.substring(1));

    assertNotNull(result);
    assertEquals("", result);
  }

  @Test
  void getVersion() {
    final String result = runner.getVersion(TEST_GOOD_PLUGIN);

    assertNotNull(result);
    assertEquals(TEST_PLUGIN_VERSION, result);
  }

  @Test
  void getVersion_badScript() {
    final String result = runner.getVersion(TEST_BROKEN_PLUGIN);

    assertNotNull(result);
    assertEquals("", result);
  }

  @Test
  void getVersion_missingPlugin() {
    final String result = runner.getVersion(TEST_BROKEN_PLUGIN.substring(1));

    assertNotNull(result);
    assertEquals("", result);
  }

  @Test
  void getPluginType() {
    final PluginType result = runner.getPluginType(TEST_GOOD_PLUGIN);

    assertNotNull(result);
    assertSame(PluginType.Single, result);
  }

  @Test
  void getPluginType_missingScript() {
    final PluginType result = runner.getPluginType(TEST_BROKEN_PLUGIN.substring(1));

    assertNotNull(result);
    assertSame(PluginType.Undefined, result);
  }

  @Test
  void loadProperties() {
    final List<LibraryPluginProperty> result = runner.getProperties(TEST_GOOD_PLUGIN);

    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertTrue(
        result.stream()
            .map(LibraryPluginProperty::getName)
            .toList()
            .contains(TEST_PROPERTY_NAME_1));
    assertTrue(
        result.stream()
            .map(LibraryPluginProperty::getLength)
            .toList()
            .contains(TEST_PROPERTY_1_LENGTH));
    assertTrue(
        result.stream()
            .map(LibraryPluginProperty::getName)
            .toList()
            .contains(TEST_PROPERTY_NAME_2));
    assertTrue(
        result.stream()
            .map(LibraryPluginProperty::getLength)
            .toList()
            .contains(TEST_PROPERTY_2_LENGTH));
  }

  @Test
  void loadProperties_badScript() {
    final List<LibraryPluginProperty> result = runner.getProperties(TEST_BROKEN_PLUGIN);

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void loadProperties_missingPlugin() {
    final List<LibraryPluginProperty> result =
        runner.getProperties(TEST_BROKEN_PLUGIN.substring(1));

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }
}
