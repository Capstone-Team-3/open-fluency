<%@ page import="com.openfluency.course.Registration" %>
<%@ page import="com.openfluency.course.Quiz" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
</head>
<body>

    <div class="container course-enrolled">
		<ul class="breadcrumb">
            <li><a href="${createLink(uri:'/') }">Home</a></li>
            <li><g:link action="search" controller="course" >Courses</g:link></li>
            <li>
				<g:link action="show" controller="course" id="${courseInstance.id}">${courseInstance.getCourseNumber()}: ${courseInstance.title}</g:link>
			</li>
            <li><a href="#">Enrolled Students</a></li>
        </ul>
        <h1 id="main">Enrolled Students </h1>


        <table class="table courses-table">
            <thead>
                <tr>
                    <th>User Name</th>
                    <th>Native Language</th>
                    <th>Progress</th>
                    <th>Test Score</th>
                    <th></th>
                </tr>
            </thead>
            <g:each in="${enrolledStudents}">
                <tr class="course-result">
                    <td>${it.user.username}</td>
                    <td>${it.user.nativeLanguage}</td>
                    <td>
	                    <div class="progress">
                           <div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width:60%">
                               60%
                           </div><!-- end progress-bar -->
                       </div><!-- end progress -->
                    </td>
                    <td>
	                		<g:if test="${gradeInstance?.correctAnswers}">
	                    		${gradeInstance?.correctAnswers/answerInstanceList?.size()*100.0}%
	                    	</g:if>
                    	</td>
                </tr>
            </g:each>
        </table>
   
      
    </div>
    <!-- end container -->
   
</body>
</html>
