package com.project.gelingeducation.common.authentication;

import com.project.gelingeducation.domain.Permission;
import com.project.gelingeducation.domain.Role;
import com.project.gelingeducation.domain.User;
import com.project.gelingeducation.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * 自定义实现 ShiroRealm，包含认证和授权两大模块
 */
@Component
@Slf4j
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private IUserService userService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 进行权限校验的时候回调用
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String account = (String) principals.getPrimaryPrincipal();
        User user = userService.findUserByAccount(account);

        Set<String> stringRoleList = new HashSet<>();
        Set<String> stringPermissionList = new HashSet<>();

        Set<Role> roles = userService.findRoleByUserId(user.getId());
        for (Role role : roles) {
            stringRoleList.add(role.getName());
            Set<Permission> permissionList = userService.findPermissionByUserId(user.getId());
            for (Permission p : permissionList) {
                if (p != null) {
                    stringPermissionList.add(p.getPerms());
                }
            }
        }

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(stringRoleList);
        simpleAuthorizationInfo.setStringPermissions(stringPermissionList);

        return simpleAuthorizationInfo;
    }


    /**
     * 用户登录的时候会调用
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {

        String account = (String) token.getPrincipal();

        User user = userService.findUserByAccount(account);

        if (user == null)
            throw new AuthenticationException("账号未注册！");
//        if (!StringUtils.equals(password, user.getPassword()))
//            throw new IncorrectCredentialsException("用户名或密码错误！");

        return new SimpleAuthenticationInfo(account, account,
                this.getClass().getName());
    }

    /**
     * 清除当前用户权限缓存
     * 使用方法：在需要清除用户权限的地方注入 ShiroRealm,
     * 然后调用其 clearCache方法。
     */
    public void clearCache() {
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        super.clearCache(principals);
    }
}
