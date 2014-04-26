<%@ page import="com.openfluency.course.Registration" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>OpenFluency</title>
</head>
<body>
    <div class="container">
        <div class="row">
            <div class="col-lg-12">

                <sec:ifNotLoggedIn>
                    <div class="jumbotron">
                        <h1>Welcome to OpenFluency</h1>
                        <p>A flash card application for foreign language study.</p>
                        <g:link class="btn btn-primary" action="create" controller="user">Create your account</g:link>
                    </div>

                    <h2>Learn languages</h2>
                    <ul>
                        <li>Create decks of flash cards for any foreign language.</li>
                        <li>
                            Customize flash cards with audio and images to help you learn.
                        </li>
                        <li>
                            Share your decks with the world or a small group of classmates or friends.
                        </li>
                        <li>Join language courses.</li>
                    </ul>

                    <h2>Build classes</h2>
                    <ul>
                        <li>
                            Create a private or public course including all the flash card decks needed for your class.
                        </li>
                        <li>
                            Create timed or untimed tests and exams, which are automatically graded on completion.
                        </li>
                        <li>Track student grades and progress through your decks.</li>
                        <li>
                            Identify the characters, words, and phrases your students find most difficult.
                        </li>
                    </ul>

                    <h2>Research foreign language acquisition</h2>
                    <ul>
                        <li>Create courses for research.</li>
                        <li>
                            Access anonymous flash card deck usage statistics and student performance metrics for your courses.
                        </li>
                        <li>View anonymous biographical user data.</li>
                    </ul>

                    <h3>
                        Ready to get started?
                        <g:link class="btn btn-primary" action="create" controller="user">Sign Up</g:link>
                    </h3>
                </sec:ifNotLoggedIn>

                <sec:ifLoggedIn>
                    <div class="dashboard">

                        <h1>
                            <sec:username />
                            's Dashboard
                        </h1>

                        <sec:ifAllGranted roles="ROLE_STUDENT">

                            <h2>In-Progress Courses</h2>

                            <g:if test="${registrations}">
                                <div class="container">
                                    <g:render template="/course/pill" collection="${registrations}" var="registrationInstance"/> 
                                </div>
                                <g:link class="view-more" action="list" controller="course">
                                    View More Courses
                                    <span class="glyphicon glyphicon-arrow-right"></span>
                                </g:link>
                            </g:if>

                            <g:else>
                                <div class="container">
                                    <p>You haven't enrolled in any courses yet! Get started:</p>
                                    <g:link class="btn btn-success" controller="course" action="search">Search for courses</g:link>
                                </div>
                            </g:else>

                            <h2>In-Progress Decks</h2>

                            <g:if test="${deckInstanceList.size() >
                                0}">
                                <div class="container">
                                    <g:each in="${deckInstanceList}">
                                        <div class="col-lg-3">
                                            <div class="panel panel-default">
                                                <div class="panel-body">

                                                    <h3>
                                                        <g:link action="show" controller="deck" id="${it.id}">${it.title}</g:link>
                                                    </h3>

                                                    <div class="progress">
                                                        <div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width:60%">60%</div>
                                                        <!-- end progress-bar --> </div>
                                                    <!-- end progress -->

                                                    <div class="continue">
                                                        <g:link class="btn btn-success" action="show" controller="deck" id="${it.id}">Continue</g:link>
                                                    </div>

                                                </div>
                                                <!-- end panel-body --> </div>
                                            <!-- end panel --> </div>
                                        <!-- end col-lg-3 --> </g:each>

                                </div>
                                <!-- end container -->
                                <g:link class="view-more" action="list" controller="deck">
                                    View More Decks
                                    <span class="glyphicon glyphicon-arrow-right"></span>
                                </g:link>
                            </g:if>
                            <g:else>
                                <div class="container">
                                    <p>You haven't added any decks yet! Get started:</p>
                                    <g:link class="btn btn-success" controller="deck" action="create">Create New Deck</g:link>
                                    <g:link class="btn btn-success" controller="deck" action="search">Search for Decks</g:link>
                                </div>
                            </g:else>

                        </sec:ifAllGranted>

                        <sec:ifAllGranted roles="ROLE_INSTRUCTOR">
                            <h2>Current Courses</h2>
                            <h2>Scheduled Courses</h2>
                            <g:if test="${myCourses.size() >
                                0}">
                                <div class="container">

                                    <g:each in="${myCourses}">
                                        <div class="col-lg-3">
                                            <div class="panel panel-default">
                                                <div class="panel-body">

                                                    <h3>
                                                        <g:link action="show" controller="course" id="${it.id}">${it.title}</g:link>
                                                    </h3>

                                                    <div class="current-chapter">
                                                        <h4>${it.getChapters().size()} chapters</h4>
                                                    </div>

                                                    <div class="continue">
                                                        <g:link class="btn btn-success" action="students" controller="course" id="${it.id}">${Registration.countByCourse(it)} Enrolled</g:link>
                                                    </div>

                                                </div>
                                                <!-- end panel-body --> </div>
                                            <!-- end panel --> </div>
                                        <!-- end col-lg-3 --> </g:each>

                                </div>
                                <!-- end container -->

                                <g:link class="view-more" action="list" controller="course">
                                    View More Courses
                                    <span class="glyphicon glyphicon-arrow-right"></span>
                                </g:link>

                            </g:if>
                            <g:else>
                                <div class="container">
                                    <p>You haven't created any courses yet! Get started:</p>
                                    <g:link class="btn btn-success" controller="course" action="create">Create a Course</g:link>
                                </div>
                            </g:else>

                            <h2>Recently Updated Chapters</h2>
                            <g:if test="${myCourses.size() >
                                0}">
                                <div class="container">

                                    <g:each in="${myCourses.sort()}">
                                        <!-- should alter sort() to be by lastUpdated , implements Comparable -->
                                        <div class="col-lg-3">
                                            <div class="panel panel-default">
                                                <div class="panel-body">

                                                    <h3>
                                                        <g:link action="show" controller="course" id="${it.id}">${it.title}</g:link>
                                                    </h3>

                                                    <div class="current-chapter">
                                                        <h4>${it.getChapters().sort()[0]?.title} chapter</h4>
                                                        <h4>
                                                            Last Updated : 
                                                        ${it.lastUpdated.month + 1} -
                                                        ${it.lastUpdated.getAt(Calendar.DAY_OF_MONTH)} -
                                                        ${it.lastUpdated.year + 1900}
                                                        </h4>
                                                    </div>

                                                    <div class="continue">
                                                        <g:link class="btn btn-success" action="students" controller="course" id="${it.id}">${Registration.countByCourse(it)} Enrolled</g:link>
                                                    </div>

                                                </div>
                                                <!-- end panel-body --> </div>
                                            <!-- end panel --> </div>
                                        <!-- end col-lg-3 --> </g:each>

                                </div>
                                <!-- end container -->

                                <g:link class="view-more" action="list" controller="course">
                                    View More Courses
                                    <span class="glyphicon glyphicon-arrow-right"></span>
                                </g:link>

                            </g:if>
                            <g:else>
                                <div class="container">
                                    <p>You haven't created any courses yet! Get started:</p>
                                    <g:link class="btn btn-success" controller="course" action="create">Create a Course</g:link>
                                </div>
                            </g:else>

                        </sec:ifAllGranted>

                    </div>
                    <!-- end dashboard --> </sec:ifLoggedIn>

            </div>
        </div>
    </div>
</body>
</html>