package com.baiyi.opscloud.service.leo.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.domain.generator.opscloud.LeoTemplate;
import com.baiyi.opscloud.domain.param.leo.LeoJobParam;
import com.baiyi.opscloud.mapper.opscloud.LeoJobMapper;
import com.baiyi.opscloud.service.leo.LeoJobService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/11/4 14:38
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class LeoJobServiceImpl implements LeoJobService {

    private final LeoJobMapper leoJobMapper;

    @Override
    public DataTable<LeoJob> queryJobPage(LeoJobParam.JobPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<LeoJob> data = leoJobMapper.queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public void add(LeoJob leoJob) {
        leoJobMapper.insert(leoJob);
    }

    @Override
    public void update(LeoJob leoJob) {
        leoJobMapper.updateByPrimaryKey(leoJob);
    }

    @Override
    public void updateByPrimaryKeySelective(LeoJob leoJob) {
        leoJobMapper.updateByPrimaryKeySelective(leoJob);
    }

}
