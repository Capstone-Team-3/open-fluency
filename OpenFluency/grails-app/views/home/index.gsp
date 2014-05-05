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
                    <div class="jumbotron center">
                        <img class="animated fadeInUp" src="images/logo-large.png"/>
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
                            <sec:username />'s Dashboard
                        </h1>

                        <sec:ifAllGranted roles="ROLE_STUDENT">
                            <g:render template="/home/student"/>
                        </sec:ifAllGranted>

                        <sec:ifAllGranted roles="ROLE_INSTRUCTOR">
                            <g:render template="/home/instructor"/>
                        </sec:ifAllGranted>

                        <sec:ifAllGranted roles="ROLE_RESEARCHER">
                            <g:render template="/dataAccess/researcher" model="[controller: "dataAccess"]"/>
                        </sec:ifAllGranted>

                    </div>
                </sec:ifLoggedIn>
            </div>
        </div>
    </div>
</body>
</html>