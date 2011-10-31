/**
 * This does front-end validation for the emi options entered.
 * It checks that the use only fill in valid options.
 * The key entered must be a alphabet letter or 'good' whitespace.
 * Good Codes: 0
 * 
 * @param element The element where the option are entered.
 * @param validEMIOptions The valid options as a string.
 * @param event The key press event.
 * @returns {Boolean} true if it is valid, otherwise false.
 */
function checkEMIOptions(element, validEMIOptions, event) {
	var charCode = event.charCode;
	//A-Z
	if (charCode >= 65 && charCode <= 90){
		return isValidOption(element, validEMIOptions, charCode);
	}
	//a-z
	if (charCode >= 97 && charCode <= 122){
		return isValidOption(element, validEMIOptions, charCode);
	}
	//if the keycode is 0 it is invalid
	if (event.keyCode == 0){
		return false;
	}
	return true;
}
/**
 * Check if the key selected is valid.
 * @param element
 * @param validEMIOptions
 * @param charCode
 * @returns
 */
function isValidOption(element, validEMIOptions, charCode){
	// don't use if it is not in the options
	var keychar = String.fromCharCode(charCode).toUpperCase();
	if (validEMIOptions.indexOf(keychar) == -1) {
		return false;
	}
	// now check that it is not a duplicate
	var index = element.value.toUpperCase().indexOf(keychar);
	if (index == -1) {
		return true;
	} else {
		// check that the duplicate is not selected, then we can replace
		return (element.selectionStart <= index && element.selectionEnd > index);
	}
}