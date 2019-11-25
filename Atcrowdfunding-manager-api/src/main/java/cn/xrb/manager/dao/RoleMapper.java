package cn.xrb.manager.dao;

import cn.xrb.domain.Role;
import cn.xrb.domain.RolePermission;
import cn.xrb.util.Data;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Role record);

    Role selectByPrimaryKey(Integer id);

    List<Role> selectAll();

    int updateByPrimaryKey(Role record);

    int batchDelete(@Param("ids") Integer[] uid);

    int batchDeleteObj(Data datas);

    List<Role> queryAllRole();

    List<Integer> queryRoleidByUserid(Integer id);

    /*void saveUserRole(@Param("userid") Integer userid, @Param("roleids") Integer[] ids);

    void deleteUserRole(@Param("userid") Integer userid,@Param("roleids")  Integer[] ids);*/
    void saveUserRole(@Param("userid") Integer userid, @Param("roleids") List<Integer> ids);

    void deleteUserRole(@Param("userid") Integer userid,@Param("roleids")  List<Integer> ids);

    int insertRolePermission(RolePermission rp);


    List<Role> queryRoleByCondition(@Param("name") String name);

    int savaRolePermission(RolePermission rp);

    int deleteRoleRolePermission(Integer roleid);
}