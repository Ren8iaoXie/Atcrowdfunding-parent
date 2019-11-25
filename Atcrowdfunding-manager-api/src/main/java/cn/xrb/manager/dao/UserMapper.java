package cn.xrb.manager.dao;

import cn.xrb.domain.Permission;
import cn.xrb.domain.Role;
import cn.xrb.domain.User;
import cn.xrb.util.Data;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    User selectByPrimaryKey(Integer id);

    List<User> selectAll();

    int updateByPrimaryKey(User record);

    /**
     * 用于登录
     * @param user
     * @return
     */
    User getUserByUAP(User user);


    List<User> getUserByPageByCondition(@Param("loginacct") String loginacct);

    List<User> queryList(@Param("startIndex") Integer startIndex, @Param("pagesize")Integer pagesize);

    Integer queryCount();

    List<Role> queryAllRole();

    List<Integer> queryRoleByUserId(Integer id);

    void saveUserRoleRelationship(@Param("userid") Integer userid, @Param("roleid") Integer roleid);

    void deleteUserRoleRelationship(@Param("userid")Integer userid, @Param("roleid")Integer roleid);

    int saveUserRoleRelationship2(@Param("userid") Integer userid, @Param("data")Data data);

    List<Permission> queryPermissionByUserId(Integer id);
}