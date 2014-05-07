<%@ page import="com.openfluency.course.Registration" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
</head>
<body>

    <div class="container course-search">
        <ul class="breadcrumb">
            <li>
                <a href="${createLink(uri:'/') }">Home</a>
            </li>
            <li>
                <g:link action="search" controller="course" >Courses</g:link>
            </li>
            <li>
                <g:link action="search" controller="course" >Search for Course</g:link>
            </li>
        </ul>
        <h1 id="main">Course Search</h1>

        <div class="row">

            <g:form action="search" controller="Course">
                <div class="col-lg-4">
                    <select id="filter-lang" class="form-control" name="filter-lang">
                        <g:each in="${languageInstanceList}">
                            <g:if test="${it.id == languageId}">
                                <option value="${it.id}" selected>${it.name}</option>
                            </g:if>
                            <g:else>
                                <option value="${it.id}">${it.name}</option>
                            </g:else>
                        </g:each>
                    </select>
                </div>
                <!-- end col-lg-4 -->

                <div class="col-lg-4">
                    <div class="input-group">
                        <g:textField class="form-control" name="search-text" placeholder="Type a keyword" id="search-text" value="${keyword}"/>
                        <span class="input-group-btn">
                            <button class="btn btn-default" type="submit" id="run-search">
                                <span class="glyphicon glyphicon-search"></span>
                            </button>
                        </span>
                    </div>
                </div>
            </g:form>
        </div>
        <!-- end row -->

        <table class="table courses-table">
            <thead>
                <tr>
                    <th>Course number</th>
                    <th>Title/instructor</th>
                    <th>Description</th>
                    <th>Number of chapters</th>
                    <th>Start date</th>
                    <th>End date</th>
                    <th>Enrolled</th>
                    <th></th>
                </tr>
            </thead>
            <g:each in="${courseInstanceList}">
                <tr class="course-result">
                    <td>${it.getCourseNumber()}</td>
                    <td>
                        <g:link action="show" controller="course" id="${it.id}">${it.title}</g:link>
                        /${it.owner.username}
                        <!-- should be the instructor name --> </td>
                    <td>${it.description}</td>
                    <td>${it.getChapters().size()}</td>
                    <td>${it.startDate}</td>
                    <td>${it.endDate}</td>
                    <td>${Registration.countByCourse(it)}</td>
                    <td>
                        <sec:ifAllGranted roles="ROLE_STUDENT">
                            <g:if test="${Registration.countByCourseAndUser(it, userInstance) == 0}">
                                <g:link action="enroll" controller="course" id="${it.id}" class="enroll pull-right btn btn-info">Enroll</g:link>
                            </g:if>
                            <g:else>
                                <g:link class="btn btn-danger pull-right" action="drop" controller="course" id="${it.id}">Drop Course</g:link>
                            </g:else>
                        </sec:ifAllGranted>
                    </td>
                </tr>
            </g:each>
        </table>

        <div class="pagination center-block text-center">
            <g:paginate controller="course" action="search"  total="${courseInstanceList.size() ?: 0}" />
        </div>
    </div>
    <!-- end container -->

</body>
</html>