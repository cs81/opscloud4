package com.baiyi.opscloud.leo.handler.build.chain.pre;

import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.leo.handler.build.BaseBuildChainHandler;
import com.baiyi.opscloud.leo.handler.build.LeoPostBuildHandler;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import com.baiyi.opscloud.leo.holder.LeoHeartbeatHolder;
import com.baiyi.opscloud.leo.supervisor.BuildingSupervisor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2022/11/14 19:56
 * @Version 1.0
 */
@Slf4j
@Component
public class BuildingSupervisorChainHandler extends BaseBuildChainHandler {

    @Resource
    private LeoPostBuildHandler leoPostBuildHandler;

    @Resource
    private LeoHeartbeatHolder heartbeatHolder;

    @Autowired
    private ThreadPoolTaskExecutor coreExecutor;

    /**
     * 启动监视器
     *
     * @param leoBuild
     * @param buildConfig
     */
    @Override
    protected void handle(LeoBuild leoBuild, LeoBuildModel.BuildConfig buildConfig) {
        LeoBaseModel.DsInstance dsInstance = buildConfig.getBuild().getJenkins().getInstance();
        JenkinsConfig jenkinsConfig = getJenkinsConfigWithUuid(dsInstance.getUuid());
        BuildingSupervisor buildingSupervisor = new BuildingSupervisor(
                this.heartbeatHolder,
                this.leoBuildService,
                leoBuild,
                leoLog,
                jenkinsConfig.getJenkins(),
                buildConfig,
                leoPostBuildHandler
        );
        coreExecutor.execute(buildingSupervisor);
        LeoBuild saveLeoBuild = LeoBuild.builder()
                .id(leoBuild.getId())
                .buildStatus("启动构建Supervisor阶段: 成功")
                .build();
        save(saveLeoBuild, "启动构建Supervisor成功: jobName={}", leoBuild.getBuildJobName());
    }

}
