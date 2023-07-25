package com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
@Data
public class QueryFeatureLibraryDto implements Dto {

    private Long id;

    private String featureCode;

    private String type;

    private String displayName;

    private String chineseName;

    private String description;

    private String group;

    private String catalog;

    private String requestor;

    private String status;

    private String createUser;

    private String updateUser;

    private String createTime;

    private String updateTime;

    private List<QueryFeatureLibraryDto> children = Collections.emptyList();

    /**
     * 子节点列表是否为空
     */
    private transient boolean childrenEmpty = false;

    /**
     * 是否搜索匹配
     */
    private transient boolean searchMatch = true;

    public boolean isMatchResult() {
        return !childrenEmpty && searchMatch;
    }

    public void checkChildrenEmpty() {
        if (CollectionUtils.isEmpty(children)) {
            this.childrenEmpty = true;
        } else {
            this.childrenEmpty = children.stream().allMatch(QueryFeatureLibraryDto::isChildrenEmpty);
        }
    }

    /**
     * 匹配搜索词
     *
     * @param superMatch 上级是否匹配
     * @return 下级是否匹配
     */
    public boolean selectSearchMatch(String search, boolean superMatch) {
        // 当前节点是否匹配
        boolean currentMatch = superMatch || isMatchSearch(search);
        if (CollectionUtils.isNotEmpty(children)) {
            boolean currentMatchBefore = currentMatch;
            for (QueryFeatureLibraryDto child : children) {
                boolean childMatch = child.selectSearchMatch(search, currentMatchBefore);
                if (!currentMatch && childMatch) {
                    currentMatch = true;
                }
            }
        }
        setSearchMatch(currentMatch);
        return currentMatch;
    }

    private boolean isMatchSearch(String search) {
        return matchSearch(this.featureCode, search) ||
                matchSearch(this.displayName, search) ||
                matchSearch(this.chineseName, search);
    }

    private boolean matchSearch(String content, String search) {
        return content != null && content.contains(search);
    }

}
