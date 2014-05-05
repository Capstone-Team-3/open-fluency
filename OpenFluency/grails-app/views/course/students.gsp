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

        <table class="table table-bordered courses-table">
            <thead>
                <tr class="active">
                    <th rowspan="2">Name</th>
                    <g:each in="${courseInstance.getChapters()}">
                        <th class="center" colspan="3">Practice ${it.title}</th>
                    </g:each>
                    <g:if test="${enrolledStudents[0].quizGrade}">
                        <th class="center" colspan="${enrolledStudents[0].quizGrade.keySet().size()}">Quiz Grades</th>
                    </g:if>
                    <th rowspan="2">Enrollment</th>
                </tr>
                <tr class="active">
                    <g:each in="${courseInstance.getChapters()}">
                        <th>Meanings (${courseInstance.chapters[0].deck.language} to ${courseInstance.chapters[0].deck.sourceLanguage})</th>
                        <th>Meanings (${courseInstance.chapters[0].deck.sourceLanguage} to ${courseInstance.chapters[0].deck.language})</th>
                        <th>Pronunciations in ${courseInstance.chapters[0].deck.language}</th>
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
                        <td>${registrationInstance.chapterProgress[chapterInstance][0]}%</td>
                        <td>${registrationInstance.chapterProgress[chapterInstance][2]}%</td>
                        <td>${registrationInstance.chapterProgress[chapterInstance][1]}%</td>
                    </g:each>
                    <g:each in="${registrationInstance.quizGrade.keySet()}" var="quizInstance">
                        <g:if test="${registrationInstance.quizGrade[quizInstance] != null}">
                            <td>${registrationInstance.quizGrade[quizInstance]}%</td>
                        </g:if>
                        <g:else>
                            <td>--</td>
                        </g:else>
                    </g:each>
                    <td>
                        ${Constants.REGISTRATION_STATUS[registrationInstance.status]}
                        <div class="pull-right">
                        <g:if test="${registrationInstance.status == Constants.PENDING_APPROVAL}">
                            <g:link controller="registration" action="approve" id="${registrationInstance.id}" class="btn btn-success btn-xs">Approve</g:link>
                            <g:link controller="registration" action="reject" id="${registrationInstance.id}" class="btn btn-danger btn-xs">Reject</g:link>
                        </g:if>
                    </div>
                    </td>
                </tr>
            </g:each>
        </table>

    </div>
    <!-- end container -->

</body>
</html>