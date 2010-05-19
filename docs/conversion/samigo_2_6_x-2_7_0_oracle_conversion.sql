ALTER TABLE SAM_PUBLISHEDASSESSMENT_T ADD LASTNEEDRESUBMITDATE timestamp NULL;

alter table SAM_ITEM_T add PARTIAL_CREDIT_FLAG number(1,0) NULL;
alter table SAM_PUBLISHEDITEM_T add PARTIAL_CREDIT_FLAG number(1,0) NULL;
alter table SAM_ANSWER_T add PARTIAL_CREDIT float NULL;
alter table SAM_PUBLISHEDANSWER_T add PARTIAL_CREDIT float NULL; 

create table SAM_GRADINGATTACHMENT_T (ATTACHMENTID number(19,0) not null, ATTACHMENTTYPE varchar2(255 char) not null, RESOURCEID varchar2(255 char), FILENAME varchar2(255 char), MIMETYPE varchar2(80 char), FILESIZE number(19,0), DESCRIPTION varchar2(4000 char), LOCATION varchar2(4000 char), ISLINK number(1,0), STATUS number(10,0) not null, CREATEDBY varchar2(255 char) not null, CREATEDDATE timestamp not null, LASTMODIFIEDBY varchar2(255 char) not null, LASTMODIFIEDDATE timestamp not null, ITEMGRADINGID number(19,0), primary key (ATTACHMENTID));
create index SAM_GA_ITEMGRADINGID_I on SAM_GRADINGATTACHMENT_T (ITEMGRADINGID);
alter table SAM_GRADINGATTACHMENT_T add constraint FK28156C6C4D7EA7B3 foreign key (ITEMGRADINGID) references SAM_ITEMGRADING_T;
create sequence SAM_GRADINGATTACHMENT_ID_S; 

-- SAM-834                                                                                                                          UPDATE SAM_ASSESSFEEDBACK_T
SET FEEDBACKDELIVERY = 3, SHOWSTUDENTRESPONSE = 0, SHOWCORRECTRESPONSE = 0, SHOWSTUDENTSCORE = 0, SHOWSTUDENTQUESTIONSCORE = 0, SHOWQUESTIONLEVELFEEDBACK = 0, SHOWSELECTIONLEVELFEEDBACK = 0, SHOWGRADERCOMMENTS = 0, SHOWSTATISTICS = 0
WHERE ASSESSMENTID in (SELECT ID FROM SAM_ASSESSMENTBASE_T WHERE TYPEID='142' AND ISTEMPLATE=1)
