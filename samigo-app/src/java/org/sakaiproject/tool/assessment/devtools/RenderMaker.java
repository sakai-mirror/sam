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



package org.sakaiproject.tool.assessment.devtools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RenderMaker
{
  private static Log log = LogFactory.getLog(RenderMaker.class);	
  // either run this application from the command line with the input file as an
  // argument, or edit this name to suit your fancy.
  //  private static String fileName = "c:\\Navigo\\webapp\\html\\picker.html";
  private static String fileName = "c:\\divstart.html";

  private static final String QUOTE = "\"";
  private static final String ESCAPEDQUOTE = "\\" + "\"";

  // this provides for the notation to place a variable in the input
  private static final String VARIABLEESCAPE = "%";

  private static final String PLUS = " + ";

  private static String WRITETEXT = "      writer.write(\"";
  private static String ENDWRITETEXT = "\");\n";
  private static String JAVADOC =
      "/**\n" +
      " * <p>Faces render output method .</p>\n" +
      " * <p>Method Generator: " +
      "org.sakaiproject.tool.assessment.devtoolsRenderMaker</p>\n" +
      " *\n" +
      " *  @param context   <code>FacesContext</code> for the current request\n" +
      " *  @param component <code>UIComponent</code> being rendered\n" +
      " *\n" +
      " * @throws IOException if an input/output error occurs\n" +
      " *" + "/\n";
  private static String METHODSIGNATURE = "  public void ";
  private static String METHODSIGNATUREEND =
      "(FacesContext context, UIComponent component)\n" +
      "    throws IOException {\n\n";
  private static String GETAWRITER =
      "      ResponseWriter writer = context.getResponseWriter();\n\n";

  private static String METHODEND = "  }\n\n";

  private static String[] validMethods = {
      "encodeBegin", "encodeChildren", "encodeEnd"
  };


  public RenderMaker()
  {
  }

  public static void main(String[] args)
  {
    if (args.length>0)
    {
      fileName = args[0];
    }
    try
    {
      if (args.length > 2)
      {
        log.debug( makeMethodFromFile(fileName, args[1]));
      }
      else
      {
        log.debug( makeMethodFromFile(fileName, "encodeBegin"));
      }
    }
    catch (IOException ex)
    {
      log.error("ooops");
    }
  }

  public static void stringTest()
  {
    StringReader sr = new StringReader("<table bgcolor=\"#000000\" width=\"100%\" border=\"0\" cellpadding=\"0\"");
    StringReader sr2 = new StringReader("<a href=\"%xxx%\">100%%%</a>");
    try {
      log.debug(makeMethod(sr,  "encodeBegin"));
      log.debug(makeMethod(sr2,  "encodeChildren"));
    }
    catch (Exception ex) {
      log.error("\n\n***********\n\nError in makeMethod(): " + ex );
    }
  }
  public static void fileTest()
  {
    try {
      log.debug(makeMethodFromFile(fileName,  "encodeBegin"));
    }
    catch (Exception ex) {
      log.error("\n\n***********\n\nError in makeMethodFromFile(): " + ex );
    }
  }

  public static String makeMethodFromFile(String fileName, String methodName)
  throws IOException
  {
    return "\n/* *** GENERATOR FILE: " + fileName + "*** */\n" +
        "\n/* *** IF SOURCE DOCUMENT CHANGES YOU NEED TO REGENERATE THIS METHOD" +
        "*** */\n" + makeMethod(new FileReader(fileName), methodName);
  }

  public static String makeMethod(Reader reader, String methodName)
    throws IOException
  {
    // validate you are making a valid method source
    boolean valid =false;
    for (int i = 0; i < validMethods.length; i++) {
      if(validMethods[i].equals(methodName))
      {
        valid = true;
        break;
      }
    }
    if (!valid) throw new IllegalArgumentException(methodName);

    BufferedReader br = new BufferedReader(reader);
    StringBuffer sb = new StringBuffer();

    sb.append(JAVADOC + METHODSIGNATURE + methodName + METHODSIGNATUREEND);
    sb.append(GETAWRITER);

    String buf;

    while((buf = br.readLine())!=null)
    {
      sb.append(makeLine(buf));
    }

    sb.append(METHODEND);

    return sb.toString();
  }

  private static String makeLine(String s){
    return makeWriterWriteText(normalize(s));
  }

  private static String normalize(String abnormal)
  {
    StringBuffer normal = new StringBuffer();
    StringTokenizer st = new StringTokenizer(abnormal, QUOTE + VARIABLEESCAPE, true);

    while(st.hasMoreTokens()){
      String token = st.nextToken();
      if (QUOTE.equals(token))
      {
        normal.append(ESCAPEDQUOTE);
      }
      else
      if (VARIABLEESCAPE.equals(token))
      {
//        normal.append(token);
        normal.append(getEscapedVariableToken(st));
      }
      else
      {
        normal.append(token);
      }
    }


    return normal.toString();
  }

  /**
   * take variable expressed inside "%" escapes
   *
   *
   *  this provides for the notation to place a variable in the input
   *  you MUST add code to initialize the variables
   *  usage:
   *  <a href="%variableName%">%variableName%</a>'  ==>
   *  'writer.writeText("<a href=\""+variableName+"\">" + variableName + "</a>", null);'
   *
   *  escaping out the escape:
   *  '%%%' => '%'
   *
   * @param st a StringTokeinzer tokenizing on the escapes and hitting one
   * @return the concatentation of the variable into the writer
   *
   */
  private static String getEscapedVariableToken(StringTokenizer st)
  {
    String token = "";
    String returnToken = "";

    if (st.hasMoreTokens())
    {
      token = st.nextToken();

      // if it is an escape it is an escaped escape so we return an escape
      if (VARIABLEESCAPE.equals(token))
      {
        returnToken = token;
      }
      else // well, it's not an escape, so we need to concatenate it in
      {
        returnToken = QUOTE + PLUS + token + PLUS + QUOTE;
      }

      // now we throw away the end token
      if (st.hasMoreTokens())
      {
        st.nextToken();
      }
    }
    return returnToken;//QUOTE + PLUS + token + PLUS + QUOTE;
  }

  private static String makeWriterWriteText(String s)
  {
    return WRITETEXT + s + ENDWRITETEXT;
  }



}
