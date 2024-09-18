package org.wenchen.demo.service;

import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wenchen.demo.code.CheckboxConstants;
import org.wenchen.demo.domain.common.Checkbox;
import org.wenchen.demo.service.common.CheckboxManager;
import org.wenchen.demo.service.common.CheckboxService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: wen-chen
 * Date: 2024/9/18
 */
@Service
@RequiredArgsConstructor
public class CommonTestServer {
    private final CheckboxService checkboxService;

    public void saveCheckbox(List<Long> listIds, Long id) {
        List<String> roomIdList = listIds.stream().map(Object::toString).collect(Collectors.toList());
        checkboxService.addMultiFields(Checkbox.class, CheckboxConstants.OBJECT_NAME, CheckboxConstants.FIELD_NAME, id,
                CollUtil.isNotEmpty(roomIdList) ? roomIdList.toArray(new String[0]) : null);
    }

    public List<Long> queryCheckbox(Long id) {
        List<String> strings = checkboxService.queryMultiFields(Checkbox.class, CheckboxConstants.OBJECT_NAME, CheckboxConstants.FIELD_NAME, id);
        return strings.stream().map(Long::valueOf).collect(Collectors.toList());
    }
}
