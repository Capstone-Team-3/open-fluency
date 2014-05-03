<%@ page import="com.openfluency.Constants" %>
<div class="col-lg-3">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h4>
                <g:link action="show" controller="course" id="${registrationInstance.course.id}">
                    ${registrationInstance.course.title}
                </g:link>
            </h4>
        </div>

        <g:if test="${registrationInstance.status == Constants.APPROVED}">
            <div class="panel-body">
                <div class="current-chapter">
                    <g:if test="${registrationInstance.course.getChapters().size() > 1}">
                        <h4>${registrationInstance.course.getChapters().size()} chapters</h4>
                    </g:if>
                    <g:else>
                        <h4>${registrationInstance.course.getChapters().size()} chapter</h4>
                    </g:else>
                </div>
            </div>
            <div class="panel-footer">
                <div class="continue">
                    <g:link class="btn btn-success" action="show" controller="course" id="${registrationInstance.course.id}">Continue</g:link>
                </div>
            </div>
        </g:if>

        <g:if test="${registrationInstance.status == Constants.PENDING_APPROVAL}">
            <div class="panel-body"></div>
            <div class="panel-footer">
                <div class="alert alert-warning">
                    <p class="center">Pending approval</p>
                </div>
            </div>
        </g:if>
    </div>
</div>