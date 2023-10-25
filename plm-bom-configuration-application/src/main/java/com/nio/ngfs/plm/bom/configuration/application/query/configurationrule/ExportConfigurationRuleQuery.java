package com.nio.ngfs.plm.bom.configuration.application.query.configurationrule;

import com.nio.ngfs.plm.bom.configuration.application.query.AbstractExportQuery;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.ExportConfigurationRuleQry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

/**
 * @author bill.wang
 * @date 2023/10/25
 */
@Component
@RequiredArgsConstructor
public class ExportConfigurationRuleQuery extends AbstractExportQuery {

    public void execute(ExportConfigurationRuleQry qry, HttpServletResponse response) {

    }
}
