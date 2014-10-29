// SAM-1817: This was originally in RichTextEditor.java
function show_hide_editor(client_id, frame_id) {
	var status =  document.getElementById(client_id + '_textinput_current_status');
	if (status.value == "firsttime") {
		status.value = "expanded";
		chef_setupformattedtextarea(client_id, true, frame_id);
		if (typeof setBlockDivs == "function" && typeof retainHideUnhideStatus == "function") {
			setBlockDivs();
			retainHideUnhideStatus('none');
		}
	}
	else {
		if (status.value == "collapsed") {
			status.value = "expanded";
			chef_setupformattedtextarea(client_id, true, frame_id);
		}
		else if (status.value == "expanded") {
			status.value = "collapsed";
			var editor = CKEDITOR.instances[client_id + '_textinput'];
			editor.destroy();
		}
	}
}

function encodeHTML(text) {
	text = text.replace(
		/&/g, '&amp;').replace(
		/"/g, '&quot;').replace(
		/</g, '&lt;').replace(
		/>/g, '&gt;');
	return text;
}

function chef_setupformattedtextarea(client_id, shouldToggle, frame_id) {
	$("body").height($("body").outerHeight() + 600);

	var textarea_id = client_id + "_textinput";

	if (shouldToggle == true) {
		var input_text = document.getElementById(textarea_id);
		var input_text_value = input_text.value;
		var input_text_encoded = encodeHTML(input_text_value);
		input_text.value = input_text_encoded;
	}

	sakai.editor.launch(textarea_id,'','450','240');
	setMainFrameHeight(frame_id);
}