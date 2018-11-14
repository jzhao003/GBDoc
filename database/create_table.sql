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

CREATE TABLE document ( -- 保存最终完成的文档信息(单个文件)
    id bigint NOT NULL AUTO_INCREMENT,
    created_on datetime,
    updated_on datetime,
    created_by bigint,
    updated_by bigint,
    title varchar(50), -- 文件名 和 doc_template.doc_title是同一个东西。
    folder_name varchar(50),
    PRIMARY KEY (id)
);

CREATE TABLE standard ( -- 标准手册
    id bigint NOT NULL AUTO_INCREMENT,
	created_on datetime,
    updated_on datetime,
    created_by bigint,
    updated_by bigint,
    title varchar(50), -- 标准手册名称，如：19489标准管理手册
    template_location varchar(256),
    PRIMARY KEY (id)
);

CREATE TABLE documents_set ( -- 符合某个标准的一套文档。最终用户使用本系统就是为了得到这个。
    id bigint NOT NULL AUTO_INCREMENT,
    created_on datetime,
    updated_on datetime,
    created_by bigint,
    updated_by bigint,
    title varchar(50), -- 文件名 和 doc_template.doc_title是同一个东西。
    standard_id bigint, -- FK=>standard.id
    PRIMARY KEY (id),
    FOREIGN KEY (standard_id) REFERENCES standard(id)
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
CREATE TABLE doc_filled_field ( -- 最终用户填好的文档要素。
	id bigint NOT NULL AUTO_INCREMENT,
    created_on datetime,
    updated_on datetime,
    created_by bigint,
    updated_by bigint,
	doc_template_id bigint,-- =FK=>doc_template.id
	name varchar(50),  -- 例如 国家级/省市级
	field_value text,
	PRIMARY KEY (id),
	FOREIGN KEY (doc_template_id) REFERENCES doc_template(id)
);


