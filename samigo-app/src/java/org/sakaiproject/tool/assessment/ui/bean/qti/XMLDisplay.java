/**********************************************************************************
 * $URL$
 * $Id$
 ***********************************************************************************
 *
 * Copyright (c) 2004, 2005, 2006 The Sakai Foundation.
 *
 * Licensed under the Educational Community License, Version 1.0 (the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **********************************************************************************/


package org.sakaiproject.tool.assessment.ui.bean.qti;

import java.io.Serializable;

/**
 * <p>Bean for QTI XML or XML fragments and descriptive information. </p>
 * <p>Used to maintain information or to dump XML to client.</p>
 * <p>Copyright: Copyright (c) 2004 Sakai</p>
 * @author Ed Smiley esmiley@stanford.edu
 * @version $Id$
 */

public class XMLDisplay implements Serializable
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 40775142933316177L;
private String name;
  private String description;
  private String xml;
  private String id;

  public XMLDisplay()
  {
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public String getXml()
  {
    return xml;
  }

  public void setXml(String xml)
  {
    this.xml = xml;
  }

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }



}
