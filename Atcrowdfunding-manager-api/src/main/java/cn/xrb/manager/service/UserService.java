package cn.xrb.manager.service;

import cn.xrb.domain.Permission;
import cn.xrb.domain.Role;
import cn.xrb.domain.User;
import cn.xrb.util.Data;
import cn.xrb.util.Page;

import java.util.List;

public interface UserService {

    /**
     * 获取所有用户信息
     * @return
     */
    List<User> selectAll();


    /**
     * 登录
     * @param user
     * @return
     */
    User getUserByUAP(User user);

    /**
     * 分页查询
     * @param
     * @param
     * @return
     */
    List<User> getUserByPage();

    /**
     * 分页查询+ 条件
     * @param loginacct
     * @return
     */
    List<User> getUserByPageByCondition(String loginacct);

    /**
     * 添加用户
     * @param user
     * @return
     */
    int addUser(User user);

    Page queryPage(Integer pageno, Integer pagesize);

    User getUserById(Integer id);

    int updateUser(User user);

    int deleteUser(Integer id);

    int deleteBatchUser(Integer[] id);

    List<Role> queryAllRole();

    List<Integer> queryRoleByUserId(Integer id);

    int saveUserRoleRelationship(Integer userid, List<Integer> ids);

    int deleteUserRoleRelationship(Integer userid, List<Integer> ids);

    int deleteUserRoleRelationship2(Integer userid, Data data);

    int saveUserRoleRelationship2(Integer userid, Data data);

    List<Permission> queryPermissionByUserId(Integer id);
}
