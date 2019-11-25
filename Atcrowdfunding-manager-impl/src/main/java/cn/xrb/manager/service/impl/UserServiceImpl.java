package cn.xrb.manager.service.impl;

import cn.xrb.domain.Permission;
import cn.xrb.domain.Role;
import cn.xrb.domain.User;
import cn.xrb.manager.dao.UserMapper;
import cn.xrb.manager.service.UserService;
import cn.xrb.util.Data;
import cn.xrb.util.MD5Util;
import cn.xrb.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    public List<User> selectAll() {
        return userMapper.selectAll();
    }

    public User getUserByUAP(User user) {
        User loginUser = userMapper.getUserByUAP(user);
        /**
         * 如果查询不到用户，就会报错，而不是返回null。
         * 我的写法是只要发现异常，直接返回null
         */
        try {
            if (loginUser!=null) {
                return loginUser;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public List<User> getUserByPage() {
        return userMapper.selectAll();
    }

    /**
     * 分页加条件
     * @param loginacct
     * @return
     */
    public List<User> getUserByPageByCondition(String loginacct) {
        return userMapper.getUserByPageByCondition(loginacct);
    }

    public int addUser(User user) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String stingDate = dateFormat.format(date);
        user.setUserpswd(MD5Util.digest("123"));
        user.setCreatetime(stingDate);
        int i = userMapper.insert(user);
        return i;
    }

    public Page queryPage(Integer pageno, Integer pagesize) {
        Page page=new Page(pageno, pagesize);
        List<User> list=userMapper.queryList(page.getStartIndex(),pagesize);
        page.setData(list);

        Integer totalSize=userMapper.queryCount();
        page.setTotalsize(totalSize);
        return page;
    }

    public User getUserById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    public int updateUser(User user) {
        return userMapper.updateByPrimaryKey(user);
    }

    public int deleteUser(Integer id) {
        return userMapper.deleteByPrimaryKey(id);
    }

    public int deleteBatchUser(Integer[] ids) {
        int totalCount=0;
        for (Integer id : ids) {
            int count=userMapper.deleteByPrimaryKey(id);
            totalCount+=count;
        }
        return totalCount;
    }

    public List<Role> queryAllRole() {
        return userMapper.queryAllRole();
    }

    public List<Integer> queryRoleByUserId(Integer id) {
        return userMapper.queryRoleByUserId(id);
    }

    public int saveUserRoleRelationship(Integer userid, List<Integer> ids) {
        int count=0;
        for (Integer roleid : ids) {
         userMapper.saveUserRoleRelationship(userid,roleid);
         count++;
        }
        return count;
    }

    public int deleteUserRoleRelationship(Integer userid, List<Integer> ids) {
        int count=0;
        for (Integer roleid : ids) {
            userMapper.deleteUserRoleRelationship(userid,roleid);
            count++;
        }
        return count;
    }

    public int deleteUserRoleRelationship2(Integer userid, Data data) {
        return 0;
    }

    public int saveUserRoleRelationship2(Integer userid, Data data) {
        return userMapper.saveUserRoleRelationship2(userid,data);
    }

    public List<Permission> queryPermissionByUserId(Integer id) {
        return userMapper.queryPermissionByUserId(id);
    }


}
