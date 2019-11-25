package cn.xrb.manager.service;

import cn.xrb.domain.Role;
import cn.xrb.util.Data;

import java.util.List;

/**
 * 
 * <p>Title: RoleService.java<／p>
 * <p>Description: 角色模块的业务接口<／p>
 * <p>Copyright: Copyright (c) 2017<／p>
 * <p>Company: Atguigu<／p>
 * @author xrb
 * @date 2019 10 12
 * @version 1.0
 */
public interface RoleService {
	List<Role> queryRole();

    List<Role> queryRoleByCondition(String name);

    int deleteById(Integer id);

    int batchDelete(Data data);

    Role queryById(Integer id);


    int updateRole(Role role);

    int saveRolePermissionRelationship(Integer roleid, Data data);

    int addRole(Role role);

//	public Page<Role> pageQuery(Map<String, Object> paramMap);
//
//	public int queryCount(Map<String, Object> paramMap);
//
//	public void saveRole(Role user);
//
//	public Role getRole(Integer id);
//
//	public int updateRole(Role user);
//
//	public int deleteRole(Integer uid);
//
//	public int batchDeleteRole(Integer[] uid);
//
//	public int batchDeleteRole(Data datas);
//
//	public List<Role> queryAllRole();
//
//	public List<Integer> queryRoleidByUserid(Integer id);
//
//	/*public void doAssignRoleByUserid(Integer userid, Integer[] ids);
//
//	public void doUnAssignRoleByUserid(Integer userid, Integer[] ids);*/
//
//
//	public void doAssignRoleByUserid(Integer userid, List<Integer> ids);
//
//	public void doUnAssignRoleByUserid(Integer userid, List<Integer> ids);
//
//	public int saveRolePermissionRelationship(Integer roleid, Data datas);
}
