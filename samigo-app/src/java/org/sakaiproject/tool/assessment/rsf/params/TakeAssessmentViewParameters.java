package org.sakaiproject.tool.assessment.rsf.params;

import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

/**
 * 
 * @author Joshua Ryan josh@asu.edu alt^I
 *
 */
public class TakeAssessmentViewParameters extends SimpleViewParameters {
  public TakeAssessmentViewParameters(String id) {
    super("/samigo/servlet/Login");
    this.id = id;
    this.fromDirect = "true";
    this.viewID = "/samigo/servlet/Login";
  }
  
  public String id;
  public String fromDirect;
}
