/**********************************************************************************
 * $URL$
 * $Id$
 ***********************************************************************************
 *
 * Copyright (c) 2004, 2005, 2006, 2008 The Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.opensource.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **********************************************************************************/


package org.sakaiproject.assessment.facade.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.sakaiproject.tool.assessment.data.dao.grading.AssessmentGradingData;
import org.sakaiproject.tool.assessment.data.dao.grading.ItemGradingAttachment;
import org.sakaiproject.tool.assessment.data.dao.grading.ItemGradingData;
import org.sakaiproject.tool.assessment.facade.AssessmentGradingFacadeQueries;
import org.sakaiproject.tool.assessment.services.PersistenceHelper;
import org.springframework.test.AbstractTransactionalSpringContextTests;

public class AssessmentGradingFacadeQueriesTest extends AbstractTransactionalSpringContextTests {
	
	private static final String AGENT_ID = "agent";

	protected String[] getConfigLocations() {
		return new String[] {"/spring-hibernate.xml"};
	}

	/** our query object */
	AssessmentGradingFacadeQueries queries = null;
	Long savedId = null;
	Long item1Id = null;
		
	protected void onSetUpInTransaction() throws Exception {
		queries = new AssessmentGradingFacadeQueries();
		queries.setSessionFactory((SessionFactory)applicationContext.getBean("sessionFactory"));
		//Set the persistance helper
		PersistenceHelper persistenceHelper = new PersistenceHelper();
		persistenceHelper.setDeadlockInterval(3500);
		persistenceHelper.setRetryCount(5);
		queries.setPersistenceHelper(persistenceHelper);
		
		loadData();

	}
	
	
	
	public void testSaveAssesmentGradingData() {
		//A AssemementGradingData to work with
		AssessmentGradingData data = new AssessmentGradingData();
		
		//we expect a failure on this one
		/*FIXME this test should fail with an exception
		 * currently the exceptions are quietly swallowed
		try {
			queries.saveOrUpdateAssessmentGrading(data);
			fail();
		}
		catch (Exception e) {
			//we expect this 
		}
		*/
		
		data.setPublishedAssessmentId(Long.valueOf(1));
		data.setAgentId(AGENT_ID);
		data.setIsLate(false);
		data.setForGrade(false);
		data.setStatus(Integer.valueOf(0));
		
		queries.saveOrUpdateAssessmentGrading(data);
		assertNotNull(data.getAssessmentGradingId());
		
		
		//test saving an answer as part of the question.
		
		
		ItemGradingData item1 = new ItemGradingData();
		item1.setAgentId(data.getAgentId());
		item1.setAssessmentGradingId(data.getAssessmentGradingId());
		item1.setPublishedItemId(1L);
		item1.setPublishedItemTextId(1L);
		//saving the item should add an ID
		queries.saveItemGrading(item1);
		assertNotNull(item1.getItemGradingId());
		
		
		
		ItemGradingData item2 = new ItemGradingData();
		item2.setAgentId(data.getAgentId());
		item2.setAssessmentGradingId(data.getAssessmentGradingId());
		item2.setPublishedItemId(1L);
		item2.setPublishedItemTextId(1L);
		
		
		data.getItemGradingSet().add(item2);
		
		
		/** saving the parent should save the children **/
		queries.saveOrUpdateAssessmentGrading(data);
		assertNotNull(item1.getItemGradingId());
		
		
		
		
	}
	
	public void testLoad() {
		loadData();
		
		AssessmentGradingData result = queries.load(savedId);
		assertNotNull(result);
		assertEquals(result.getItemGradingSet().size(), 2);
		
		
		List<AssessmentGradingData> subs = queries.getAllSubmissions("1");
		assertNotNull(subs);
		assertEquals(4, subs.size());
		
		
	}

	
	public void testGetAttachements() {
		
		ItemGradingData item1 = queries.getItemGrading(item1Id);
		assertTrue(item1.getHasAttachmentSet());
		
		List<ItemGradingAttachment> attachments = queries.getItemGradingAttachmentSet(item1Id);
		assertNotNull(attachments);
		
		//assertEquals(2, attachments.size());
		
	}
	
	/**
	 * Load some test data
	 */
	private void loadData() {
		//set up some data
		AssessmentGradingData data = new AssessmentGradingData();
		data.setPublishedAssessmentId(Long.valueOf(1));
		data.setAgentId(AGENT_ID);
		data.setIsLate(false);
		data.setForGrade(false);
		data.setStatus(Integer.valueOf(0));
		
		queries.saveOrUpdateAssessmentGrading(data);
		
		
		
		
		AssessmentGradingData data2 = new AssessmentGradingData();
		data2.setPublishedAssessmentId(Long.valueOf(1));
		data2.setAgentId("agent2");
		data2.setIsLate(false);
		data2.setForGrade(true);
		data2.setStatus(0);
		queries.saveOrUpdateAssessmentGrading(data2);
		
		
		
		AssessmentGradingData data3 = new AssessmentGradingData();
		data3.setPublishedAssessmentId(Long.valueOf(1));
		data3.setAgentId("agent3");
		data3.setIsLate(false);
		data3.setForGrade(true);
		data3.setStatus(0);
		queries.saveOrUpdateAssessmentGrading(data3);
		
		ItemGradingData item1 = new ItemGradingData();
		item1.setAgentId(data.getAgentId());
		item1.setAssessmentGradingId(data.getAssessmentGradingId());
		item1.setPublishedItemId(1L);
		item1.setPublishedItemTextId(1L);
		
		ItemGradingAttachment at1 = new ItemGradingAttachment();
		at1.setStatus(0);
		at1.setCreatedBy(AGENT_ID);
		at1.setCreatedDate(new Date());
		at1.setLastModifiedBy(AGENT_ID);
		at1.setLastModifiedDate(new Date());
		
		ItemGradingAttachment at2 = new ItemGradingAttachment();
		at2.setStatus(0);
		at2.setCreatedBy(AGENT_ID);
		at2.setCreatedDate(new Date());
		at2.setLastModifiedBy(AGENT_ID);
		at2.setLastModifiedDate(new Date());
		
		
		List<ItemGradingAttachment> list = new ArrayList<ItemGradingAttachment>();
		list.add(at1);
		list.add(at2);
		item1.setItemGradingAttachmentList(list);
		
		ItemGradingData item2 = new ItemGradingData();
		item2.setAgentId(data.getAgentId());
		item2.setAssessmentGradingId(data.getAssessmentGradingId());
		item2.setPublishedItemId(2L);
		item2.setPublishedItemTextId(2L);
		
		
		data.getItemGradingSet().add(item2);
		data.getItemGradingSet().add(item1);
		queries.saveItemGrading(item1);
		queries.saveItemGrading(item2);
		queries.saveOrUpdateAssessmentGrading(data);
		
		savedId = data.getAssessmentGradingId();
		item1Id = item1.getItemGradingId();
		
	}
	
}
