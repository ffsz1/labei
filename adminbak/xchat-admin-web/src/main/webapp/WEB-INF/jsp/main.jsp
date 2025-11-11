<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page isELIgnored="false" %>
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-control" content="no-cache">
<meta http-equiv="Cache" content="no-cache">
<!DOCTYPE html>
<html>
    <head>
        <title>后台管理系统</title>
        <!-- css code -->
        <%@include file="common/style.html" %>
        <!-- script code -->
        <%@include file="common/script.html" %>
    </head>
    <body class="hold-transition skin-blue sidebar-mini">
        <div class="wrapper">
            <!-- header code -->
            <%@include file="common/header.jsp" %>
            <!-- Content Wrapper. Contains page content -->
            <div class="content-wrapper" id="content"></div>
            <!-- /.content-wrapper -->
            <div class="modal fade" id="tipModal" tabindex="-1" role="dialog" aria-labelledby="modalLabel">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h4 class="modal-title">提示信息</h4>
                        </div>
                        <div class="modal-body" id="tipMsg"></div>
                    </div>
                </div>
            </div>
            <!-- footer code -->
            <%@include file="common/footer.jsp" %>
        </div>
        <!-- ./wrapper -->
    </body>
</html>

