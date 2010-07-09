-- SAM-666
alter table SAM_ASSESSFEEDBACK_T add FEEDBACKCOMPONENTOPTION number default null;
update  SAM_ASSESSFEEDBACK_T set FEEDBACKCOMPONENTOPTION = 2;
alter table SAM_PUBLISHEDFEEDBACK_T add FEEDBACKCOMPONENTOPTION number default null;
update  SAM_PUBLISHEDFEEDBACK_T set FEEDBACKCOMPONENTOPTION = 2;

-- SAM-756 (SAK-16822):  oracle only
alter table SAM_ITEMTEXT_T add (TEMP_CLOB_TEXT clob);
update SAM_ITEMTEXT_T SET TEMP_CLOB_TEXT = TEXT;
alter table SAM_ITEMTEXT_T drop column TEXT;
alter table SAM_ITEMTEXT_T rename column TEMP_CLOB_TEXT to TEXT;

alter table SAM_PUBLISHEDITEMTEXT_T add (TEMP_CLOB_TEXT clob);
update SAM_PUBLISHEDITEMTEXT_T SET TEMP_CLOB_TEXT = TEXT;
alter table SAM_PUBLISHEDITEMTEXT_T drop column TEXT;
alter table SAM_PUBLISHEDITEMTEXT_T rename column TEMP_CLOB_TEXT to TEXT;

alter table SAM_ITEMGRADING_T add (TEMP_CLOB_TEXT clob);
update SAM_ITEMGRADING_T SET TEMP_CLOB_TEXT = ANSWERTEXT;
alter table SAM_ITEMGRADING_T drop column ANSWERTEXT;
alter table SAM_ITEMGRADING_T rename column TEMP_CLOB_TEXT to ANSWERTEXT;

