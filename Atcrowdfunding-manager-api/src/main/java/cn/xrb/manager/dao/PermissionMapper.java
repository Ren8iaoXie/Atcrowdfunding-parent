package cn.xrb.manager.dao;

import cn.xrb.domain.Permission;

import java.util.List;

public interface PermissionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Permission record);

    Permission selectByPrimaryKey(Integer id);

    List<Permission> selectAll();

    int updateByPrimaryKey(Permission record);

    Permission getRootPermission();

    List<Permission> getChildrenPermissionById(Integer id);

    List<Integer> queryPermissionIdByRoleId(Integer roleid);

}