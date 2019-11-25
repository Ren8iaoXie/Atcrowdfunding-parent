package cn.xrb.manager.service;

import cn.xrb.domain.Permission;

import java.util.List;

/**
 * @author xieren8iao
 * @create 2019/10/14 - 10:44
 */
public interface PermissionService {
    Permission getRootPermission();

    List<Permission> getChildrenPermissionById(Integer id);

    List<Permission> queryAllPermiisions();

    int savePermission(Permission permission);

    Permission getPermissionById(Integer id);

    int updatePermission(Permission permission);

    int deletePermissionById(Integer id);

    List<Integer> queryPermissionIdByRoleId(Integer roleid);


}
