package com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot;

import com.nio.bom.share.domain.model.AggrRoot;
import com.nio.bom.share.utils.GZIPUtils;
import com.nio.ngfs.plm.bom.configuration.domain.model.AbstractDo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
@Slf4j
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OxoVersionSnapshotAggr extends AbstractDo implements AggrRoot<Long> {

    /**
     * 车型
     */
    private String modelCode;

    /**
     * 版本号
     */
    private String version;

    /**
     * oxo快照信息
     */
    private String oxoSnapshot;

    /**
     * Formal/Informal
     */
    private String type;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 发版标题
     */
    private String title;

    /**
     * 发布内容
     */
    private String changeContent;

    /**
     * 邮件组
     */
    private String emailGroup;

    @Override
    public Long getUniqId() {
        return id;
    }




    public String findOxoSnapshot(){
        return GZIPUtils.uncompress(this.oxoSnapshot);
    }

}
