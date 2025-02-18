package com.baiyi.opscloud.domain.param.kubernetes;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2023/10/18 16:37
 * @Version 1.0
 */
public class KubernetesParam {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class GetResource {

        @NotNull(message = "数据源实例ID不能为空")
        @Schema(description = "数据源实例ID")
        private Integer instanceId;

        @Schema(description = "命名空间")
        private String namespace;

        @Schema(description = "资源名称")
        private String name;

    }

}
