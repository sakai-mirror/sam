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
import org.apache.commons.collections.comparators.NullComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sakaiproject.tool.assessment.services.GradingService;

public class CalculatedQuestionBean implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5567978724454506570L;
    private Map<String, CalculatedQuestionFormulaBean> formulas;
    private Map<String, CalculatedQuestionVariableBean> variables;

    public CalculatedQuestionBean() {
        formulas = new HashMap<String, CalculatedQuestionFormulaBean>();
        variables = new HashMap<String, CalculatedQuestionVariableBean>();
    }

    public Map<String, CalculatedQuestionFormulaBean> getFormulas() {
        return formulas;
    }

    /**
     * getFormulasList returns a List of all formulas, sorted by formula name
     * @return
     */
    public List<CalculatedQuestionFormulaBean> getFormulasList() {
        List<CalculatedQuestionFormulaBean> beanList = new ArrayList<CalculatedQuestionFormulaBean>(formulas.values());
        Collections.sort(beanList, new Comparator<CalculatedQuestionFormulaBean>() {
            public int compare(CalculatedQuestionFormulaBean bean1, CalculatedQuestionFormulaBean bean2) {
                return new NullComparator().compare(bean1.getName(), bean2.getName());
            }
        });
        return beanList;
    }
    
    public void addFormula(CalculatedQuestionFormulaBean formula) {
        formulas.put(formula.getName(), formula);
    }

    public CalculatedQuestionFormulaBean getFormula(String name) {
        return formulas.get(name);
    }

    public void removeFormula(String name) {
        formulas.remove(name);
    }

    public Map<String, CalculatedQuestionVariableBean> getVariables() {
        return variables;
    }
    
    /**
     * getVariablesList returns a List of all variables, sorted by variable name
     * @return
     */
    public List<CalculatedQuestionVariableBean> getVariablesList() {
        List<CalculatedQuestionVariableBean> beanList = new ArrayList<CalculatedQuestionVariableBean>(variables.values());
        Collections.sort(beanList, new Comparator<CalculatedQuestionVariableBean>() {
            public int compare(CalculatedQuestionVariableBean bean1, CalculatedQuestionVariableBean bean2) {
                return new NullComparator().compare(bean1.getName(), bean2.getName());
            }
        });
        return beanList;
    }

    public void addVariable(CalculatedQuestionVariableBean variable) {
        variables.put(variable.getName(), variable);
    }

    public CalculatedQuestionVariableBean getVariable(String name) {
        return variables.get(name);
    }
    
    public void removeVariable(String name) {
        variables.remove(name);
    }
    
    public void extractFromInstructions(String instructions) {
        extractFormulasFromInstructions(instructions);
        extractVariablesFromInstructions(instructions);
    }
    
    private void extractFormulasFromInstructions(String instructions) {
        GradingService gs = new GradingService();
        List<String> formulaNames = gs.extractFormulas(instructions);
          
        // add any missing formulas
        for (String formulaName : formulaNames) {
            if (!this.formulas.containsKey(formulaName)) {
                CalculatedQuestionFormulaBean bean = new CalculatedQuestionFormulaBean();
                bean.setName(formulaName);
                bean.setSequence(new Long(this.variables.size() + this.formulas.size() + 1));
                this.addFormula(bean);
            }
        }                 
    }
    
    /**
     * extractVariablesFromInstructions examines the question instructions, pulls 
     * any variables that are not already defined as MatchItemBeans and adds them
     * to the list.
     */
    private void extractVariablesFromInstructions(String instructions) {
        GradingService gs = new GradingService();
        List<String> variableNames = gs.extractVariables(instructions);
        
        // add any missing variables
        for (String variableName : variableNames) {
            if (!this.variables.containsKey(variableName)) {
                CalculatedQuestionVariableBean bean = new CalculatedQuestionVariableBean();
                bean.setName(variableName);
                bean.setSequence(new Long(this.variables.size() + this.formulas.size() + 1));
                this.addVariable(bean);
            }
        }         
    }
}