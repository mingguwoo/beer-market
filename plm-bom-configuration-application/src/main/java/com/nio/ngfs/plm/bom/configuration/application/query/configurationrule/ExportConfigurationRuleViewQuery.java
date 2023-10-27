package com.nio.ngfs.plm.bom.configuration.application.query.configurationrule;

import com.nio.ngfs.plm.bom.configuration.application.query.AbstractExportQuery;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.QueryViewQry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wangchao.wang
 */

@Component
@RequiredArgsConstructor
public class ExportConfigurationRuleViewQuery extends AbstractExportQuery {


    public void execute(QueryViewQry qry, HttpServletResponse response, HttpServletRequest request) {



    }
}
