package cn.xrb.manager.service.impl;

import cn.xrb.domain.Role;
import cn.xrb.domain.RolePermission;
import cn.xrb.manager.dao.RoleMapper;
import cn.xrb.manager.service.RoleService;
import cn.xrb.util.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xieren8iao
 * @create 2019/10/12 - 18:08
 */
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;

    public List<Role> queryRole() {
        return roleMapper.selectAll();
    }

    public List<Role> queryRoleByCondition(String name) {
        return roleMapper.queryRoleByCondition(name);
    }

    public int deleteById(Integer id) {
        return roleMapper.deleteByPrimaryKey(id);
    }

    public int batchDelete(Data data) {
        int count=0;
        List<Integer> roleIds=data.getIds();
        for (Integer roleId : roleIds) {
            roleMapper.deleteByPrimaryKey(roleId);
            count++;
        }
        return count;
    }

    public Role queryById(Integer id) {
        return roleMapper.selectByPrimaryKey(id);
    }

    public int updateRole(Role role) {
        return roleMapper.updateByPrimaryKey(role);
    }

    public int saveRolePermissionRelationship(Integer roleid, Data data) {
        int totalCount=0;
        roleMapper.deleteRoleRolePermission(roleid);

        for (Integer permissionid :data.getIds() ) {
            RolePermission rp=new RolePermission();
            rp.setRoleid(roleid);
            rp.setPermissionid(permissionid);
            int count=roleMapper.savaRolePermission(rp);
            totalCount+=count;
        }
        return totalCount;
    }

    public int addRole(Role role) {
        return roleMapper.insert(role);
    }


//    public Page<Role> pageQuery(Map<String, Object> paramMap) {
//        return null;
//    }
//
//    public int queryCount(Map<String, Object> paramMap) {
//        return 0;
//    }
//
//    public void saveRole(Role user) {
//
//    }
//
//    public Role getRole(Integer id) {
//        return null;
//    }
//
//    public int updateRole(Role user) {
//        return 0;
//    }
//
//    public int deleteRole(Integer uid) {
//        return 0;
//    }
//
//    public int batchDeleteRole(Integer[] uid) {
//        return 0;
//    }
//
//    public int batchDeleteRole(Data datas) {
//        return 0;
//    }
//
//    public List<Role> queryAllRole() {
//        return null;
//    }
//
//    public List<Integer> queryRoleidByUserid(Integer id) {
//        return null;
//    }
//
//    public void doAssignRoleByUserid(Integer userid, List<Integer> ids) {
//
//    }
//
//    public void doUnAssignRoleByUserid(Integer userid, List<Integer> ids) {
//
//    }
//
//    public int saveRolePermissionRelationship(Integer roleid, Data datas) {
//        return 0;
//    }
}
