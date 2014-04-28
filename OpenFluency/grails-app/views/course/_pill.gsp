<%@ page import="com.openfluency.Constants" %>
<div class="col-lg-3">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h4>${registrationInstance.course.title}</h4>
        </div>

        <g:if test="${registrationInstance.status == Constants.APPROVED}">
            <div class="panel-body">
                <div class="current-chapter">
                    <h4>Current chapter</h4>
                    <a href="#">Chapter 1: The Basics</a>
                </div>
            </div>
            <div class="panel-footer">
                <div class="continue">
                    <g:link class="btn btn-success" action="show" controller="course" id="${registrationInstance.course.id}">Continue</g:link>
                </div>
            </g:if>

            <g:if test="${registrationInstance.status == Constants.PENDING_APPROVAL}">
                <div class="panel-body">
                </div>
                <div class="panel-footer">
                    <div class="alert alert-warning">
                        <p class="center">Pending approval</p>
                    </div>
                </div>
            </g:if>
        </div>
    </div>
</div>