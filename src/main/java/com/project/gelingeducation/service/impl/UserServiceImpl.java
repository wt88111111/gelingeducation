package com.project.gelingeducation.service.impl;

import com.project.gelingeducation.common.dto.PageResult;
import com.project.gelingeducation.common.exception.AllException;
import com.project.gelingeducation.common.exception.StatusEnum;
import com.project.gelingeducation.common.utils.MD5Util;
import com.project.gelingeducation.dao.IUserDao;
import com.project.gelingeducation.entity.Permission;
import com.project.gelingeducation.entity.Role;
import com.project.gelingeducation.entity.User;
import com.project.gelingeducation.service.IRoleService;
import com.project.gelingeducation.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserDao userDao;

    @Autowired
    private IRoleService roleService;

    /**
     * 注册
     *
     * @param user
     * @return
     */
    @Override
    @Transactional
    public User register(User user) {
        if (userDao.findByPhone(user.getAccount()) == null) {
            user.setUserName("管理员");
            user.setStatus(1);
            user.setPassword(MD5Util.encrypt(user.getAccount().toLowerCase(), user.getPassword()));
            return userDao.insert(user);
        } else {
            throw new AllException(StatusEnum.ACCOUNT_ALREADY_EXISTS);
        }
    }

    /**
     * 添加用户
     *
     * @param user
     * @return
     */
    @Override
    @Transactional
    public User addUser(User user) {
        if (user.getRole() == null) {
            user.setRole(roleService.findDefault());
        }
        if (userDao.findByPhone(user.getAccount()) == null) {
            user.setUserName("用户名");
            user.setStatus(1);
            user.setPassword(MD5Util.encrypt(user.getAccount().toLowerCase(), user.getPassword()));
            user.setCoverImg("https://timgsa.baidu.com/timg?image&quality=80&size=b9999" +
                    "_10000&sec=1582740929074&di=88ebb0f61e464281d947673187acaa59&imgtype=0" +
                    "&src=http%3A%2F%2Fattach.bbs.miui.com%2Fforum%2Fthreadcover%2Fbb%2Fa1%2F1" +
                    "988382.jpg");
            return userDao.insert(user);
        } else {
            throw new AllException(StatusEnum.ACCOUNT_ALREADY_EXISTS);
        }
    }


    /**
     * 通过id查找用户
     *
     * @param id
     * @return
     */
    @Override
    public User findById(Long id) {
        User user = userDao.findById(id);
        return user;
    }

    /**
     * 页面格式的用户的列表
     *
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public Object queryAll(Integer currentPage, Integer pageSize) {
        if (currentPage != null && pageSize != null) {
            return userDao.queryAll(currentPage, pageSize);
        }else {
            return userDao.queryAll();
        }
    }

    /**
     * 更新用户头像
     *
     * @param id
     * @param coverImg
     */
    @Override
    @Transactional
    public void updateCoverImg(Long id, String coverImg) {
        userDao.updateCoverImg(id, coverImg);
    }

    /**
     * 更新用户
     *
     * @param user
     */
    @Override
    @Transactional
    public void update(User user) {
        userDao.update(user);
    }

    /**
     * @param id
     * @param oldPassword
     * @param newPassword
     */
    @Override
    @Transactional
    public void updatePassword(Long id, String oldPassword, String newPassword) {
        User user = userDao.findById(id);
        if (user.getPassword().equals(MD5Util.encrypt(user.getAccount().toLowerCase(),
                oldPassword))) {
            user.setPassword(MD5Util.encrypt(user.getAccount().toLowerCase(),
                    newPassword));
        } else {
            throw new AllException(StatusEnum.ACCOUNT_PASSWORD_ERROR);
        }
    }

    /**
     * 删除用户
     *
     * @param id
     */
    @Override
    @Transactional
    public void delUser(Long id) {
        userDao.delect(id);
    }

    /**
     * 批量删除用户
     *
     * @param ids
     */
    @Override
    @Transactional
    public void delSelUser(String ids) {
        userDao.delSel(ids);
    }

    /**
     * 通过名字用户格式的用户的list
     *
     * @param name
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public PageResult selbyname(String name, Integer currentPage, Integer pageSize) {
        return userDao.selbyname(name, currentPage, pageSize);
    }

    /**
     * 查找用户
     *
     * @param account
     * @return
     */
    @Override
    public User findUserByAccount(String account) {
        User user = userDao.findByPhone(account);
        return user;
    }

    /**
     * 查找身份
     *
     * @param id
     * @return
     */
    @Override
    public Role findRoleByUserId(Long id) {
        User user = userDao.findById(id);
        return user.getRole();
    }

    /**
     * 查找权限
     *
     * @param id
     * @return
     */
    @Override
    public Set<Permission> findPermissionByUserId(Long id) {
        User user = userDao.findById(id);
        Role role = user.getRole();
        Set<Permission> permissions = new HashSet<>();
        for (Permission permission : role.getPermissions()) {
            permissions.add(permission);
        }
        return permissions;
    }

    /**
     * 添加身份
     *
     * @param userId
     * @param roleId
     */
    @Override
    @Transactional
    public void addRole(Long userId, Long roleId) {
        User user = userDao.findById(userId);
        Role role = roleService.findByRole(roleId);
        user.setRole(role);
    }

}