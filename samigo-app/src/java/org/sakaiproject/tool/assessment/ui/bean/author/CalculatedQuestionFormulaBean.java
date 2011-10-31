/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/sam/trunk/samigo-app/src/java/org/sakaiproject/tool/assessment/ui/bean/author/MatchItemBean.java $
 * $Id: MatchItemBean.java 59684 2009-04-03 23:33:27Z arwhyte@umich.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2004, 2005, 2006, 2008, 2009 The Sakai Foundation
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

package org.sakaiproject.tool.assessment.ui.bean.author;

import java.io.Serializable;

/**
 * CalculatedQuestionFormulaBean contains the formula for the answer for a
 * Calculated question.  The formula contains references to CalculatedQuestionVariableBeans,
 * which allows a test to have different variables and answers each time that it is taken.
 * @author mgillian
 */
public class CalculatedQuestionFormulaBean implements Serializable {

    private static final long serialVersionUID = 1747088544228808857L;
    private Long sequence;
    private String name;
    private String text;
    private String tolerance;
    private String decimalPlaces;
    private static final String DEFAULT_TOLERANCE = "0.01";
    private static final String DEFAULT_FORMULA = "0";
    private static final String DEFAULT_DECIMAL_PLACES = "0";
    
    public CalculatedQuestionFormulaBean() {
        this.text = DEFAULT_FORMULA;
        this.tolerance = DEFAULT_TOLERANCE;
        this.decimalPlaces = DEFAULT_DECIMAL_PLACES;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }
    
    public Long getSequence() {
        return this.sequence;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        if (this.text == null) {
            return DEFAULT_FORMULA;
        }
        return this.text;
    }

    public void setTolerance(String tolerance) {
        this.tolerance = tolerance;
    }

    public String getTolerance() {
        if (this.tolerance == null) {
            return DEFAULT_TOLERANCE;
        }
        return this.tolerance;
    }

    public void setDecimalPlaces(String decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    public String getDecimalPlaces() {
        if (this.decimalPlaces == null) {
            return DEFAULT_DECIMAL_PLACES;
        }
        return this.decimalPlaces;
    }
}
