package org.sakaiproject.tool.assessment.entity.impl;

import org.sakaiproject.entitybroker.entityprovider.CoreEntityProvider;
import org.sakaiproject.entitybroker.entityprovider.capabilities.AutoRegisterEntityProvider;
import org.sakaiproject.tool.assessment.entity.api.PublishedAssessmentEntityProvider;
import org.sakaiproject.tool.assessment.facade.PublishedAssessmentFacade;
import org.sakaiproject.tool.assessment.services.assessment.PublishedAssessmentService;

/**
 * Entity Provider impl for samigo PublishedAssessments
 * 
 * @author Joshua Ryan  josh@asu.edu  alt^I
 *
 */
public class PublishedAssessmentEntityProviderImpl implements PublishedAssessmentEntityProvider, CoreEntityProvider, AutoRegisterEntityProvider {

  public String getEntityPrefix() {
    return ENTITY_PREFIX;
  }

  public boolean entityExists(String id) {
    boolean rv = false;

    PublishedAssessmentService service = new PublishedAssessmentService();

    //TODO: Should we refrence Published Assessments via ID or via alias as they are now?
    PublishedAssessmentFacade pub = service.getPublishedAssessmentIdByAlias(id);
    if (pub != null) {
      rv = true;
    }

    return rv;
  }
}
