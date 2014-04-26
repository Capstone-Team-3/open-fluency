<%@ page import="com.openfluency.Constants" %>
<div class="col-lg-3">
    <div class="panel panel-default">
        <div class="panel-body">
            <h3>${registrationInstance.course.title}</h3>

            <g:if test="${registrationInstance.status == Constants.APPROVED}">
                <div class="current-chapter">
                    <h4>Current chapter</h4>
                    <a href="#">Chapter 1: The Basics</a>
                </div>

                <div class="continue">
                    <g:link class="btn btn-success" action="show" controller="course" id="${registrationInstance.course.id}">Continue</g:link>
                </div>
            </g:if>
            <g:else>
            <div class="alert alert-warning">
            <p class="center">Pending approval</p>
            </div>
        </g:else>
        </div>
    </div>
</div>