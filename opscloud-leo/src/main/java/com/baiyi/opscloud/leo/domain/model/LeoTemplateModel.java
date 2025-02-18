package com.baiyi.opscloud.leo.domain.model;

import com.baiyi.opscloud.common.util.YamlUtil;
import com.baiyi.opscloud.domain.generator.opscloud.LeoTemplate;
import com.google.gson.JsonSyntaxException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/11/1 11:43
 * @Version 1.0
 */
@Slf4j
public class LeoTemplateModel {

    /**
     * 从配置加载
     *
     * @param config
     * @return
     */
    public static TemplateConfig load(String config) {
        if (StringUtils.isEmpty(config)) {
            return TemplateConfig.EMPTY_TEMPLATE;
        }
        try {
            return YamlUtil.loadAs(config, TemplateConfig.class);
        } catch (JsonSyntaxException e) {
            log.error(e.getMessage());
            return TemplateConfig.EMPTY_TEMPLATE;
        }
    }

    public static TemplateConfig load(LeoTemplate leoTemplate) {
        return load(leoTemplate.getTemplateConfig());
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TemplateConfig {
        private static final TemplateConfig EMPTY_TEMPLATE = TemplateConfig.builder().build();
        private Template template;
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Template {
        private Jenkins jenkins;
        private String name;
        @Schema(description = "任务目录")
        private String folder;
        @Schema(description = "模板任务完整URL")
        private String url;
        private String type;
        private String version;
        private String hash;
        private String comment;
        private List<String> tags;
        private List<LeoBaseModel.Parameter> parameters;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Jenkins {
        private LeoBaseModel.DsInstance instance;
    }

}
