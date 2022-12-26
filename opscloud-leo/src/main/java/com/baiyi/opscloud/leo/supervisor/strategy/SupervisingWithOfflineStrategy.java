package com.baiyi.opscloud.leo.supervisor.strategy;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesPodDriver;
import com.baiyi.opscloud.domain.constants.DeployTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.domain.vo.leo.LeoDeployingVO;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.leo.supervisor.strategy.base.SupervisingStrategy;
import io.fabric8.kubernetes.api.model.Pod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

import static com.baiyi.opscloud.domain.vo.leo.LeoDeployingVO.MAX_RESTART;

/**
 * @Author baiyi
 * @Date 2022/12/26 17:29
 * @Version 1.0
 */
@Slf4j
@Component
public class SupervisingWithOfflineStrategy extends SupervisingStrategy {

    @Override
    protected LeoDeployingVO.Deploying getDeploying(LeoDeploy leoDeploy,
                                                    LeoDeployModel.DeployConfig deployConfig,
                                                    KubernetesConfig.Kubernetes kubernetes,
                                                    LeoDeployModel.Deploy deploy,
                                                    LeoBaseModel.Deployment deployment) {
        List<Pod> pods = KubernetesPodDriver.listPod(kubernetes, deployment.getNamespace(), deployment.getName());
        if (CollectionUtils.isEmpty(pods)) {
            return LeoDeployingVO.Deploying.builder().build();
        }
        final String containerName = deployment.getContainer().getName();

        LeoDeployingVO.VerionDetails offlineVersion = LeoDeployingVO.VerionDetails.builder()
                .title("下线中")
                .versionName(deploy.getDeployVersion1().getVersionName())
                .versionDesc(deploy.getDeployVersion1().getVersionDesc())
                .image(deploy.getDeployVersion1().getImage())
                .build();

        for (Pod pod : pods) {
            LeoDeployingVO.PodDetails podDetails = podDetailsHelper.toPodDetails(pod, containerName);
            offlineVersion.putPod(podDetails);
        }

        return LeoDeployingVO.Deploying.builder()
                .deployType(deploy.getDeployType())
                .versionDetails1(offlineVersion)
                .replicas(deployment.getReplicas())
                .build()
                .init();
    }

    @Override
    protected boolean verifyFinish(LeoDeploy leoDeploy, LeoDeployingVO.Deploying deploying) {
        // 发布结束
        if (deploying.getIsFinish()) {
            LeoDeploy saveLeoDeploy = LeoDeploy.builder()
                    .id(leoDeploy.getId())
                    .endTime(new Date())
                    .deployResult("SUCCESS")
                    .deployStatus("执行部署任务阶段: 结束")
                    .isFinish(true)
                    .isActive(true)
                    .build();
            leoDeployService.updateByPrimaryKeySelective(saveLeoDeploy);
            return true;
        }
        return false;
    }

    @Override
    protected void verifyError(LeoDeploy leoDeploy, LeoDeployingVO.Deploying deploying) {
        if (deploying.isMaxRestartError()) {
            LeoDeploy saveLeoDeploy = LeoDeploy.builder()
                    .id(leoDeploy.getId())
                    .endTime(new Date())
                    .deployResult("ERROR")
                    .deployStatus(String.format("执行部署任务阶段: 容器重启次数超过最大值 maxRestart=%s", MAX_RESTART))
                    .isFinish(true)
                    .isActive(false)
                    .build();
            leoDeployService.updateByPrimaryKeySelective(saveLeoDeploy);
        }
    }

    @Override
    public String getDeployType() {
        return DeployTypeConstants.OFFLINE.name();
    }

}
