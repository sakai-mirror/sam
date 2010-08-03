/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2004 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    nor may "Apache" appear in their name, without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 * Portions of this software are based upon public domain software
 * originally written at the National Center for Supercomputing Applications,
 * University of Illinois, Urbana-Champaign.
 *
 */


var highestOptionId = +25;
var highestItemId = +25;
var additionalOptionsGroupSize = +3;
var additionalItemsGroupSize = +3;


$(document).ready(function(){
	
	//************* OPTIONS ********************
	//Show/Hide Simple or Rich-Text Options based on user selection
	var radiosSimpleOrRichAnswerOptions = $("input[name=itemForm:emiAnswerOptionsSimpleOrRich]");
	radiosSimpleOrRichAnswerOptions.bind('click', function(){
		if (this.value === "0"){
			//show simple
			$("#emiAnswerOptionsRich").hide();
			$("#emiAnswerOptionsSimple").show();
		}else if (this.value === "1"){
			//show rich
			$("#emiAnswerOptionsRich").show();
			$("#emiAnswerOptionsSimple").hide();
		}
	});
	
	//Remove Option
	for (i=0; i<=highestOptionId; i++) {
		var emiOptionRemoveLink = $("a[id=itemForm:emiAnswerOptions:" + i + ":RemoveLink]");
		emiOptionRemoveLink.bind('click', function() {
			var optionId = +($(this).attr("id").split(":")[2]);
			for (j=optionId; j<highestOptionId; j++) {
				var k = +j+1;
				var optionText1 = $("input[id=itemForm:emiAnswerOptions:" + j + ":Text]");
				var optionText2 = $("input[id=itemForm:emiAnswerOptions:" + k + ":Text]");
				optionText1.val(optionText2.val());
				//if reached the visible-invisible boundary, hide the last visible row
				if (optionText1.is(':visible') && optionText2.is(':hidden')) {
					$("table[id=itemForm:emiAnswerOptions:" + j + ":Row]").parent().parent().hide();
					break;
				}
			}
			var lastOptionText = $("input[id=itemForm:emiAnswerOptions:" + highestOptionId + ":Text]");
			lastOptionText.val("");
			$("table[id=itemForm:emiAnswerOptions:" + highestOptionId + ":Row]").parent().parent().hide();
			return false;
	    });
	}
	
	
	//Add Options
	var emiOptionAddLink = $("a[id=itemForm:addEmiAnswerOptionsLink]");
	emiOptionAddLink.bind('click', function(){
		for (i=0; i<highestOptionId; i++) {
			var j = +i+1;
			var optionText1 = $("input[id=itemForm:emiAnswerOptions:" + i + ":Text]");
			var optionText2 = $("input[id=itemForm:emiAnswerOptions:" + j + ":Text]");
			//if reached the visible-invisible boundary, show additional rows
			if (optionText1.is(':visible') && optionText2.is(':hidden')) {
				for (k=0; k<additionalOptionsGroupSize; k++) {
					var l = +j+k;
					if (l<=highestOptionId) {
						$("table[id=itemForm:emiAnswerOptions:" + l + ":Row]").parent().parent().show();
					}
				}
				return false;
			}
		}
		return false;
	});
	
	
	//************* ITEMS ********************
	//Remove Items
	for (i=0; i<=highestOptionId; i++) {
		var emiItemRemoveLink = $("a[id=itemForm:emiQuestionAnswerCombinations:" + i + ":RemoveLink]");
		emiItemRemoveLink.bind('click', function() {
			var itemId = +($(this).attr("id").split(":")[2]);
			for (j=itemId; j<highestItemId; j++) {
				var k = +j+1;
				var itemText1 = $("textarea[id^=itemForm:emiQuestionAnswerCombinations:" + j +"]");
				var itemText2 = $("textarea[id^=itemForm:emiQuestionAnswerCombinations:" + k +"]");
				itemText1.val(itemText2.val());
				//if reached the visible-invisible boundary, hide the last visible row
				if (itemText1.is(':visible') && itemText2.is(':hidden')) {
					$("table[id=itemForm:emiQuestionAnswerCombinations:" + j + ":Row]").parent().parent().hide();
					break;
				}
			}
			var lastItemText = $("input[id^=itemForm:emiQuestionAnswerCombinations:" + highestItemId +"]");
			lastItemText.val("");
			$("table[id=itemForm:emiQuestionAnswerCombinations:" + highestItemId + ":Row]").parent().parent().hide();
			return false;
	    });
	}
	

	
	//Add Items
	var emiItemAddLink = $("a[id=itemForm:addEmiQuestionAnswerCombinationsLink]");
	emiItemAddLink.bind('click', function(){
		for (i=0; i<highestItemId; i++) {
			var j = +i+1;
			var row1 = $("table[id=itemForm:emiQuestionAnswerCombinations:" + i + ":Row]");
			var row2 = $("table[id=itemForm:emiQuestionAnswerCombinations:" + j + ":Row]");
			//if reached the visible-invisible boundary, show additional rows
			if (row1.is(':visible') && row2.is(':hidden')) {
				for (k=0; k<additionalItemsGroupSize; k++) {
					var l = +j+k;
					if (l<=highestItemId) {
						$("table[id=itemForm:emiQuestionAnswerCombinations:" + l + ":Row]").parent().parent().show();
					}
				}
				return false;
			}
		}
		return false;
	});
	
	
	
/*	var textPasteAnswerOptions = $("textarea[name=itemForm:emiAnswerOptionsPaste]");
	textPasteAnswerOptions.bind('blur', function(){
		var ta = $("textarea[name=itemForm:emiAnswerOptionsPaste]");
		alert(ta.val());
	});
*/	
	
	
	//load triggers
	var radioChecked = $("input[name=itemForm:emiAnswerOptionsSimpleOrRich]:checked");
	radioChecked.trigger('click');
	
	
	//hide excess Options at start
	var firstOptionText = $("input[id=itemForm:emiAnswerOptions:" + 0 + ":Text]");
	if (firstOptionText.val() === "") {
		for (i=4; i<26; i++) {
			$("table[id=itemForm:emiAnswerOptions:" + i + ":Row]").parent().parent().hide();
		}
	}
	else {
		for (i=0; i<26; i++) {
			var optionText = $("input[id=itemForm:emiAnswerOptions:" + i + ":Text]");
			if (optionText.val() === "") {
				$("table[id=itemForm:emiAnswerOptions:" + i + ":Row]").parent().parent().hide();
			}
		}
	}
	
	//hide excess Items at start
	var firstItemText = $("textarea[id^=itemForm:emiQuestionAnswerCombinations:0]");
	if (firstItemText.val() === "") {
		for (i=4; i<26; i++) {
			$("table[id=itemForm:emiQuestionAnswerCombinations:" + i + ":Row]").parent().parent().hide();
		}
	}
	else {
		for (i=0; i<26; i++) {
			var itemText = $("textarea[id^=itemForm:emiQuestionAnswerCombinations:" + i +"]");
			if (itemText.val() === "") {
				$("table[id=itemForm:emiQuestionAnswerCombinations:" + i + ":Row]").parent().parent().hide();
			}
		}
	}
	

	
	
	//Vertically Align the Pasted Options Table Container
	$("textarea[id=itemForm:emiAnswerOptionsPaste]").parent().parent().parent().parent().parent().css('vertical-align','top');
	$("table[id=itemForm:emiAnswerOptions]").parent().css('vertical-align','top');
	
	
	
	
});

//*************** JQuery Functions Above ******************



var checkflag = "false";

function checkAll(field) {
if (field != null) {
  if (field.length >0){
// for more than one checkbox
    if (checkflag == "false") {
       for (i = 0; i < field.length; i++) {
           field[i].checked = true;}
       checkflag = "true";
       return "Uncheck all"; }
    else {
       for (i = 0; i < field.length; i++) {
           field[i].checked = false; }
       checkflag = "false";
       return "Check all"; }
  }
  else {
// for only one checkbox
    if (checkflag == "false") {
  field.checked = true;
  checkflag = "true";
  return "Uncheck all"; }
else {
  field.checked = false; 
  checkflag = "false";
  return "Check all"; }

   }
}
}


function uncheck(field){
      field.checked = false; 
  checkflag = "false";
    return "uncheck"; 
}



// this is for multiple choice authoring. uncheck all other radio buttons when one is checked, to make it  behave like they are in an array.

function uncheckOthers(field){
 var fieldname = field.getAttribute("name");
 var tables= document.getElementsByTagName("TABLE");
 var prevCorrectBtn=null;

 for (var i = 0; i < tables.length; i++) {
    if ( tables[i].id.indexOf("mcradiobtn") >=0){
       var radiobtn = tables[i].getElementsByTagName("INPUT")[0];
       if (fieldname!=radiobtn.getAttribute("name")){
          if (radiobtn.checked){
             prevCorrectBtn=radiobtn.getAttribute("name");
          }
          radiobtn.checked = false;
       }
    }
 }
 
var selectId =  field.getAttribute("value");
var inputhidden = document.getElementById("itemForm:selectedRadioBtn");
inputhidden.setAttribute("value", selectId);
swtichPartialCredit(fieldname,prevCorrectBtn); 
}

function swtichPartialCredit(newCorrect,oldCorrect){
   var toggleDiv=document.getElementById('partialCredit_toggle');
   if( typeof(toggleDiv) == 'undefined' ||toggleDiv == null){
      return;
   }
   else{
       //setting old one to zero
       if(oldCorrect!=null && oldCorrect!='undefined'){
             var position= oldCorrect.split(":");
             var  prevcorrId="itemForm:mcchoices:"+position[2]+":partialCredit";
             var pInput= document.getElementById(prevcorrId);
             pInput.valueOf().value=0;
             pInput.style.borderStyle = "solid double";
             pInput.style.borderColor="red";
			 pInput.disabled=false;
             pInput.focus();
             var reminderTextId="itemForm:mcchoices:"+position[2]+":partialCreditReminder";
             var reminderTexElement= document.getElementById(reminderTextId);
             reminderTexElement.style.visibility="visible";
         }
         //setting new one to 100 
         position= newCorrect.split(":");
         var currCorrId="itemForm:mcchoices:"+position[2]+":partialCredit";
         var correctPInput= document.getElementById(currCorrId);
         correctPInput.valueOf().value=100;
		 correctPInput.disabled=true;
  }
}

function resetInsertAnswerSelectMenus(){
  var selectlist = document.getElementsByTagName("SELECT");

  for (var i = 0; i < selectlist.length; i++) {
        if ( selectlist[i].id.indexOf("insertAdditionalAnswerSelectMenu") >=0){
          selectlist[i].value = 0;
        }
  }

  var toggleDiv=document.getElementById('partialCredit_toggle');
  if( typeof(toggleDiv) == 'undefined' || toggleDiv == null){ return;}
  else{
    var QtypeTable=document.getElementById('itemForm:chooseAnswerTypeForMC');
    QtypeTable.rows[0].cells[0].appendChild(toggleDiv); 
  }
}

function disablePartialCreditField(){
 var inputs= document.getElementsByTagName("INPUT");

 for (var i = 0; i < inputs.length; i++) {
    if (inputs[i].name.indexOf("mcradiobtn") >= 0){
	   var radiobtn = inputs[i];
       if (radiobtn.checked){
          var subElement= radiobtn.name.split(":");
          var currCorrId="itemForm:mcchoices:"+subElement[2]+":partialCredit";
		  var correctPInput= document.getElementById(currCorrId);
          correctPInput.disabled=true;
       }   
    }
 }
}

function clickAddChoiceLink(){

var newindex = 0;
for (i=0; i<document.links.length; i++) {
  if ( document.links[i].id.indexOf("hiddenAddChoicelink") >=0){
    newindex = i;
    break;
  }
}

document.links[newindex].onclick();
}


//gopalrc - added 23 Nov 2009
function clickAddEmiAnswerOptionsLink(){
	
	//alert("clickAddEmiAnswerOptionsLink");

	var newindex = 0;
	for (i=0; i<document.links.length; i++) {
	  if ( document.links[i].id.indexOf("hiddenAddEmiAnswerOptionsActionlink") >=0){
	    newindex = i;
	    break;
	  }
	}

	document.links[newindex].onclick();
}


//gopalrc - added 23 Nov 2009
function clickAddEmiQuestionAnswerCombinationsLink(){

	//alert("clickAddEmiQuestionAnswerCombinationsLink");

	var newindex = 0;
	for (i=0; i<document.links.length; i++) {
	  if ( document.links[i].id.indexOf("hiddenAddEmiQuestionAnswerCombinationsActionlink") >=0){
	    newindex = i;
	    break;
	  }
	}

	document.links[newindex].onclick();
}



/*

//gopalrc - added Jul 2010
function setEmiAnswerOptionsSimpleOrRich() {
	//var isSimple = document.forms['itemForm']['itemForm:emiAnswerOptionsSimpleOrRich'][0].checked;
	var isSimple = $("input[name=itemForm:emiAnswerOptionsSimpleOrRich]:eq(0):checked");
	if (isSimple) {
		$("#emiAnswerOptionsRich").hide();
		$("#emiAnswerOptionsSimple").show();
	}
	else {
		$("#emiAnswerOptionsSimple").hide();
		$("#emiAnswerOptionsRich").show();
	}
}


//gopalrc - added Jul 2010
function showLayer(whichLayer)
{
  var elem, vis;
  elem = getElementWithId(whichLayer);
  vis = elem.style;
  vis.display = 'block';
}

//gopalrc - added Jul 2010
function hideLayer(whichLayer)
{
  var elem, vis;
  elem = getElementWithId(whichLayer);
  vis = elem.style;
  vis.display = 'none';
}


//gopalrc - added Jul 2010
function getElementWithId(id){
    var obj;
    if(document.getElementById){
         Prefer the widely supported W3C DOM method, if
           available:-
        
        obj = document.getElementById(id);
    }else if(document.all){
         Branch to use document.all on document.all only
           browsers. Requires that IDs are unique to the page
           and do not coincide with NAME attributes on other
           elements:-
        
        obj = document.all[id];
    }else if(document.layers){
         Branch to use document.layers, but that will only work for
           CSS positioned elements and LAYERs that are not nested. A
           recursive method might be used instead to find positioned
           elements within positioned elements but most DOM nodes on
           document.layers browsers cannot be referenced at all.
        
        obj = document.layers[id];
    }
     If no appropriate/functional element retrieval mechanism
       exists on this browser this function returns null:-
    
    return obj||null;
}
*/



function countNum(){
  var spanList= document.getElementsByTagName("SPAN");
  var count=1;
  for (var i = 0; i < spanList.length; i++) 
    {
        if(spanList[i].id.indexOf("num")>=0)
         {
           spanList[i].innerHTML = count;
           count++;
         }
    }
  
}

/*
 * document.htmlareas will be undefined if the wysiwyg is anything
 * other then htmlarea, so we know if the onsubmit trick is needed
 * to get htmlarea to work right or not
 *
 */
function editorCheck(){
   if (document.htmlareas==undefined) {
      return true;
   }
   else {
      return false;
   }
}


// this is to disable the 'copy' button when importing questions from pool to assessement. itemtype =10

var disabledImport= 'false';
function disableImport(){
  if (disabledImport== 'false'){
    disabledImport= 'true'
  }
  else{ // any subsequent click disable button & action
    if (document.forms[0].elements['editform:import'])
    {
      document.forms[0].elements['editform:import'].disabled=true;
    }
  }
}

function toPoint(id)
{
  var x=document.getElementById(id).value
  document.getElementById(id).value=x.replace(',','.')
}

