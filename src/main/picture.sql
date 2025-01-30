ALTER TABLE picture
--添加新列
    ADD COLUMN reviewStatus INT DEFAULT 0 NOT NULL COMMENT'审核状态：0-待审核；1-通过；2-拒绝',
    ADD COLUMN reViewMessage VARCHAR(512) NULL COMMENT'审核信息',
    ADD COLUMN reviewerId BIGINT NULL COMMENT '审核人 ID',
    ADD COLUMN reViewTime DATETIME NULL COMMENT '审核时间';
-- 创建基于 reviewStatus 列的索引
CREATE INDEX idx_reviewStatus ON picture (reviewStatus);