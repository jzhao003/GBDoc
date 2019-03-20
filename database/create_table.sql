CREATE TABLE `user` (
    id bigint NOT NULL AUTO_INCREMENT,
    name varchar(255),
    role varchar(50), -- if admin or not
    created_on datetime,
    updated_on datetime,
    created_by bigint,
    updated_by bigint,
    PRIMARY KEY (id)
);

CREATE TABLE standard ( -- 标准手册
    id bigint NOT NULL AUTO_INCREMENT,
	created_on datetime not null default CURRENT_TIMESTAMP(),
    updated_on datetime not null default CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP(),
    created_by bigint,
    updated_by bigint,
    title varchar(50), -- 标准手册名称，如：19489标准管理手册
    template_location varchar(256),
    PRIMARY KEY (id)
);

CREATE TABLE standard_section ( -- 标准的目录项
    id bigint NOT NULL AUTO_INCREMENT,
    created_on datetime,
    updated_on datetime,
    created_by bigint,
    updated_by bigint,
    section_number varchar(50), -- 如 7.1.1
    section_content varchar(50),
    standard_id bigint, -- FK=>standard.id
    doc_title varchar(256),  -- doc_template展开后所应该在的文档的标题
    location int,            -- doc_template展开后所应该在的文档中的位置。
    PRIMARY KEY (id),
    FOREIGN KEY (standard_id) REFERENCES standard(id)
);

CREATE TABLE doc_template ( -- 模板， 对应 19489手册 ， 内容 列。 一个文档可能由多个doc_template 展开后组成。
    id bigint NOT NULL AUTO_INCREMENT,
	created_on datetime,
    updated_on datetime,
    created_by bigint,
    updated_by bigint,
    standard_section_id bigint not null comment '所属的目录项。',
    content text,  -- 其中会有 【国家级/省市级】 形式的要素
    PRIMARY KEY (id),
    FOREIGN KEY (standard_section_id) REFERENCES standard_section(id)
);

CREATE TABLE `standard_section_file` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT,
   `standard_section_id` bigint(20) NOT NULL,
   `filename` varchar(50) DEFAULT NULL COMMENT '文件名。',
   `updated_on` datetime DEFAULT NULL,
   `created_by` bigint(20) DEFAULT NULL,
   `updated_by` bigint(20) DEFAULT NULL,
   PRIMARY KEY (`id`),
   KEY `standard_section_id` (`standard_section_id`),
   CONSTRAINT `standard_section_file_ibfk_1` FOREIGN KEY (`standard_section_id`) REFERENCES `standard_section` (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='记录标准目录项对应的文件名，一个目录项有可能由多个文件里的段落构成。一个文件也可能包含多个段落构成不同的目录项。这个表的目的是快速找到给定的目录项所对应的文件。这个是模板的一部分。'

--不必建表表示模板中的单个文件了，用模板id和文件名来定位单个文件。



CREATE TABLE created_document (
    id bigint NOT NULL AUTO_INCREMENT,
    created_on datetime,
    updated_on datetime,
    created_by bigint,
    updated_by bigint,
    folder_name varchar(50) COMMENT '保存这套文档的目录。目录里面的是从上传的模板copy后 填上用户输入字段的文件。',
    standard_id bigint, -- FK=>standard.id
    PRIMARY KEY (id),
    FOREIGN KEY (standard_id) REFERENCES standard(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 
COMMENT='符合某个标准的一套文档。最终用户使用本系统就是为了得到这个。统一套模板理论上可以生成多个created_document，每个created_document可以填不同的要素。';


CREATE TABLE doc_field (
	id bigint NOT NULL AUTO_INCREMENT,
    created_on datetime,
    updated_on datetime,
    created_by bigint,
    updated_by bigint,
	created_document_id bigint,
	name varchar(50),  -- 例如 国家级/省市级
	field_value text default '',
	PRIMARY KEY (id),
	FOREIGN KEY (created_document_id) REFERENCES created_document(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 
COMMENT='最终需要用户填的文档要素。在创建新created_document时，系统要扫描模板中的所有文件，为扫描到的每个要素生成对应的记录。';


CREATE TABLE standard_section_field (
	id bigint NOT NULL AUTO_INCREMENT,
    created_document_id bigint(20) NOT NULL,
    `standard_section_id` bigint(20) NOT NULL,
    doc_field_id  bigint(20) NOT NULL,
    created_on datetime,
    updated_on datetime,
    created_by bigint,
    updated_by bigint,
	PRIMARY KEY (id),
	FOREIGN KEY (doc_field_id ) REFERENCES  doc_field(id),
    FOREIGN KEY (standard_section_id ) REFERENCES  standard_section(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 
COMMENT='用来追踪 标准目录项和文档要素的关系。一个文档要素可能被多个目录项使用，一个目录项一般会使用多个要素。在创建新created_document时，系统要生成doc_field记录和standard_section_field。';
