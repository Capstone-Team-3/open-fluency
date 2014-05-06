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
                    <div class="landing-page">
                        <div class="jumbotron center">
                            <img class="animated fadeInUp" src="images/logo-large.png"/>
                            <p>A flashcard application for foreign language study</p>
                            <g:link class="btn btn-success btn-lg" action="create" controller="user">Create your account</g:link>
                        </div>
                        
                        <div class="landing-content">
                            <div class="col-lg-4">
                                <section>
                                    <h2 class="text-center">Learn languages</h2>
                                    <p class="text-center role h4">Sign up as a <strong>Student</strong> to:</p>
                                    <ul>
                                        <li>Create decks of flashcards for foreign language learning.</li>
                                        <li>Customize flashcards with audio and images to help you learn.</li>
                                        <li>Share your decks with the world.</li>
                                        <li>Join language courses.</li>
                                    </ul>
                                </section>
                            </div>

                            <div class="col-lg-4">
                                <section>
                                    <h2 class="text-center">Build classes</h2>
                                    <p class="text-center role h4">Sign up as an <strong>Instructor</strong> to:</p>
                                    <ul>
                                        <li>Create private or public courses with all the flashcard decks needed for your classes.</li>
                                        <li>Create timed or untimed quizzes, which are automatically graded on completion.</li>
                                        <li>Track student grades and progress through your flashcard decks.</li>
                                        <li>Identify the characters, words, and phrases your students find most difficult.</li>
                                    </ul>
                                </section>
                            </div>

                            <div class="col-lg-4">
                                <section>
                                    <h2 class="text-center">Research acquisition</h2>
                                    <p class="text-center role h4">Sign up as a <strong>Researcher</strong> to:</p>
                                    <ul>
                                        <li>Access anonymous flashcard deck usage statistics and student performance metrics.</li>
                                        <li>View anonymous biographical user data.</li>
                                        <li>Create flashcard decks to research language acquisition.</li>
                                    </ul>
                                </section>
                            </div>

                            <div class="col-lg-12">
                                <h4 class="well text-center">
                                    Ready to get started?
                                    <g:link class="btn btn-success" action="create" controller="user">Sign Up</g:link>
                                </h4>
                            </div>
                        </div>
                    </div><!-- end landing-page -->
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

                    </div>f
                </sec:ifLoggedIn>
            </div>
        </div>
    </div>
</body>
</html>