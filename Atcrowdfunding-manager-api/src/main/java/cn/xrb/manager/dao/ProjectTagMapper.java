package cn.xrb.manager.dao;

import cn.xrb.domain.ProjectTag;

import java.util.List;

public interface ProjectTagMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProjectTag record);

    ProjectTag selectByPrimaryKey(Integer id);

    List<ProjectTag> selectAll();

    int updateByPrimaryKey(ProjectTag record);
}