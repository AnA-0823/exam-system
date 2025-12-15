create table exams
(
    id          bigint auto_increment comment '试卷ID'
        primary key,
    title       varchar(255)                        not null comment '试卷标题',
    total_score double                              not null comment '试卷总分',
    duration    int                                 not null comment '考试时长',
    start_time  timestamp                           not null comment '考试开始时间',
    end_time    timestamp                           not null comment '考试结束时间',
    created_at  timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_at  timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '试卷表';
create index idx_start_end_time
    on exams (start_time, end_time);


create table questions
(
    id             bigint auto_increment comment '题目ID'
        primary key,
    exam_id        bigint                              not null comment '试卷ID',
    content        text                                not null comment '题干内容',
    options        json                                null comment '选项内容（JSON格式，例如：[{"key":"A","value":"..."}, {...}]），仅适用于选择题和判断题',
    correct_answer varchar(100)                        not null comment '正确答案',
    score          double                              not null comment '题目分值',
    created_at     timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_at     timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '题目表';

