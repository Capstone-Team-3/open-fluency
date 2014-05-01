<%@ page import="com.openfluency.course.Registration" %>
<%@ page import="com.openfluency.Constants" %>
<%@ page import="com.openfluency.course.Quiz" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
</head>
<body>

    <div class="container course-enrolled">
        <ul class="breadcrumb">
            <li>
                <a href="${createLink(uri:'/') }">Home</a>
            </li>
            <li>
                <g:link action="search" controller="course" >Courses</g:link>
            </li>
            <li>
                <g:link action="show" controller="course" id="${courseInstance.id}">${courseInstance.getCourseNumber()}: ${courseInstance.title}</g:link>
            </li>
            <li>
                <a href="#">Enrolled Students</a>
            </li>
        </ul>
        <h1 id="main">Enrolled Students</h1>
        <br/>
        <br/>
        <table class="table courses-table">
            <thead>
                <tr>
                    <th>User Name</th>
                    <th>Progress</th>
                    <th>Grades</th>
                    <th>Enrollment</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <g:each in="${enrolledStudents}" var="registrationInstance">
                <tr class="course-result">
                    <td>${registrationInstance.user.username}</td>
                    <td>
                        <g:each in="${registrationInstance.chapterProgress.keySet()}" var="chapterInstance">
                            <div class="col-lg-4">
                                <div class="panel panel-default">
                                    <div class="panel-heading">${chapterInstance.title}</div>
                                    <div class="panel-body">
                                        <small>Meaning Progress</small>
                                        <g:render template="/deck/progress" model="[progress: registrationInstance.chapterProgress[chapterInstance][0]]"/>
                                        <small>Pronunciation Progress</small>
                                        <g:render template="/deck/progress" model="[progress: registrationInstance.chapterProgress[chapterInstance][1]]"/>
                                    </div>
                                </div>
                            </div>
                        </g:each>
                    </td>
                    <td>
                        <g:each in="${registrationInstance.quizGrade.keySet()}" var="quizInstance">
                            <div class="col-lg-4">
                                <div class="panel panel-default">
                                    <div class="panel-heading">${quizInstance.title}</div>
                                    <div class="panel-body">
                                        ${registrationInstance.quizGrade[quizInstance]}
                                    </div>
                                </div>
                            </div>
                        </g:each>
                    </td>
                    <td>
                        ${Constants.REGISTRATION_STATUS[registrationInstance.status]}
                    </td>
                    <td>
                        <g:if test="${registrationInstance.status == Constants.PENDING_APPROVAL}">
                            <g:link controller="registration" action="approve" id="${registrationInstance.id}" class="btn btn-success">Approve</g:link>
                            <g:link controller="registration" action="reject" id="${registrationInstance.id}" class="btn btn-danger">Reject</g:link>
                        </g:if>
                    </td>
                </tr>
            </g:each>
        </table>

    </div>
    <!-- end container -->

</body>
</html>