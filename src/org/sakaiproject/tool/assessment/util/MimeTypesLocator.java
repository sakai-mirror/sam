/**********************************************************************************
*
* $Header: /cvs/sakai2/sam/src/org/sakaiproject/spring/SpringBeanLocator.java,v 1.4 2005/05/20 18:49:00 janderse.umich.edu Exp $
*
***********************************************************************************/
/*
* Copyright (c) 2003, 2004, 2005 The Regents of the University of Michigan, Trustees of Indiana University,
*                  Board of Trustees of the Leland Stanford, Jr., University, and The MIT Corporation
*
* Licensed under the Educational Community License Version 1.0 (the "License");
* By obtaining, using and/or copying this Original Work, you agree that you have read,
* understand, and will comply with the terms and conditions of the Educational Community License.
* You may obtain a copy of the License at:
*
*      http://cvs.sakaiproject.org/licenses/license_1_0.html
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
* INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
* AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
* DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
* FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package org.sakaiproject.tool.assessment.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.activation.MimetypesFileTypeMap;
import java.io.File;
 
public class MimeTypesLocator{

  private static Log log = LogFactory.getLog(MimeTypesLocator.class);
  private static MimeTypesLocator instance = null;
  private static MimetypesFileTypeMap map = null;

  public static MimeTypesLocator getInstance(){
    if (instance != null)
      return instance;
    else
      return new MimeTypesLocator();
  }

  public void setMimetypesFileTypeMap(MimetypesFileTypeMap map){
    this.map = map;
  } 

  public MimetypesFileTypeMap getMimetypesFileTypeMap(){
    return map;
  }

  public String getContentType(File file){
    return map.getContentType(file);
  }

}

/**********************************************************************************
*
* $Header: /cvs/sakai2/sam/src/org/sakaiproject/spring/SpringBeanLocator.java,v 1.4 2005/05/20 18:49:00 janderse.umich.edu Exp $
*
***********************************************************************************/

