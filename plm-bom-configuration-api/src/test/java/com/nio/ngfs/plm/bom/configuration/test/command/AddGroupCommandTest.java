package com.nio.ngfs.plm.bom.configuration.test.command;

import com.nio.ngfs.plm.bom.configuration.application.command.feature.AddGroupCommand;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.AddGroupCmd;
import com.nio.ngfs.plm.bom.configuration.test.AbstractTest;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * @author xiaozhou.tu
 * @date 2023/7/11
 */
public class AddGroupCommandTest extends AbstractTest {

    @Resource
    private AddGroupCommand addGroupCommand;

    @Test
    public void execute() {
        AddGroupCmd addGroupCmd = new AddGroupCmd();
        addGroupCmd.setGroupCode("00 GENERAL");
        addGroupCmd.setDisplayName("");
        addGroupCmd.setChineseName("");
        addGroupCmd.setDescription("");
        addGroupCmd.setCreateUser("xiaozhou.tu");
        addGroupCommand.execute(addGroupCmd);
    }

}
