/**********************************************************************************
 * $URL$
 * $Id$
 ***********************************************************************************
 *
 * Copyright (c) 2004, 2005, 2006 Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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



package org.sakaiproject.tool.assessment.devtools;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SubstituteProperties
{

  private static Log log = LogFactory.getLog(SubstituteProperties.class);

  private static Properties properties;
  private static Map map;
  private static String propFileName; // resource bundle
  private static String fileName; // JSF source
  private static String FLAG = "\\[\\[";

  public static void main(String[] args)
  {
    for (int i = 0; i < args.length; i++) {
      //log.info("using argument " + i + ":" + args[i]);
    }

    propFileName = args[0];
    fileName = args[1];
    makeProp();
    makeMap();
    //log.info("\n\n" + getReplaced());


  }

  /**
   * read in properties file which is your resource bundle
   */
  private static void makeProp()
  {
    properties = new Properties();
    try {
      properties.load(new FileInputStream(propFileName));
    }
    catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		log.warn("oops " + propFileName);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		log.warn("oops " + propFileName);
	}
  }

  /**
   * reverse lookup, all text to be replaced is tagged with FLAG="[["
   */
  private static void makeMap()
  {
    map = new HashMap();
    Enumeration enumer = properties.keys();

    while (enumer.hasMoreElements())
    {
      Object key = enumer.nextElement();
      map.put(FLAG + properties.get(key), key);
    }
  }
  /**
   * return a String containing the modified file
   * @return String
   */
  /*
  private static String getReplaced()
  {
    String contents = "";
    String line = "";
    try {
      BufferedReader br = new BufferedReader(new FileReader(fileName));
      while (line != null)
      {
        line = br.readLine() ;
        contents += line + "\n";
      }
    }
    catch (Exception ex) {
      log.warn("oops " + fileName);
    }

    Iterator iter = map.keySet().iterator();

    while (iter.hasNext())
    {
      Object key = iter.next();
      Object value = map.get(key);
      String toReplace = (String) key;
      String replaceWith = makeTag((String) value);
      try
      {
        contents = contents.replaceAll(toReplace, replaceWith);
      }
      catch (Exception ex) {
        log.warn("**** UNABLE TO REPLACE ****: '" +toReplace+"'");
      }
    }


    return contents;

  }	
  */
}
