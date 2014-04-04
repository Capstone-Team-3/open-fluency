<%@ page import="com.openfluency.course.Registration" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
</head>
<body>
    <div class="container course-search">

        <h1>Course Search</h1>

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
                            <button class="btn btn-default" type="submit">
                                <span class="glyphicon glyphicon-search"></span>
                            </button>
                        </span>
                    </div>
                </div>
            </g:form>
        </div>
        <!-- end row -->

        <table class="table">
            <thead>
                <tr>
                    <th>Course number</th>
                    <th>Title/instructor</th>
                    <th>Description</th>
                    <th>Number of chapters</th>
                    <th>Start date</th>
                    <th>End date</th>
                    <th>Enrolled</th>
                </tr>
            </thead>
            <g:each in="${courseInstanceList}">
                <tr>
                    <td>
                        ${it.id}
                    </td>
                    <td>
                        <g:link action="show" controller="course" id="${it.id}">${it.title}</g:link>
                        /${it.owner.username}<!-- should be the instructor name -->
                    </td>
                    <td>
                        ${it.description}
                    </td>
                    <td>5</td><!-- should be something like :it.chapterInstance.size() -->
                    <td>01/02/2014</td><!-- should be date property -->
                    <td>15/05/2014</td><!-- should be date property -->
                    <td>${Registration.countByCourse(it)}</td>
                    <td>
                        <g:link action="enroll" controller="course" id="${it.id}" class="btn btn-info">Enroll</g:link>
                    </td>
                </tr>
            </g:each>
        </table>
    </div>
    <!-- end container -->
</body>
</html>