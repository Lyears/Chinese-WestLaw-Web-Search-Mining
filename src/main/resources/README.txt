There are totally 3 zips containing data describing legal cases.
The zips are described below.

data1.zip
	containing 1246572 json files
	each json file contains a dictionary where keys are:
		id	:	id of this piece of data
		iname :	被执行人姓名/名称
		caseCode	:	案号
		age	:	年龄
		sexy	:	性别
		cardNum	:	身份证号码/组织机构代码
		bussinessEntity (若被执行人为公司):	法人
		courtName	:	执行法院
		areaName	:	省份
		partyTypeName	:	Just ignore this key...
		gistId	:	执行依据文号
		regDate	:	立案时间
		gistUnit	:	做出执行依据单位
		duty	:	生效法律文书确定的义务
		performance	:	被执行人履行情况
		performedPart	:	履行部分
		unperformPart	:	未履行部分
		disruptTypeName	:	被执行人行为具体情形
		publishDate	:	发布时间
		qysler (若被执行人为公司)	:	[{'cardNum': 人员身份证号码, 'corporationtypename':人员与公司关系, 'iname': 人员姓名}]

data2.zip
	containing 390358 json files
	each json file contains a dictionary where keys are:
		案号；被执行人；被执行人地址；被执行标的金额（元）；申请执行人；承办法院、联系电话

instruments.zip
	containing 3090 json files
	each json file contains a dictionary where keys are:
		id；案号；标题；文书类别；案由；承办部门；级别；结案日期；content,
		where 'content' shows the complete legal instrument.
