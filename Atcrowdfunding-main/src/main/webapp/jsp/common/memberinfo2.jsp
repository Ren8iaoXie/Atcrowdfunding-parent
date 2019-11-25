<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
    <div class="col-sm-3 col-md-3 column">
        <div class="row">
            <div class="col-md-12">
                <div class="thumbnail" style="    border-radius: 0px;">
                    <img src="${APP_PATH}/img/services-box1.jpg" class="img-thumbnail" alt="">
                    <div class="caption" style="text-align:center;">
                        <h3>
                            ${sessionScope.member.loginacct }
                        </h3>
                        <c:choose>
                            <c:when test="${member.authstatus eq '1'}">
                                <span class="label label-warning" style="cursor:pointer;">实名认证申请中</span>
                            </c:when>
                            <c:when test="${member.authstatus eq '2'}">
                                <span class="label label-success" style="cursor:pointer;">已实名认证</span>
                            </c:when>
                            <c:otherwise>
                                <span class="label label-danger" style="cursor:pointer;" onclick="window.location.href='${APP_PATH}/member/apply.htm'">未实名认证</span>
                            </c:otherwise>
                        </c:choose>

                    </div>
                </div>
            </div>
        </div>
        <div class="list-group">
            <div class="list-group-item active">
                资产总览<span class="badge"><i class="glyphicon glyphicon-chevron-right"></i></span>
            </div>
            <div class="list-group-item " style="cursor:pointer;" onclick="window.location.href='${APP_PATH}/member/minecrowdfunding.htm'">
                我的众筹<span class="badge"><i class="glyphicon glyphicon-chevron-right"></i></span>
            </div>
        </div>
    </div>
