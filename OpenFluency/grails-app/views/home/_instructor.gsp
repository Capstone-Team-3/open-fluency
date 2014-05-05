<%@ page import="com.openfluency.course.Registration" %>
<h2>Recently Updated Courses</h2>
<g:if test="${myCourses}">
    <div class="dashboard-container">
        <g:each in="${myCourses}">
            <div class="col-lg-3">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4>
                            <g:link action="show" controller="course" id="${it.id}" data-toggle="tooltip" data-placement="top" title="${it.title}">${it.title}</g:link>
                        </h4>
                    </div>
                    <div class="panel-body">
                        <div class="pull-right">
                            <g:if test="${it.visible}">
                                <span class="tooltiper glyphicon glyphicon-globe" data-toggle="tooltip"  data-placement="top" title="This course is visible to all users."></span>
                            </g:if>
                            <g:if test="${it.open}">
                                <span class="tooltiper glyphicon glyphicon-lock" data-toggle="tooltip"  data-placement="top" title="Approval is required to register for this course."></span>
                            </g:if>
                        </div>

                        <ul class="list-unstyled">
                            <li><strong>Last Updated:</strong> ${it.lastUpdated.format("MM/dd/yyyy")}</li>
                            <li>
                                <g:link class="view-more" action="students" controller="course" id="${it.id}">
                                    <span class="glyphicon glyphicon-user"></span>
                                    ${Registration.countByCourse(it)} Student Enrolled
                                </g:link>
                            </li>
                        </ul>
                    
                    </div>
                    <div class="panel-footer center">
                        <g:link class="btn btn-info" controller="course" action="show" id="${it.id}">View Course</g:link>
                    </div>
                </div>
            </div>
        </g:each>
    </div>
</g:if>
<g:else>
    <div class="dashboard-container">
        <p>You haven't created any courses yet! Get started:</p>
        <g:link class="btn btn-success" controller="course" action="create">Create a Course</g:link>
    </div>
</g:else>   
<g:link class="view-more" action="list" controller="course">
    View More Courses
    <span class="glyphicon glyphicon-arrow-right"></span>
</g:link>
