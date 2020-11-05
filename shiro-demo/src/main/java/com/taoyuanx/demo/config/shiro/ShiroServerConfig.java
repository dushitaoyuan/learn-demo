package com.taoyuanx.demo.config.shiro;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.taoyuanx.demo.exception.AuthException;
import com.taoyuanx.demo.exception.ServiceException;
import com.taoyuanx.demo.service.LoginService;
import com.taoyuanx.demo.utils.PasswordUtil;
import com.taoyuanx.demo.utils.PropertiesUtil;
import com.taoyuanx.demo.vo.LoginUserVo;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 */

@Configuration
public class ShiroServerConfig {
    @Autowired
    LoginService loginService;

    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //设置登陆url
        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setSuccessUrl("/index");
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        // 拦截器.
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        // 配置不会被拦截的链接 顺序判断
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/swagger-ui/**", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/**/*.css", "anon");
        filterChainDefinitionMap.put("/**/*.js", "anon");
        filterChainDefinitionMap.put("/**/*.html", "anon");
        filterChainDefinitionMap.put("/img/**", "anon");
        filterChainDefinitionMap.put("/fonts/**", "anon");
        filterChainDefinitionMap.put("/**/favicon.ico", "anon");
        filterChainDefinitionMap.put("/index", "user");
        //开放主注销接口
        filterChainDefinitionMap.put("/logout", "anon");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myShiroRealm());
        securityManager.setCacheManager(shiroCacheManager());
        securityManager.setSessionManager(sessionManager());
        securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }

    /**
     * @return
     */
    @Bean
    public DemoShiroRealm myShiroRealm() {
        DemoShiroRealm myShiroRealm = new DemoShiroRealm();
        myShiroRealm.setCredentialsMatcher(credentialsMatcher());
        return myShiroRealm;
    }

    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }

    /**
     * redis 缓存权限信息
     */
    @Bean
    public RedisCacheManager shiroCacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(shiroRedisManager());
        return redisCacheManager;
    }


    @Bean
    public RedisManager shiroRedisManager() {
        RedisManager redisManager = new RedisManager();
        String hostPort = PropertiesUtil.getSystemProperty("spring.redis.host") + ":" + PropertiesUtil.getSystemProperty("spring.redis.port");
        redisManager.setHost(hostPort);
        return redisManager;
    }

    /**
     * shiro bcrypt 比对
     */
    @Bean
    public CredentialsMatcher credentialsMatcher() {
        return new SimpleCredentialsMatcher() {
            @Override
            public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
                UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
                String hashPassword = new String(usernamePasswordToken.getPassword());
                String dbPassword = (String) info.getCredentials();
                return PasswordUtil.passwordEqual(dbPassword, hashPassword);
            }
        };
    }

    /**
     * remeberme
     */
    @Bean
    public CookieRememberMeManager rememberMeManager() {

        SimpleCookie rememberMeCookie = new SimpleCookie("rememberMe");
        rememberMeCookie.setHttpOnly(true);
        /**
         * 7天免密登录
         */
        int MAX_AGE_ONE_WEEK = 604800;
        rememberMeCookie.setMaxAge(MAX_AGE_ONE_WEEK);
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager() {
            /**
             * 重写序列化接口,防止将principals都序列化成RememberMe cookie
             */
            @Override
            protected byte[] serialize(PrincipalCollection principals) {
                SimplePrincipalCollection principal = (SimplePrincipalCollection) principals;
                Object primaryPrincipal = principal.getPrimaryPrincipal();
                LoginUserVo loginUserVo = (LoginUserVo) primaryPrincipal;
                return loginUserVo.getUsername().getBytes();
            }

            @Override
            protected PrincipalCollection deserialize(byte[] serializedIdentity) {
                String username = new String(serializedIdentity);
                LoginUserVo user = loginService.getByUsername(username);
                if (user == null) {
                    throw new AuthException("用户不存在,请重新登录");
                }
                return new SimplePrincipalCollection(user, String.valueOf(user.getId()));
            }
        };
        cookieRememberMeManager.setCookie(rememberMeCookie);
        /**
         * Remember  cookie aes 加密密码
         */
        cookieRememberMeManager.setCipherKey("1234567812345678".getBytes());
        return cookieRememberMeManager;
    }

    /***
     *  shiro-redis 集中会话管理
     */
    @Bean
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO());
        return sessionManager;
    }

    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(shiroRedisManager());
        return redisSessionDAO;
    }


    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

}
