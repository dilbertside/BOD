/**
 * BizObjectDocFactoryTest
 */
package com.bs.bod;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * @author dbs
 *
 */
public class BodFactoryTest {

  ObjectMapper mapper = new ObjectMapper();
  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
    mapper.enableDefaultTyping(); // default to using DefaultTyping.OBJECT_AND_NON_CONCRETE
    //mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception {
  }

  /**
   * Test method for {@link com.bs.bod.BodFactory#init(String, String, String, String, String, ConfirmationCode, String)}.
   * @throws JsonProcessingException 
   */
  @Test
  public void testInitFull() throws JsonProcessingException {
    System.out.println();
    System.out.println("+++++++++++++++++  init full ++++++++++++++++++++");
    String referenceId = "refId";
    String logicalId = "logicalId";
    String sysEnvCode = "TEST";
    ConfirmationCode confirmationCode = ConfirmationCode.never;
    String authorizationId= "NONE";
    String taskId= "456";
    String componentId= "788";
    BodFactory.init(sysEnvCode, logicalId, componentId, taskId, referenceId, confirmationCode, authorizationId, null);
    Bod bod = BodFactory.create();
    assertNotNull(bod);
    String json = mapper.writeValueAsString(bod);
    assertNotNull(json);
    System.out.println(json);
    BodFactory.init(null, null, null, null, null, null, null, null);
  }
  /**
   * Test method for {@link com.bs.bod.BodFactory#init(String, String, String, String, String, ConfirmationCode, String)}.
   * @throws JsonProcessingException 
   */
  @Test
  public void testInit1() throws JsonProcessingException {
    System.out.println();
    System.out.println("+++++++++++++++++  init light++ ++++++++++++++++++++");
    String logicalId = "logicalId";
    String sysEnvCode = "TEST";
    ConfirmationCode confirmationCode = ConfirmationCode.never;
    String componentId= "788";
    BodFactory.init(sysEnvCode, logicalId, componentId, confirmationCode);
    Bod bod = BodFactory.create();
    assertNotNull(bod);
    String json = mapper.writeValueAsString(bod);
    assertNotNull(json);
    System.out.println(json);
    BodFactory.init(null, null, null, null, null, null, null, null);
  }
  /**
   * Test method for {@link com.bs.bod.BodFactory#init(String, String, ConfirmationCode)}.
   * @throws JsonProcessingException 
   */
  @Test
  public void testInit2() throws JsonProcessingException {
    System.out.println();
    System.out.println("+++++++++++++++++  init light ++++++++++++++++++++");
    String logicalId = "logicalId";
    ConfirmationCode confirmationCode = ConfirmationCode.never;
    String componentId= "788";
    BodFactory.init(logicalId, componentId, confirmationCode);
    Bod bod = BodFactory.create();
    assertNotNull(bod);
    String json = mapper.writeValueAsString(bod);
    assertNotNull(json);
    System.out.println(json);
    BodFactory.init(null, null, null, null, null, null, null, null);
  }
  /**
   * Test method for {@link com.bs.bod.BodFactory#create()}.
   * @throws JsonProcessingException 
   */
  @Test
  public void testCreate() throws JsonProcessingException {
    System.out.println();
    System.out.println("+++++++++++++++++  create  ++++++++++++++++++++");
    Bod bod = BodFactory.create();
    assertNotNull(bod);
    String json = mapper.writeValueAsString(bod);
    assertNotNull(json);
    System.out.println(json);
  }

  /**
   * Test method for {@link com.bs.bod.BodFactory#createPing()}.
   * @throws JsonProcessingException 
   */
  @Test
  public void testCreatePing() throws JsonProcessingException {
    System.out.println();
    System.out.println("+++++++++++++++++  create Ping  ++++++++++++++++++++");
    Bod bod = BodFactory.createPing();
    assertNotNull(bod);
    String json = mapper.writeValueAsString(bod);
    assertNotNull(json);
    System.out.println(json);
  }
  
  static public DateFormat jsonLogDf = new SimpleDateFormat("yyyy-MMM-dd-HH-mm-ss");
  /**
   * Test method for {@link com.bs.bod.BodFactory#createLoadOnError(Object)}.
   * @throws IOException 
   */
  @Test
  public void testCreatecreateLoadOnError() throws IOException {
    System.out.println();
    System.out.println("+++++++++++++++++  create Load OnError  ++++++++++++++++++++");
    Bod bod = BodFactory.createLoadWithOnError(buildComponent("aa", "bb", "cc"));
    assertNotNull(bod);
    String json = mapper.writeValueAsString(bod);
    assertNotNull(json);
    System.out.println(json);
  }

  @Test
  public void testDeserializationComponentPayload() throws IOException {
    System.out.println();
    System.out.println("+++++++++++++++++  Deserialization Component Payload  ++++++++++++++++++++");
    Bod bod = BodFactory.createLoadWithOnError(buildComponent("aa", "bb", "cc"));
    assertNotNull(bod);
    //String json = mapper.writerWithType(new TypeReference<Component>() { }).writeValueAsString(bod);
    String json = mapper.writeValueAsString(bod);
    assertNotNull(json);
    System.out.println(json);
    //deserialize
    Bod ret = mapper.readValue(json, Bod.class);
    assertTrue(ret.getDataArea().getNoun().getComponent().getClass().equals(Component.class));
  }
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Test
  public void testDeserializationListComponentPayload() throws IOException {
    System.out.println();
    System.out.println("+++++++++++++++++  Deserialization List Component Payload  ++++++++++++++++++++");
    List components = new ArrayList<>(5);
    components.add(buildComponent("aa", "bb", "cc"));
    components.add(buildComponent(RandomStringUtils.randomAlphanumeric(5), RandomStringUtils.randomAlphanumeric(6), RandomStringUtils.randomAlphanumeric(7)));
    components.add(buildComponent(RandomStringUtils.randomAlphanumeric(5), RandomStringUtils.randomAlphanumeric(6), RandomStringUtils.randomAlphanumeric(7)));
    components.add(buildComponent(RandomStringUtils.randomAlphanumeric(5), RandomStringUtils.randomAlphanumeric(6), RandomStringUtils.randomAlphanumeric(7)));
    components.add(buildComponent(RandomStringUtils.randomAlphanumeric(5), RandomStringUtils.randomAlphanumeric(6), RandomStringUtils.randomAlphanumeric(7)));
    Bod bod = BodFactory.createLoadWithOnError(components);
    assertNotNull(bod);
    //String json = mapper.writerWithType(new TypeReference<Component>() { }).writeValueAsString(bod);
    String json = mapper.writeValueAsString(bod);
    assertNotNull(json);
    System.out.println(json);
    //deserialize
    Bod ret = mapper.readValue(json, Bod.class);
    assertTrue(ret.getDataArea().getNoun().getComponent().getClass().equals(ArrayList.class));
    List<Component> components2 = (List)ret.getDataArea().getNoun().getComponent(); 
    assertTrue(components2.get(0).getClass().equals(Component.class));
    Component c1 = components2.get(0);
    assertTrue(c1.strings.get(0).equals("aa"));
  }
  
  private Component buildComponent(String ...strings) {
    Component c = new Component();
    c.total = new Long(strings.length);
    c.timestamp = jsonLogDf.format(new Date());
    c.strings = Arrays.asList(strings);
    return c;
  }

  /**
   * Test method for {@link com.bs.bod.BodFactory#createError(Bod, Exception)}.
   * @throws JsonProcessingException 
   */
  @Test
  public void testCreateError() throws JsonProcessingException {
    System.out.println();
    System.out.println("+++++++++++++++++  create Error  ++++++++++++++++++++");
    Bod bod = null;
    try {
      throw new RuntimeException("test");
    } catch (Exception e) {
      bod = BodFactory.createError(BodFactory.create(), e);
    }
    assertNotNull(bod);
    String json = mapper.writeValueAsString(bod);
    assertNotNull(json);
    System.out.println(json);
  }
  
  /**
   * Test method for {@link com.bs.bod.BodFactory#createError(Exception)}.
   * @throws JsonProcessingException 
   */
  @Test
  public void testCreateErrorNoBod() throws JsonProcessingException {
    System.out.println();
    System.out.println("+++++++++++++++++  create Error noBod  ++++++++++++++++++++");
    Bod bod = null;
    try {
      throw new RuntimeException("test");
    } catch (Exception e) {
      bod = BodFactory.createError(e);
    }
    assertNotNull(bod);
    String json = mapper.writeValueAsString(bod);
    assertNotNull(json);
    System.out.println(json);
  }
  
  /**
   * Test method for {@link com.bs.bod.BodFactory#createControlWithConfirmation(String)}.
   * @throws JsonProcessingException 
   */
  @Test
  public void testCreateControlWithConfirmation() throws JsonProcessingException {
    System.out.println();
    System.out.println("+++++++++++++++++  create Control With Confirmation  ++++++++++++++++++++");
    Bod bod = BodFactory.createControlWithConfirmation("start");
    assertNotNull(bod);
    String json = mapper.writeValueAsString(bod);
    assertNotNull(json);
    System.out.println(json);
  }
  
  /**
   * Test method for {@link com.bs.bod.BodFactory#createWithConfirmationAndRouteAndControl(VerbEnum, String, String, String)}.
   * @throws JsonProcessingException 
   */
  @Test
  public void testCreateWithConfirmationAndRouteAndControl() throws JsonProcessingException {
    System.out.println();
    System.out.println("+++++++++++++++++  create With Confirmation And Route And Control  ++++++++++++++++++++");
    Bod bod = BodFactory.createWithConfirmationAndRouteAndControl(VerbEnum.process, "none", "shell", "fire");
    assertNotNull(bod);
    String json = mapper.writeValueAsString(bod);
    assertNotNull(json);
    System.out.println(json);
  }
}
