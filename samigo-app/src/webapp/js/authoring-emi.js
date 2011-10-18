//gopalrc - global vars
var highestOptionId = +25;
var highestItemId = +59;
var maxAvailableItems = +30;
var minOptions = +2;
var additionalOptionsGroupSize = +3;
var additionalItemsGroupSize = +3;
var showAtStart = +4;
var removeLabel = "X";
var maxErrorsDisplayed = +20;
var ANSWER_OPTION_LABELS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

//gopalrc - jQuery functions
if (typeof $ === 'undefined') {
	$ = jQuery;
}
$(document).ready(function(){
	
	//only applies to EMI authoring
	if (!emiAuthoring) return;
	
	//set the lead in default VULA-1283
	$('[identity="lead_in_statement"]').focus(function() {
		var leadin = $(this);
		var defaultval = $('#default_lead_in_statement_description').val().replace("<br/>", "\n");
		if (leadin.val() == defaultval) {
			leadin.val('');
			leadin.removeClass('placeholder');
		}
	}).blur(function() {
		var leadin = $(this);
		var defaultval = $('#default_lead_in_statement_description').val().replace("<br/>", "\n");
		if (leadin.val() == '' || leadin.val() == defaultval) {
			leadin.addClass('placeholder');
			leadin.val(defaultval);
		}
	}).blur();
	$('[identity="lead_in_statement"]').parents('form').submit(function() {
		$(this).find('[identity="lead_in_statement"]').each(function() {
			var leadin = $(this);
			var defaultval = $('#default_lead_in_statement_description').val().replace("<br/>", "\n");
			if (leadin.val() == defaultval) {
				leadin.val($('#default_lead_in_statement').val());
			}
		})
	});
	
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
	var at_least_two_options_required_error = $("input[id=at_least_two_options_required_error]").val();
	var at_least_two_pasted_options_required_error = $("input[id=at_least_two_pasted_options_required_error]").val();
	
	//create error message tags
	var $errorMessageTable = $('#emiErrorMessageTable');
	var $errorMessageRow = $('<tr></tr>');
	var $errorMessageColumn = $('<td></td>');
	
	//function to execute on save
	var buttonSave = $("input[value=Save]");
	buttonSave.bind('click', function(){
		$errorMessageTable.removeClass('messageSamigo');
		$('#emiErrorMessageTable > tr').remove();
		var errorMessages = new Array(maxErrorsDisplayed);
		var errorNumber=+0;

		//Validate Answer Point Value
		var answerPointValue = $("input[name=itemForm:answerptr]").val();
		if (answerPointValue.trim()=="" || /[^0-9.]/g.test(answerPointValue)) {
			errorMessages[errorNumber++] = answer_point_value_error;
			itemError = true;
		}
		
		//Validate Theme Text
		var themeText = $("input[name=itemForm:themetext]").val();
		if (themeText.trim()=="") {
			errorMessages[errorNumber++] = theme_text_error;
		}
		
		
		//Validate Options
		var simpleOrRichAnswerOptions = $("input[name=itemForm:emiAnswerOptionsSimpleOrRich]:checked").val();
		var optionLabels = "";
		var numOptions = +0;
		
		if (simpleOrRichAnswerOptions==0) { //simple
			var pastedOptions = $("textarea[id=itemForm:emiAnswerOptionsPaste]").val();
			if (pastedOptions == null || pastedOptions.trim()=="") {
				for (j=0; j<highestOptionId; j++) {
					var optionText = $("input[id=itemForm:emiAnswerOptions:" + j + ":Text]");
					if (optionText.is(':visible') && (optionText.val()==null || optionText.val().trim()=="") ) {
						//errorMessages[errorNumber++] = simple_text_options_blank_error;
						//break;
					}
					else if (optionText.is(':visible')) {
						var label = ANSWER_OPTION_LABELS.substring(j, j+1);
						optionLabels+=label;
						numOptions++;
					}
				}
				if (numOptions < minOptions) {
					errorMessages[errorNumber++] = at_least_two_options_required_error;
				}
			}
			else if (pastedOptions.split("\n").length < minOptions) {
				errorMessages[errorNumber++] = at_least_two_pasted_options_required_error;
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
		for (j=0; j<=highestItemId; j++) {
			var itemError = false;
			var labelInput = $("input[id=itemForm:emiQuestionAnswerCombinations:" + j + ":Label]");
			var itemText = $("textarea[id^=itemForm:emiQuestionAnswerCombinations:" + j +":]").val();
			var hasAttachment = $("input[id=itemForm:emiQuestionAnswerCombinations:" + j + ":hasAttachment]");
			var row = $("table[id=itemForm:emiQuestionAnswerCombinations:" + i + ":Row]");

			if (row && row.is(':visible') && labelInput.val() !== removeLabel 
					&& ((itemText && itemText.trim()!=="") || hasAttachment.val()==="true") )  {
				if (labelInput.val().trim()=="" || /[^0-9]/g.test(labelInput.val())) {
					errorMessages[errorNumber++] = blank_or_non_integer_item_sequence_error + labelInput.val();
					itemError = true;
				}
				
				var correctOptionLabels = $("input[id=itemForm:emiQuestionAnswerCombinations:" + j + ":correctOptionLabels]").val().toUpperCase();
				if (correctOptionLabels.trim()=="" || /[^A-Z,]/gi.test(correctOptionLabels)) {
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
			}
		}
		
		if (errorNumber > 0) {
			for (i=0; i<errorNumber; i++) {
				var col = $errorMessageColumn.clone().append(errorMessages[i]);
				var row = $errorMessageRow.clone().appendTo($errorMessageTable);
				col.appendTo(row);
			}
			$errorMessageTable.addClass('messageSamigo');
			top.window.scrollTo(0,0);
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
			$("a[id=itemForm:addEmiAnswerOptionsLink]").show();
			setContainerHeight();
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
					else {
						$("a[id=itemForm:addEmiAnswerOptionsLink]").hide();
						break;
					}
				}
				setContainerHeight();
				return false;
			}
		}
		$("a[id=itemForm:addEmiAnswerOptionsLink]").hide();
		setContainerHeight();
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
	var emiVisibleItems = $("input[id=itemForm:emiVisibleItems]");

	
	//Remove Items
	for (i=0; i<=highestItemId; i++) {
		var emiItemRemoveLink = $("a[id=itemForm:emiQuestionAnswerCombinations:" + i + ":RemoveLink]");
		emiItemRemoveLink.bind('click', function() {
			var itemId = +($(this).attr("id").split(":")[2]);
			var row = $("table[id=itemForm:emiQuestionAnswerCombinations:" + itemId + ":Row]").parent().parent();
			var labelRemove = $("input[id=itemForm:emiQuestionAnswerCombinations:" + itemId + ":Label]");
			labelRemove.val(removeLabel);
			row.hide();
			var seq=+0;
			//Resequence items
			for (j=0; j<=highestItemId; j++) {
				row = $("table[id=itemForm:emiQuestionAnswerCombinations:" + j + ":Row]");
				var labelInput = $("input[id=itemForm:emiQuestionAnswerCombinations:" + j + ":Label]");
				if (row && row.is(':visible') && labelInput.val() !== removeLabel)  {
					seq++;
					labelInput.val(seq);
					labelInput.trigger('change');
				}
			}
			emiVisibleItems.val(seq);
			$("a[id=itemForm:addEmiQuestionAnswerCombinationsLink]").show();
			setContainerHeight();
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
				availableItems++;
				labelInput.val(availableItems);
				labelInput.trigger('change');
				emiVisibleItems.val(availableItems);
				row.parent().parent().show();
				if (availableItems>=maxAvailableItems) {
					//alert("Maximum Number of Items Added");
					//messageDialog("Maximum Number of Items Added");
					$("a[id=itemForm:addEmiQuestionAnswerCombinationsLink]").hide();
					setContainerHeight();
					return false;
				}
				if (++j==additionalItemsGroupSize) {
					setContainerHeight();
					return false;
				}
			}
			else if (row && row.is(':visible')) {
				availableItems++;
				labelInput.val(availableItems);
				labelInput.trigger('change');
				emiVisibleItems.val(availableItems);
			}
			
			if (availableItems>=maxAvailableItems) {
				//alert("Maximum Number of Items Added");
				//messageDialog("Maximum Number of Items Added");
				$("a[id=itemForm:addEmiQuestionAnswerCombinationsLink]").hide();
				setContainerHeight();
				return false;
			}
		}
		messageDialog("Please save accumulated changes before adding additional items.");
		setContainerHeight();
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
			
			var correctOpts = $(this).val();
			var maxOptions = +0;
			for (var iMax=0; iMax<correctOpts.length; iMax++) {
				var currCorrectLabel = correctOpts.substring(iMax, iMax+1);
				if (ANSWER_OPTION_LABELS.indexOf(currCorrectLabel) != -1) {
					maxOptions += 1;
				}
			}
			
			requiredOptionsCountSelect.empty();
			requiredOptionsCountSelect.append('<option value="0">' + all_option + '</option>');
			for (j=1; j<=maxOptions-1; j++) {
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

	
	//Validate Items
	for (j=0; j<=highestItemId; j++) {
		var labelInput = $("input[id=itemForm:emiQuestionAnswerCombinations:" + j + ":Label]");
		labelInput.bind('change', function() {
			$(this).parent().children("span[id=showItemLabel]").html($(this).val());
		});
		labelInput.trigger('change');
	}
	
	
	
	//trigger startup events
	var radioSimpleOrRichChecked = $("input[name=itemForm:emiAnswerOptionsSimpleOrRich]:checked");
	radioSimpleOrRichChecked.trigger('click');
	
	
	//hide excess Options at start
	var firstOptionText = $("input[id=itemForm:emiAnswerOptions:" + 0 + ":Text]");
	var isAllNull = true;
	for (i=highestOptionId; i>=0; i--) {
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
		for (i=0; i<showAtStart; i++) {
			$("table[id=itemForm:emiAnswerOptions:" + i + ":Row]").parent().parent().show();
		}
	}
	

	//hide excess Items at start
	//var firstItemText = $("textarea[id^=itemForm:emiQuestionAnswerCombinations:0]");
	isAllNull = true;
	for (i=highestItemId; i>=0; i--) {
		var itemText = $("textarea[id^=itemForm:emiQuestionAnswerCombinations:" + i +"]");
		var hasAttachment = $("input[id=itemForm:emiQuestionAnswerCombinations:" + i + ":hasAttachment]");
		var labelInput = $("input[id=itemForm:emiQuestionAnswerCombinations:" + i + ":Label]");
		if ((itemText.val() === "" && hasAttachment.val()==="false") || labelInput.val()==removeLabel) {
			$("table[id=itemForm:emiQuestionAnswerCombinations:" + i + ":Row]").parent().parent().hide();
		}
		else {
			isAllNull = false;
			//break;
		}
	}
	var itemsToShow=+0;
	if (isAllNull) {
		itemsToShow = +showAtStart;
		emiVisibleItems.val(showAtStart);
	}
	else {
		itemsToShow = +emiVisibleItems.val();
	}
	for (i=0; i<=highestItemId; i++) {
		if (+itemsToShow == +0) break;
		var labelInput = $("input[id=itemForm:emiQuestionAnswerCombinations:" + i + ":Label]");
		if (labelInput.val() != removeLabel) {
			$("table[id=itemForm:emiQuestionAnswerCombinations:" + i + ":Row]").parent().parent().show();
			if (labelInput.val()==itemsToShow) {
				break;
			}
		}
	}

	
	//Vertically Align the Pasted Options Table Container
	$("textarea[id=itemForm:emiAnswerOptionsPaste]").parent().parent().parent().parent().parent().css('vertical-align','top');
	$("table[id=itemForm:emiAnswerOptions]").parent().css('vertical-align','top');

	//Make the container visible after all processing
	$("div[id=portletContent]").css('display','block');
	

	
	function messageDialog(message) {
		jAlert(message, 'Warning', null, 'confirm');
	}
	
	function setContainerHeight() {
		var containerFrame = $("iframe[class=portletMainIframe]", parent.document.body);
		containerFrame.height($(document.body).height() + 30);
	}
});

//*************** JQuery Functions Above ******************