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

        <table class="table courses-table">
            <thead>
                <tr>
                    <th rowspan="2">Name</th>
                    <g:each in="${courseInstance.getChapters()}">
                        <th colspan="3">${it.title} Practice</th>
                    </g:each>
                    <th colspan="${enrolledStudents[0].quizGrade.keySet().size()}">Quiz Grades</th>
                    <th rowspan="2">Enrollment</th>
                    <th rowspan="2">Actions</th>
                </tr>
                <tr>
                    <g:each in="${courseInstance.getChapters()}">
                        <th>Meanings</th>
                        <th>Pronunciations</th>
                        <th>Symbols</th>
                    </g:each>
                    <g:each in="${enrolledStudents[0].quizGrade.keySet()}">
                        <th>${it.title}</th>
                    </g:each>
                </tr>
            </thead>

            <g:each in="${enrolledStudents}" var="registrationInstance">
                <tr class="course-result">
                    <td>${registrationInstance.user.username}</td>
                    <g:each in="${registrationInstance.chapterProgress.keySet()}" var="chapterInstance">
                        <td>${registrationInstance.chapterProgress[chapterInstance][0]}</td>
                        <td>${registrationInstance.chapterProgress[chapterInstance][1]}</td>
                        <td></td>
                    </g:each>
                    <g:each in="${registrationInstance.quizGrade.keySet()}" var="quizInstance">
                        <td>${registrationInstance.quizGrade[quizInstance]}</td>
                    </g:each>
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