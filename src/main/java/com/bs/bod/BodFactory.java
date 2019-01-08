/**
 * BodFactory
 */
package com.bs.bod;

import com.bs.bod.error.BodError;
import com.bs.bod.error.ErrorType;
import com.bs.bod.error.Severity;

/**
 * Business Object Document (BOD)factory
 * 
 * @author dbs on Dec 25, 2015 11:13:35 AM
 * @since 0.0.1
 * @version 1.0
 * @version 1.1 add route
 * @version 1.2 add {@link BodFactory#createControlWithConfirmation(String)}
 * @version 1.3 add {@link #createWithConfirmationAndRouteAndControl(VerbEnum, String, String, String)} and {@link #createError(Bod, ErrorType, String)} and {@link #createError(Bod, ErrorType, Severity, String)}
 * @version 1.4 add Digest checksum private key for {@link #init(String, String, ConfirmationCode, String)} {@link #init(String, String, String, String, String, ConfirmationCode, String, String)}
 */
public class BodFactory {

  /**
   * initialize permanently specific variables for a sub system
   * 
   * @param sysEnvCode
   *          {@link com.bs.bod.Bod#sysEnvCode}, default is <strong>prod</strong>
   * @param logicalId
   *          {@link com.bs.bod.Sender#logicalId} default is <strong>null</strong>
   * @param componentId
   *          {@link com.bs.bod.Sender#componentId} default is <strong>null</strong>
   * @param taskId
   *          {@link com.bs.bod.Sender#taskId} default is <strong>null</strong>
   * @param referenceId
   *          {@link com.bs.bod.Sender#referenceId} default is <strong>null</strong>
   * @param confirmationCode
   *          {@link com.bs.bod.Sender#confirmationCode}, default is <strong>never</strong>
   * @param authorizationId
   *          {@link com.bs.bod.Sender#authorizationId} default is <strong>null</strong>
   * @param privateKey
   *          {@link com.bs.bod.Bod#__sharedPrivateKey}, default is <strong>changeme</strong>
   */
  public static void init(final String sysEnvCode, final String logicalId, final String componentId, final String taskId, final String referenceId, final ConfirmationCode confirmationCode,
      final String authorizationId, final String privateKey) {
    init(logicalId, componentId, confirmationCode, privateKey);
    Bod.__sysEnvCode = sysEnvCode;
    Sender.__taskId = taskId;
    Sender.__referenceId = referenceId;
    Sender.__authorizationId = authorizationId;
  }

  /**
   * initialize permanently specific variables for a sub system
   * 
   * @param sysEnvCode
   *          {@link com.bs.bod.Bod#sysEnvCode}, default is <strong>prod</strong>
   * @param logicalId
   *          {@link com.bs.bod.Sender#logicalId} default is <strong>null</strong>
   * @param componentId
   *          {@link com.bs.bod.Sender#componentId} default is <strong>null</strong>
   * @param confirmationCode
   *          {@link com.bs.bod.Sender#confirmationCode}, default is <strong>never</strong>
   */
  public static void init(final String sysEnvCode, final String logicalId, final String componentId, final ConfirmationCode confirmationCode) {
    init(logicalId, componentId, confirmationCode);
    Bod.__sysEnvCode = sysEnvCode;
  }

  /**
   * initialize permanently specific variables for a sub system
   * 
   * @param logicalId
   *          {@link com.bs.bod.Sender#logicalId} default is <strong>null</strong>
   * @param componentId
   *          {@link com.bs.bod.Sender#componentId} default is <strong>null</strong>
   * @param confirmationCode
   *          {@link com.bs.bod.Sender#confirmationCode}, default is <strong>never</strong>
   */
  public static void init(String logicalId, String componentId, ConfirmationCode confirmationCode) {
    Sender.__logicalId = logicalId;
    Sender.__componentId = componentId;
    Sender.__confirmationCode = confirmationCode;
  }
  
  /**
   * initialize permanently specific variables for a sub system
   * 
   * @param logicalId
   *          {@link com.bs.bod.Sender#logicalId} default is <strong>null</strong>
   * @param componentId
   *          {@link com.bs.bod.Sender#componentId} default is <strong>null</strong>
   * @param confirmationCode
   *          {@link com.bs.bod.Sender#confirmationCode}, default is <strong>never</strong>
   * @param privateKey
   *          {@link com.bs.bod.Bod#__sharedPrivateKey}, default is <strong>changeme</strong>
   */
  public static void init(final String logicalId, final String componentId, final ConfirmationCode confirmationCode, final String privateKey) {
    init(logicalId, componentId, confirmationCode);
    Bod.__sharedPrivateKey = privateKey;
  }

  /**
   * create a minimal valid BOD
   * 
   * @return a new {@link Bod}, never null
   */
  public static Bod create() {
    return new Bod()/*.removeNoun()*/;
  }
  
  /**
   * Helper to build a ready made {@link Bod} with following main properties:<br>
   * <li>Conversation mode: <strong>OnError</strong> {@link com.bs.bod.ConfirmationCode}</li>
   * <li>verb action: <strong>load</strong> {@link com.bs.bod.VerbEnum#load}</li>
   * <br> 
   * @param payload {@link Object} to serialize in JSON
   * @return a new {@link Bod}, never null
   */
  public static Bod createLoadWithOnError(Object payload) {
    return new Bod().setConfirmationCodeOnError().setVerbAction(VerbEnum.load).setPayload(payload);
  }

  /**
   * Helper to build a ready made {@link Bod} <strong>ping</strong> with following main properties:<br>
   * <li>Conversation mode: <strong>never</strong> {@link com.bs.bod.ConfirmationCode#never}</li>
   * <li>verb action: <strong>ping</strong> {@link com.bs.bod.VerbEnum#ping}</li>
   * <li>Noun is removed</li>
   * <br> 
   * @return a new {@link Bod}, never null
   */
  public static Bod createPing() {
    return new Bod().setConfirmationCodeNever().setVerbAction(VerbEnum.ping)/*.removeNoun()*/;
  }

  /**
   * Helper to create a programmatic error<br>
   * {@link Severity#ERROR} will be set
   * 
   * @param bod {@link Bod} which to add error msg
   * @param e {@link Exception}
   * @return  {@link Bod}, never null
   */
  public static Bod createError(Bod bod, Exception e) {
    if(null == bod){
      bod = new Bod().setConfirmationCodeNever().setVerbAction(VerbEnum.error);
      bod.getDataArea().getNoun().setError(new BodError(e));
    }else{
      if(bod.hasError()){
        bod.getDataArea().getNoun().getError().putError(e);
      }else{
        bod.getDataArea().getNoun().setError(new BodError(e));
      }
    }
    bod.getDataArea().getVerb().setError(true);
    return bod;
  }
  
  /**
   * Helper to create a programmatic error<br>
   * {@link Severity#ERROR} will be set
   * 
   * @param bod {@link Bod} which to add error msg
   * @param errorType {@link ErrorType} 
   * @param error can be an error code or a message code 
   * @return  {@link Bod}, never null
   */
  public static Bod createError(Bod bod, ErrorType errorType, String error) {
    if(null == bod){
      bod = new Bod().setConfirmationCodeNever().setVerbAction(VerbEnum.error);
      bod.getDataArea().getNoun().setError(new BodError(errorType, error));
    }else{
      if(bod.hasError()){
        bod.getDataArea().getNoun().getError().putError(errorType, error);
      }else{
        bod.getDataArea().getNoun().setError(new BodError(errorType, error));
      }
    }
    bod.getDataArea().getVerb().setError(true);
    return bod;
  }
  
  /**
   * Helper to create a programmatic error<br>
   * {@link Severity#ERROR} will be set
   * 
   * @param bod {@link Bod} which to add error msg
   * @param errorType {@link ErrorType}
   * @param severity {@link Severity} severity is set once only
   * @param error can be an error code or a message code 
   * @return  {@link Bod}, never null
   */
  public static Bod createError(Bod bod, ErrorType errorType, Severity severity, String error) {
    if(null == bod){
      bod = new Bod().setConfirmationCodeNever().setVerbAction(VerbEnum.error);
      bod.getDataArea().getNoun().setError(new BodError(errorType, error).setSeverity(severity));
    }else{
      if(bod.hasError()){
        bod.getDataArea().getNoun().getError().putError(errorType, error).setSeverity(severity);
      }else{
        bod.getDataArea().getNoun().setError(new BodError(errorType, error).setSeverity(severity));
      }
    }
    bod.getDataArea().getVerb().setError(true);
    return bod;
  }
  
  /**
   * Helper to create a programmatic error<br>
   * {@link Severity#ERROR} will be set
   * 
   * @param e {@link Exception}
   * @return  {@link Bod}, never null
   */
  public static Bod createError(Exception e) {
    return createError(null, e);
  }
  
  /**
   * Helper to create a programmatic error
   * @param message log String
   * @param severity {@link Severity}
   * @return  {@link Bod}, never null
   */
  public static Bod createLog(String message, Severity severity) {
    return createLog(null, message, severity);
  }
  
  /**
   * Helper to create a programmatic error
   * @param bod which to add error msg
   * @param message log String
   * @param severity {@link Severity}
   * @return  {@link Bod}, never null
   */
  public static Bod createLog(Bod bod, String message, Severity severity) {
    if(null == bod){
      bod = new Bod().setConfirmationCodeNever().setVerbAction(VerbEnum.error);
      bod.getDataArea().getNoun().setError(new BodError(message, severity));
    }else{
      if(bod.hasError()){
        bod.getDataArea().getNoun().getError().setSeverity(severity).setTitle(message);
      }else{
        bod.getDataArea().getNoun().setError(new BodError(message, severity));
      }
    }
    bod.getDataArea().getVerb().setError(true);
    return bod;
  }

  /**
   * Helper to build a ready made {@link Bod} with following main properties:<br>
   * <li>Conversation mode: <strong>Always</strong> {@link com.bs.bod.ConfirmationCode}</li>
   * <li>verb action: <strong>close</strong> {@link com.bs.bod.VerbEnum#close}</li>
   * <br> 
   * @param payload {@link Object} to serialize in JSON
   * @return a new {@link Bod}, never null
   */
  public static Bod createCloseWithConfirmation(Object payload) {
    return new Bod().setConfirmationCodeAlways().setVerbAction(VerbEnum.close).setPayload(payload);
  }
  /**
   * Helper to build a ready made {@link Bod} with following main properties:<br>
   * <li>Conversation mode: <strong>Always</strong> {@link com.bs.bod.ConfirmationCode}</li>
   * <li>verb action: <strong>close</strong> {@link com.bs.bod.VerbEnum#close}</li>
   * <br> 
   * @param payload {@link Object} to serialize in JSON
   * @param route additional filter or route for messaging system
   * @return a new {@link Bod}, never null
   */
  public static Bod createCloseWithConfirmationAndRoute(Object payload, String route) {
    Bod bod = new Bod().setConfirmationCodeAlways().setVerbAction(VerbEnum.close).setPayload(payload);
    bod.getDataArea().getNoun().setRoute(route);
    return bod; 
  }
  
  /**
   * Helper to build a ready made {@link Bod} with following main properties:<br>
   * <li>Conversation mode: <strong>Always</strong> {@link com.bs.bod.ConfirmationCode}</li>
   * <li>verb action: <strong>create</strong> {@link com.bs.bod.VerbEnum#create}</li>
   * <br> 
   * @param payload {@link Object} to serialize in JSON
   * @return a new {@link Bod}, never null
   */
  public static Bod createCreateWithConfirmation(Object payload) {
    return new Bod().setConfirmationCodeAlways().setVerbAction(VerbEnum.create).setPayload(payload);
  }
  
  /**
   * Helper to build a ready made {@link Bod} with following main properties:<br>
   * <li>Conversation mode: <strong>Always</strong> {@link com.bs.bod.ConfirmationCode}</li>
   * <li>verb action: <strong>create</strong> {@link com.bs.bod.VerbEnum#create}</li>
   * <br> 
   * @param payload {@link Object} to serialize in JSON
   * @param route additional filter or route for messaging system
   * @return a new {@link Bod}, never null
   */
  public static Bod createCreateWithConfirmationAndRoute(Object payload, String route) {
    assert route != null : "route cannot be null";
    Bod bod = new Bod().setConfirmationCodeAlways().setVerbAction(VerbEnum.create).setPayload(payload);
    bod.getDataArea().getNoun().setRoute(route);
    return bod; 
  }

  /**
   * Helper to build a ready made {@link Bod} with following main properties:<br>
   * <li>Conversation mode: <strong>Always</strong> {@link com.bs.bod.ConfirmationCode}</li>
   * <br> 
   * @param verb {@link com.bs.bod.VerbEnum} action
   * @param payload {@link Object} to serialize in JSON
   * @param route additional filter or route for messaging system
   * @return a new {@link Bod}, never null
   */
  public static Bod createWithConfirmationAndRoute(VerbEnum verb, Object payload, String route) {
    assert verb != null : "verb cannot be null";
    Bod bod = new Bod().setConfirmationCodeAlways().setVerbAction(verb).setPayload(payload);
    bod.getDataArea().getNoun().setRoute(route);
    return bod; 
  }
  
  /**
   * Helper to build a ready made {@link Bod} <strong>confirm</strong> REPLY with following main properties:<br>
   * <li>Conversation mode: <strong>never</strong> {@link com.bs.bod.ConfirmationCode#never}</li>
   * <li>verb action: <strong>control</strong> {@link com.bs.bod.VerbEnum#confirm}</li>
   * <li>Payload is added</li>
   * <br> 
   * @param request {@link Bod} 
   * @param message {@link String} to send back to requestor
   * @return a new {@link Bod}, never null
   */
  public static Bod createReplyConfirmBod(final Bod request, String message) {
    Bod bod = new Bod().setConfirmationCodeNever().setVerbAction(VerbEnum.confirm).setPayloadMessage(message);
    bod.getApplicationArea().getSender().setReferenceId(request.getBodId());
    return bod;
  }

  /**
   * Helper to build a ready made {@link Bod} <strong>confirm</strong> REPLY with following main properties:<br>
   * <li>Conversation mode: <strong>never</strong> {@link com.bs.bod.ConfirmationCode#never}</li>
   * <li>verb action: <strong>control</strong> {@link com.bs.bod.VerbEnum#confirm}</li>
   * <li>Payload is added</li>
   * <br> 
   * @param request {@link Bod} 
   * @param payload {@link Object} to serialize in JSON
   * @return a new {@link Bod}, never null
   */
  public static Bod createReplyConfirmBod(final Bod request, Object payload) {
    Bod bod = new Bod().setConfirmationCodeNever().setVerbAction(VerbEnum.confirm).setPayload(payload);
    bod.getApplicationArea().getSender().setReferenceId(request.getBodId());
    return bod;
  }
  
  /**
   * Helper to build a ready made {@link Bod} <strong>control</strong> with following main properties:<br>
   * <li>Conversation mode: <strong>always</strong> {@link com.bs.bod.ConfirmationCode#always}</li>
   * <li>verb action: <strong>control</strong> {@link com.bs.bod.VerbEnum#control}</li>
   * <li>Noun is removed</li>
   * <br> 
   * @param control {@link String} to serialize in JSON
   * @return a new {@link Bod}, never null
   */
  public static Bod createControlWithConfirmation(String control) {
    Bod bod = new Bod().setConfirmationCodeAlways().setVerbAction(VerbEnum.control)/*.removeNoun()*/;
    bod.getDataArea().getVerb().setControl(control);
    return bod;
  }

  /**
   * Helper to build a ready made {@link Bod} <strong>control</strong> with following main properties:<br>
   * <li>Conversation mode: <strong>always</strong> {@link com.bs.bod.ConfirmationCode#always}</li>
   * <br> 
   * @param verb {@link com.bs.bod.VerbEnum} action
   * @param payload {@link Object} to serialize in JSON
   * @param route additional filter or route for messaging system
   * @param control {@link String} to serialize in JSON
   * @return a new {@link Bod}, never null
   */
  public static Bod createWithConfirmationAndRouteAndControl(VerbEnum verbEnum, Object payload, String route, String control) {
    Bod bod = new Bod().setConfirmationCodeAlways().setVerbAction(verbEnum).setPayload(payload);
    bod.getDataArea().getVerb().setControl(control);
    bod.getDataArea().getNoun().setRoute(route);
    return bod;
  }

}
