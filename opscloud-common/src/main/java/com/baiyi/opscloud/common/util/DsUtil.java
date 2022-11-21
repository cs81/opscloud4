package com.baiyi.opscloud.common.util;

import com.baiyi.opscloud.common.exception.datasource.DatasourceRuntimeException;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

/**
 * @Author baiyi
 * @Date 2020/7/3 4:29 下午
 * @Version 1.0
 */
public class DsUtil {

    private DsUtil() {
    }

    public static <T> T toDsConfig(String propsYml, Class<T> targetClass) {
        if (StringUtils.isEmpty(propsYml))
            throw new DatasourceRuntimeException(ErrorEnum.DATASOURCE_PROPS_EMPTY);
        try {
            Yaml yaml = new Yaml(new SafeConstructor());
            return yaml.loadAs(propsYml, targetClass);
        } catch (JsonSyntaxException e) {
            throw new DatasourceRuntimeException(ErrorEnum.DATASOURCE_PROPS_CONVERT_ERROR);
        }
    }

}
