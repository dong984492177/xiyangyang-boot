package com.ywt.message.component.vendor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@Slf4j
public abstract class AbstractBaseMessageVendorExecutor implements BaseMessageVendorExecutor {

    protected String replaceTemplateContent(String template, Map<String, String> replacement) {
        if (replacement == null || replacement.isEmpty()) {
            return template;
        }

        List<String> searchStringList = new ArrayList<>();
        List<String> replacementList = new ArrayList<>();

        for (Map.Entry<String, String> entry : replacement.entrySet()) {
            searchStringList.add(String.format("{#%s#}", entry.getKey()));
            replacementList.add(entry.getValue());
        }

        return StringUtils.replaceEach(template, searchStringList.toArray(new String[searchStringList.size()]), replacementList.toArray(new String[replacementList.size()]));
    }

}
