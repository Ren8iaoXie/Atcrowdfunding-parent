package cn.xrb.manager.service.impl;

import cn.xrb.domain.Permission;
import cn.xrb.manager.dao.PermissionMapper;
import cn.xrb.manager.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xieren8iao
 * @create 2019/10/14 - 10:50
 */
@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;

    public Permission getRootPermission() {
        return permissionMapper.getRootPermission();
    }

    public List<Permission> getChildrenPermissionById(Integer id) {
        return permissionMapper.getChildrenPermissionById(id);
    }

    public List<Permission> queryAllPermiisions() {
        return permissionMapper.selectAll();
    }

    public int savePermission(Permission permission) {
        return permissionMapper.insert(permission);
    }

    public Permission getPermissionById(Integer id) {
        return permissionMapper.selectByPrimaryKey(id);
    }

    public int updatePermission(Permission permission) {
        return permissionMapper.updateByPrimaryKey(permission);
    }

    public int deletePermissionById(Integer id) {
        return permissionMapper.deleteByPrimaryKey(id);
    }

    public List<Integer> queryPermissionIdByRoleId(Integer roleid) {
        return permissionMapper.queryPermissionIdByRoleId(roleid);
    }


}
