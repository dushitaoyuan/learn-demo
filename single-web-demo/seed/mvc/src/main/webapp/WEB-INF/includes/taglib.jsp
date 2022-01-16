<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fns" uri="/WEB-INF/tlds/fns.tld" %>
<%@ taglib prefix="fnc" uri="/WEB-INF/tlds/fnc.tld" %>
<%--自定义标签--%>
<%@ taglib prefix="sys" tagdir="/WEB-INF/tags" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ctxStatic" value="${pageContext.request.contextPath}/static"/>

<script>
    /**
     *
     *  初始化js常量
     */
    window.ctx = "${ctx}";
    window.ctxStatic = "${ctxStatic}";
    window.loginUrl = ctx + "login.jsp";
    window.logOutUrl = ctx + "logout";
</script>



