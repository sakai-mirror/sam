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


//gopalrc - global vars
var highestOptionId = +25;
var highestItemId = +25;
var maxAvailableItems = +10;
var additionalOptionsGroupSize = +3;
var additionalItemsGroupSize = +3;
var removeLabel = "X";
var maxErrorsDisplayed = +20;
var ANSWER_OPTION_LABELS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

//gopalrc - jQuery functions
$(document).ready(function(){
	
	//only applies to EMI authoring
	if (!emiAuthoring) return;
	
	//************* VALIDATION *****************
	var error_dialog_title_line1 = $("input[id=error_dialog_title_line1]").val();
	var error_dialog_title_line2 = $("input[id=error_dialog_title_line2]").val();
	var answer_point_value_error = $("input[id=answer_point_value_error]").val();
	var theme_text_error = $("input[id=theme_text_error]").val();
	var simple_text_options_blank_error = $("input[id=simple_text_options_blank_error]").val();
	var number_of_rich_text_options_error = $("input[id=number_of_rich_text_options_error]").val();
	var blank_or_non_integer_item_sequence_error = $("input[id=blank_or_non_integer_item_sequence_error]").val();
	var correct_option_labels_error = $("input[id=correct_option_labels_error]").val();
	var item_text_not_entered_error = $("input[id=item_text_not_entered_error]").val();
	var correct_option_labels_invalid_error = $("input[id=correct_option_labels_invalid_error]").val();
	
	
	
	
	var $errorMessageDialog = $('<div></div>')
	.html('')
	.dialog({
		autoOpen: false,
		title: error_dialog_title_line1+'<br/>'+error_dialog_title_line2
	});
	
	var buttonSave = $("input[value=Save]");
	buttonSave.bind('click', function(){
		var errorMessages = new Array(maxErrorsDisplayed);
		var errorNumber=+0;

		//Validate Answer Point Value
		var answerPointValue = $("input[name=itemForm:answerptr]").val();
		if (answerPointValue.trim()=="" || +answerPointValue==0) {
			errorMessages[errorNumber++] = answer_point_value_error;
		}
		
		
		//Validate Theme Text
		var themeText = $("input[name=itemForm:themetext]").val();
		if (themeText.trim()=="") {
			errorMessages[errorNumber++] = theme_text_error;
		}
		
		
		//Validate Options
		//var simpleOrRichAnswerOptions = $("input[name=itemForm:emiAnswerOptionsSimpleOrRich]").val();
		var simpleOrRichAnswerOptions = $("input[name=itemForm:emiAnswerOptionsSimpleOrRich]:checked").val();
		var optionLabels = "";
		
		if (simpleOrRichAnswerOptions==0) { //simple
			var pastedOptions = $("textarea[id=itemForm:emiAnswerOptionsPaste]").val();
			if (pastedOptions == null || pastedOptions.trim()=="") {
				for (j=0; j<highestOptionId; j++) {
					var optionText = $("input[id=itemForm:emiAnswerOptions:" + j + ":Text]");
					if (optionText.is(':visible') && (optionText.val()==null || optionText.val().trim()=="") ) {
						errorMessages[errorNumber++] = simple_text_options_blank_error;
						break;
					}
					else if (optionText.is(':visible')) {
						var label = ANSWER_OPTION_LABELS.substring(j, j+1);
						optionLabels+=label;
					}
				}
			}
			else if (pastedOptions.split("\n").length < 2) {
				errorMessages[errorNumber++] = "At least 2 pasted options required";
			}
		}
		else { // Rich
			var richAnswerOptionsCount = +$("select[id=itemForm:answerOptionsRichCount]").val();
			if (richAnswerOptionsCount == 0) {
				errorMessages[errorNumber++] = number_of_rich_text_options_error;
			}
			else {
				optionLabels = ANSWER_OPTION_LABELS.substring(0, richAnswerOptionsCount);
			}
		}
		

		//Validate Items
		for (j=0; j<highestItemId; j++) {
			var itemError = false;
			var labelInput = $("input[id=itemForm:emiQuestionAnswerCombinations:" + j + ":Label]");
			if (labelInput && labelInput.is(':visible') && labelInput.val() !== removeLabel)  {
				if (labelInput.val().trim()=="" || /[^0-9]/g.test(labelInput.val())) {
					errorMessages[errorNumber++] = blank_or_non_integer_item_sequence_error + labelInput.val();
					itemError = true;
				}

				var itemText = $("textarea[id^=itemForm:emiQuestionAnswerCombinations:" + j +":]").val();
				if (itemText.trim()=="") {
					errorMessages[errorNumber++] = item_text_not_entered_error + labelInput.val();
					itemError = true;
				}

				var correctOptionLabels = $("input[id=itemForm:emiQuestionAnswerCombinations:" + j + ":correctOptionLabels]").val().toUpperCase();
				if (correctOptionLabels.trim()=="" || /[^a-z,]/gi.test(correctOptionLabels)) {
					errorMessages[errorNumber++] = correct_option_labels_error + labelInput.val();
					itemError = true;
				}
				
				if (optionLabels.length > 0) {
					for (i=0; i<correctOptionLabels.length; i++) {
						thisLabel = correctOptionLabels.substring(i, i+1);
						if (optionLabels.indexOf(thisLabel)==-1) {
							errorMessages[errorNumber++] = correct_option_labels_invalid_error + labelInput.val();
							itemError = true;
							break;
						}
					}
				}
				
				//if (itemError) break;
			}
		}
		
		
		
		if (errorNumber > 0) {
			$errorMessageDialog.dialog('close');
			var messageHTML = "<font color='red'><ul>";
			for (i=0; i<errorNumber; i++) {
				messageHTML += "<li>" + errorMessages[i]+"</li>";
			}
			messageHTML += "<ul/></font>";
			$errorMessageDialog.html(messageHTML);
			$errorMessageDialog.dialog('open');
			//alert(errorMessages);
			return false;
		}
		else {
			return true;
		}
	});
	
	
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
					optionText1.val("");
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
	

	//Hide Pasted if Option Text Entered
	for (i=0; i<=highestOptionId; i++) {
		var emiOptionText = $("input[id=itemForm:emiAnswerOptions:" + i + ":Text]");
		emiOptionText.bind('change', function() {
			if ($(this).val().trim() != "") {
				$("textarea[id=itemForm:emiAnswerOptionsPaste]").hide();
				$("textarea[id=itemForm:emiAnswerOptionsPaste]").val("");
				$("label[id=itemForm:pasteLabel]").hide();
			}
			return false;
	    });
		emiOptionText.trigger('change');
	}
	
	
	
	//************* ITEMS ********************
	//Remove Items
	for (i=0; i<=highestItemId; i++) {
		var emiItemRemoveLink = $("a[id=itemForm:emiQuestionAnswerCombinations:" + i + ":RemoveLink]");
		emiItemRemoveLink.bind('click', function() {
			var itemId = +($(this).attr("id").split(":")[2]);
			var row = $("table[id=itemForm:emiQuestionAnswerCombinations:" + itemId + ":Row]").parent().parent();
			var labelRemove = $("input[id=itemForm:emiQuestionAnswerCombinations:" + itemId + ":Label]");
			labelRemove.val(removeLabel);
			row.hide();
			var seq=+1;
			//Resequence items
			for (j=0; j<highestItemId; j++) {
				var labelInput = $("input[id=itemForm:emiQuestionAnswerCombinations:" + j + ":Label]");
				if (labelInput && labelInput.is(':visible') && labelInput.val() !== removeLabel)  {
					labelInput.val(seq++);
				}
			}
			return false;
	    });
	}
	

	
	//Add Items
	var emiItemAddLink = $("a[id=itemForm:addEmiQuestionAnswerCombinationsLink]");
	emiItemAddLink.bind('click', function(){
		var j=+0;
		var availableItems=0;
		for (i=0; i<highestItemId; i++) {
			var row = $("table[id=itemForm:emiQuestionAnswerCombinations:" + i + ":Row]");
			var labelInput = $("input[id=itemForm:emiQuestionAnswerCombinations:" + i + ":Label]");
			//if reached the visible-invisible boundary, show additional rows
			if (row && !row.is(':visible') && labelInput.val() !== removeLabel) {
				$("table[id=itemForm:emiQuestionAnswerCombinations:" + i + ":Row]").parent().parent().show();
				availableItems++;
				if (availableItems>=maxAvailableItems) {
					alert("Maximum Number of Items Added");
					return false;
				}
				if (++j==additionalItemsGroupSize) return false;
			}
			else {
				availableItems++;
			}
			
			if (availableItems>=maxAvailableItems) {
				alert("Maximum Number of Items Added");
				return false;
			}
		}
		alert("Please save accumulated changes before adding additional items.");
		return false;
	});
	

	//Update CorrectOptionsLabels and RequiredOptionsCount 
	var all_option = $("input[id=all]").val();
	for (i=0; i<=highestItemId; i++) {
		var emiCorrectOptionLabelsInput = $("input[id=itemForm:emiQuestionAnswerCombinations:" + i + ":correctOptionLabels]");
		if (emiCorrectOptionLabelsInput==null) break;
		emiCorrectOptionLabelsInput.bind('change', function() {
			var itemId = +($(this).attr("id").split(":")[2]);
			var requiredOptionsCountSelect = $("select[id=itemForm:emiQuestionAnswerCombinations:" + itemId + ":requiredOptionsCount]");
			var currentSelection = requiredOptionsCountSelect.val();
			
			//var changedCorrectOptions = $(this).val().split(",");
			var maxOptions = $(this).val().length;
			
			requiredOptionsCountSelect.empty();
			requiredOptionsCountSelect.append('<option value="0">' + all_option + '</option>');
			for (j=1; j<=maxOptions; j++) {
				if (j == currentSelection) {
					requiredOptionsCountSelect.append('<option selected="selected" value="'+ j +'">'+ j +'</option>');
				}
				else {
					requiredOptionsCountSelect.append('<option value="'+ j +'">'+ j +'</option>');
				}
			}
			return false;
	    });
		emiCorrectOptionLabelsInput.trigger('change');
	}
	
	//trigger startup events
	var radioSimpleOrRichChecked = $("input[name=itemForm:emiAnswerOptionsSimpleOrRich]:checked");
	radioSimpleOrRichChecked.trigger('click');
	
	
	//hide excess Options at start
	var firstOptionText = $("input[id=itemForm:emiAnswerOptions:" + 0 + ":Text]");
	var isAllNull = true;
	for (i=25; i>=0; i--) {
		var optionText = $("input[id=itemForm:emiAnswerOptions:" + i + ":Text]");
		if (optionText.val() === "" || optionText.val() === null) {
			$("table[id=itemForm:emiAnswerOptions:" + i + ":Row]").parent().parent().hide();
		}
		else {
			isAllNull = false;
			break;
		}
	}
	if (isAllNull) {
		for (i=0; i<4; i++) {
			$("table[id=itemForm:emiAnswerOptions:" + i + ":Row]").parent().parent().show();
		}
	}
	

	//hide excess Items at start
	var firstItemText = $("textarea[id^=itemForm:emiQuestionAnswerCombinations:0]");
	isAllNull = true;
	for (i=25; i>=0; i--) {
		var itemText = $("textarea[id^=itemForm:emiQuestionAnswerCombinations:" + i +"]");
		if (itemText.val() === "") {
			$("table[id=itemForm:emiQuestionAnswerCombinations:" + i + ":Row]").parent().parent().hide();
		}
		else {
			isAllNull = false;
			break;
		}
	}
	if (isAllNull) {
		for (i=0; i<4; i++) {
			$("table[id=itemForm:emiQuestionAnswerCombinations:" + i + ":Row]").parent().parent().show();
		}
	}

	
	//Vertically Align the Pasted Options Table Container
	$("textarea[id=itemForm:emiAnswerOptionsPaste]").parent().parent().parent().parent().parent().css('vertical-align','top');
	$("table[id=itemForm:emiAnswerOptions]").parent().css('vertical-align','top');
	
	$("div[id=portletContent]").css('display','block');
	
	
	
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

/*	
for (i=0; i<=highestItemId; i++) {
	var emiItemRemoveLink = $("a[id=itemForm:emiQuestionAnswerCombinations:" + i + ":RemoveLink]");
	emiItemRemoveLink.bind('click', function() {
		var itemId = +($(this).attr("id").split(":")[2]);
		var lastVisible = 0;
		
		// Have to do this in 2 loops, because the attributes like visibility are also copied
		for (x=itemId; x<highestItemId; x++) {
			y = +x+1;
			var remLink1 = $("a[id=itemForm:emiQuestionAnswerCombinations:" + x + ":RemoveLink]");
			var remLink2 = $("a[id=itemForm:emiQuestionAnswerCombinations:" + y + ":RemoveLink]");
			//if reached the visible-invisible boundary, hide the last visible row
			if (remLink1.is(':visible') && !remLink2.is(':visible')) {
				lastVisible=x;
				break;
			}
		}
alert("A");			
		for (j=itemId; j<highestItemId; j++) {
			var k = +j+1;

			var removeLink1 = $("a[id=itemForm:emiQuestionAnswerCombinations:" + j + ":RemoveLink]");
			var removeLink2 = $("a[id=itemForm:emiQuestionAnswerCombinations:" + k + ":RemoveLink]");

alert("B");			
			
			var row1 = $("table[id=itemForm:emiQuestionAnswerCombinations:" + j + ":Row]").parent().parent();
			var row2 = $("table[id=itemForm:emiQuestionAnswerCombinations:" + k + ":Row]").parent().parent();
			row1.html(row2.clone(true).html());

alert("C");			
			
			//if reached the visible-invisible boundary, hide the last visible row
			if (j === lastVisible) {
alert("D1");			
                row1.hide();
                row1.html(backupEMIItemRow.clone(true).html());
alert("D2");			
                
				break;
			}
		}
		
		lastRow = $("table[id=itemForm:emiQuestionAnswerCombinations:" + highestItemId + ":Row]").parent().parent();
alert("E");			

		lastRow.hide();
		lastRow.html(backupEMIItemRow.clone(true).html());
		return false;
    });
}
*/	


/*	
for (i=0; i<=highestItemId; i++) {
	var emiItemRemoveLink = $("a[id=itemForm:emiQuestionAnswerCombinations:" + i + ":RemoveLink]");
	emiItemRemoveLink.bind('click', function() {
		var itemId = +($(this).attr("id").split(":")[2]);
		for (j=itemId; j<highestItemId; j++) {
			var k = +j+1;

			var removeLink1 = $("a[id=itemForm:emiQuestionAnswerCombinations:" + j + ":RemoveLink]");
			var removeLink2 = $("a[id=itemForm:emiQuestionAnswerCombinations:" + k + ":RemoveLink]");

			var itemText1 = $("textarea[id^=itemForm:emiQuestionAnswerCombinations:" + j +":]");
			var itemText2 = $("textarea[id^=itemForm:emiQuestionAnswerCombinations:" + k +":]");
			itemText1.val(itemText2.val());
			var itemTextId1 = itemText1.attr("id");
			var itemTextId2 = itemText2.attr("id");
			var fckEditor1;
			var fckEditor2;
			
			if (typeof FCKeditorAPI !== "undefined" && itemText1 !== null && itemText2 !== null) {
			    fckEditor1 = FCKeditorAPI.GetInstance(itemTextId1);
			    fckEditor2 = FCKeditorAPI.GetInstance(itemTextId2);
			    if (fckEditor1 && fckEditor2) {
			    	fckEditor1.SetHTML(fckEditor2.GetHTML());
			    }
			    else if (fckEditor1) {
			    	fckEditor1.SetHTML(itemText2.val());
			    }
			}
			var correctOptionLabels1 = $("input[id=itemForm:emiQuestionAnswerCombinations:" + j + ":correctOptionLabels]");
			var correctOptionLabels2 = $("input[id=itemForm:emiQuestionAnswerCombinations:" + k + ":correctOptionLabels]");
			correctOptionLabels1.val(correctOptionLabels2.val());
			
			var requiredAnswersCount1 = $("select[id=itemForm:emiQuestionAnswerCombinations:" + j +":requiredOptionsCount]");
			var requiredAnswersCount2 = $("select[id=itemForm:emiQuestionAnswerCombinations:" + k +":requiredOptionsCount]");
			requiredAnswersCount1.val(requiredAnswersCount2.val());
			//if reached the visible-invisible boundary, hide the last visible row
			
			if (removeLink1.is(':visible') && !removeLink2.is(':visible')) {
				itemText1.val("");
				if (typeof FCKeditorAPI !== "undefined") {
					if (fckEditor1) fckEditor1.SetHTML("");
				}
                $("table[id=itemForm:emiQuestionAnswerCombinations:" + j + ":Row]").parent().parent().hide();
				break;
			}
		}
		
		var lastItemText = $("input[id^=itemForm:emiQuestionAnswerCombinations:" + highestItemId +":]");
		lastItemText.val("");
		var lastCorrectOptionLabels = $("textarea[id=itemForm:emiQuestionAnswerCombinations:" + highestItemId + ":correctOptionLabels]");
		lastCorrectOptionLabels.val("");
		var lastRequiredAnswersCount = $("select[id=itemForm:emiQuestionAnswerCombinations:" + highestItemId +":requiredOptionsCount]");
		lastRequiredAnswersCount.val("0");

		if (typeof FCKeditorAPI !== "undefined" && lastItemText !== null) {
		    lastFckEditor = FCKeditorAPI.GetInstance(lastItemText.attr("id"));
		    if (lastFckEditor) lastFckEditor.SetHTML("");
		}
		
		
		$("table[id=itemForm:emiQuestionAnswerCombinations:" + highestItemId + ":Row]").parent().parent().hide();
		return false;
    });
}
*/	
