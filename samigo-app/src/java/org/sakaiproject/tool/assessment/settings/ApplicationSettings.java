/**********************************************************************************
 * $URL$
 * $Id$
 ***********************************************************************************
 *
 * Copyright 2004, 2005, 2006, 2007 Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 *
 *       http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *
 **********************************************************************************/



package org.sakaiproject.tool.assessment.settings;

import java.io.IOException;
import java.util.Properties;
import org.sakaiproject.util.ResourceLoader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author <a href="mailto:lance@indiana.edu">Lance Speelmon </a>
 * @version $Id: ApplicationSettings.java,v 1.1 2004/06/17 19:20:21
 *          lancespeelmon Exp $
 */
public class ApplicationSettings
{
  private static Log log = LogFactory.getLog(ApplicationSettings.class);
  private static boolean disableMultipleChoiceSingle = false;
  private static boolean disableMultipleChoiceMultiple = false;
  private static boolean disableMultipleChoiceSurvey = false;
  private static boolean disableShortAnswerEssay = false;
  private static boolean disableFillInTheBlank = false;
  private static boolean disableFillInNumeric = false;
  private static boolean disableMatching = false;
  private static boolean disableTrueFalse = false;
  private static boolean disableAudioRecording = false;
  private static boolean disableFileUpload = false;
  private static boolean poolingUserAdminDisabled = false;
  private static boolean enableAutoSaveForGrading = false;
  private static String buildVersion;
  private static String buildTime;
  private static String buildTag;

  static
  {
    try
    {
      Properties p = PathInfo.getInstance().getSettingsProperties(
          Constants.SETTINGS_FILE);
      if (p == null)
      {
        throw new RuntimeException("Could not find settings file: "
            + Constants.SETTINGS_FILE);
      }

      Boolean b = null;
      // disableMultipleChoiceSingle
      b = Boolean.valueOf(p.getProperty("disableMultipleChoiceSingle", "false"));
      if (b.booleanValue())
      {
        log.info("disableMultipleChoiceSingle = true");
        disableMultipleChoiceSingle = true;
      }
      // disableMultipleChoiceMultiple
      b = Boolean.valueOf(p.getProperty("disableMultipleChoiceMultiple", "false"));
      if (b.booleanValue())
      {
        log.info("disableMultipleChoiceMultiple = true");
        disableMultipleChoiceMultiple = true;
      }
      // disableMultipleChoiceSurvey
      b = Boolean.valueOf(p.getProperty("disableMultipleChoiceSurvey", "false"));
      if (b.booleanValue())
      {
        log.info("disableMultipleChoiceSurvey = true");
        disableMultipleChoiceSurvey = true;
      }
      // disableShortAnswerEssay
      b = Boolean.valueOf(p.getProperty("disableShortAnswerEssay", "false"));
      if (b.booleanValue())
      {
        log.info("disableShortAnswerEssay = true");
        disableShortAnswerEssay = true;
      }
      // disableFillInTheBlank
      b = Boolean.valueOf(p.getProperty("disableFillInTheBlank", "false"));
      if (b.booleanValue())
      {
        log.info("disableFillInTheBlank = true");
        disableFillInTheBlank = true;
      }
      // disableFillInNumeric
      b = Boolean.valueOf(p.getProperty("disableFillInNumeric", "false"));
      if (b.booleanValue())
      {
        log.info("disableFillInNumeric = true");
        disableFillInNumeric = true;
      }
      // disableMatching
      b = Boolean.valueOf(p.getProperty("disableMatching", "false"));
      if (b.booleanValue())
      {
        log.info("disableMatching = true");
        disableMatching = true;
      }
      // disableTrueFalse
      b = Boolean.valueOf(p.getProperty("disableTrueFalse", "false"));
      if (b.booleanValue())
      {
        log.info("disableTrueFalse = true");
        disableTrueFalse = true;
      }
      // disableAudioRecording
      b = Boolean.valueOf(p.getProperty("disableAudioRecording", "false"));
      if (b.booleanValue())
      {
        log.info("disableAudioRecording = true");
        disableAudioRecording = true;
      }
      // disableFileUpload
      b = Boolean.valueOf(p.getProperty("disableFileUpload", "false"));
      if (b.booleanValue())
      {
        log.info("disableFileUpload = true");
        disableFileUpload = true;
      }
      // poolingUserAdminDisabled
      b = Boolean.valueOf(p.getProperty("poolingUserAdminDisabled", "false"));
      if (b.booleanValue())
      {
        log.info("poolingUserAdminDisabled = true");
        poolingUserAdminDisabled = true;
      }

      // enableAutoSaveForGrading
      b = Boolean.valueOf(p.getProperty("enableAutoSaveForGrading", "false"));
      if (b.booleanValue())
      {
        log.info("enableAutoSaveForGrading= true");
        enableAutoSaveForGrading = true;
      }

      // build information
      final ResourceLoader rb = new ResourceLoader("org.navigoproject.build");
      buildVersion = rb.getString("build.version");
      buildTime = rb.getString("build.time");
      buildTag = rb.getString("build.tag");
      if (log.isInfoEnabled())
      {
        log.info("buildVersion=" + buildVersion);
        log.info("buildTime=" + buildTime);
        log.info("buildTag=" + buildTag);
      }
    }
    catch (IOException e)
    {
      log.fatal(e);
      throw new RuntimeException(e);
    }
  }

  /**
   * @return Returns the disableAudioRecording.
   */
  public static boolean isDisableAudioRecording()
  {
    return disableAudioRecording;
  }

  /**
   * @return Returns the disableFileUpload.
   */
  public static boolean isDisableFileUpload()
  {
    return disableFileUpload;
  }

  /**
   * @return Returns the disableFillInTheBlank.
   */
  public static boolean isDisableFillInTheBlank()
  {
    return disableFillInTheBlank;
  }
  
  /**
   * @return Returns the disableFillNumeric
   */
  public static boolean isDisableFillInNumeric()
  {
    return disableFillInNumeric;
  }

  /**
   * @return Returns the disableMatching.
   */
  public static boolean isDisableMatching()
  {
    return disableMatching;
  }

  /**
   * @return Returns the disableMultipleChoiceMultiple.
   */
  public static boolean isDisableMultipleChoiceMultiple()
  {
    return disableMultipleChoiceMultiple;
  }

  /**
   * @return Returns the disableMultipleChoiceSingle.
   */
  public static boolean isDisableMultipleChoiceSingle()
  {
    return disableMultipleChoiceSingle;
  }

  /**
   * @return Returns the disableMultipleChoiceSurvey.
   */
  public static boolean isDisableMultipleChoiceSurvey()
  {
    return disableMultipleChoiceSurvey;
  }

  /**
   * @return Returns the disableShortAnswerEssay.
   */
  public static boolean isDisableShortAnswerEssay()
  {
    return disableShortAnswerEssay;
  }

  /**
   * @return Returns the disableTrueFalse.
   */
  public static boolean isDisableTrueFalse()
  {
    return disableTrueFalse;
  }

  /**
   * @return Returns the poolingUserAdminDisabled.
   */
  public static boolean isPoolingUserAdminDisabled()
  {
    return poolingUserAdminDisabled;
  }

  /**
   * @return Returns the enableAutoSaveForGrading.
   */
  public static boolean isEnableAutoSaveForGrading()
  {
    return enableAutoSaveForGrading;
  }

  /**
   * @return Returns the buildTag.
   */
  public static String getBuildTag()
  {
    return buildTag;
  }

  /**
   * @return Returns the buildTime.
   */
  public static String getBuildTime()
  {
    return buildTime;
  }

  /**
   * @return Returns the buildVersion.
   */
  public static String getBuildVersion()
  {
    return buildVersion;
  }
}
