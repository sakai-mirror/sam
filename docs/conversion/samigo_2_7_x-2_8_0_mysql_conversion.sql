-- SAM-666
alter table SAM_ASSESSFEEDBACK_T add FEEDBACKCOMPONENTOPTION int(11) default null;
update  SAM_ASSESSFEEDBACK_T set FEEDBACKCOMPONENTOPTION = 2;
alter table SAM_PUBLISHEDFEEDBACK_T add FEEDBACKCOMPONENTOPTION int(11) default null;
update  SAM_PUBLISHEDFEEDBACK_T set FEEDBACKCOMPONENTOPTION = 2;

alter table SAM_ASSESSMENTGRADING_T add LASTVISITEDPART integer default null;
alter table SAM_ASSESSMENTGRADING_T add LASTVISITEDQUESTION integer default null;

