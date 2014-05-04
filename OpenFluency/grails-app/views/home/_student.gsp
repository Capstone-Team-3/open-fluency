<h2>In-Progress Courses</h2>

<g:if test="${registrations}">
    <div class="dashboard-container">
        <g:render template="/course/pill" collection="${registrations}" var="registrationInstance"/>
    </div>
    <g:link class="view-more" action="list" controller="course">
        View More Courses
        <span class="glyphicon glyphicon-arrow-right"></span>
    </g:link>
</g:if>

<g:else>
    <div class="dashboard-container">
        <div class="col-lg-6">
            <p>You haven't enrolled in any courses yet! Get started:</p>
            <g:link class="btn btn-success" controller="course" action="search">Search for courses</g:link>
        </div>
    </div>
</g:else>

<h2>In-Progress Decks</h2>
<g:if test="${deckInstanceList}">
    <div class="dashboard-container">
        <g:each in="${deckInstanceList}">
            <div class="col-lg-4">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4>
                            <g:link action="show" controller="deck" id="${it.id}">${it.title}</g:link>
                        </h4>
                    </div>
                    <div class="panel-body">
                        <div class="donut-container">
                            <div class="col-lg-4 progress-donut center" data-progress="${it.progress[0]}" id="meaning-progress-${it.id}">
                                <p>Meaning</p>
                            </div>

                            <div class="col-lg-4 progress-donut center" data-progress="${it.progress[1]}" id="pronunciation-progress-${it.id}">
                                <p>Pronunciation</p>
                            </div>
                        </div>
                    </div>
                    <div class="panel-footer">
                        <div class="continue">
                            <g:link class="btn btn-success" action="show" controller="deck" id="${it.id}">Continue</g:link>
                        </div>
                    </div>
                </div>
                <!-- end panel --> </div>
            <!-- end col-lg-3 --> </g:each>

    </div>
    <!-- dashboard-container -->

    <g:link class="view-more" action="list" controller="deck">
        View More Decks
        <span class="glyphicon glyphicon-arrow-right"></span>
    </g:link>
</g:if>
<g:else>
    <div class="dashboard-container">
        <div class="col-lg-6">
            <p>You haven't added any decks yet! Get started:</p>
            <g:link class="btn btn-success" controller="deck" action="create">Create New Deck</g:link>
            <g:link class="btn btn-success" controller="deck" action="search">Search for Decks</g:link>
        </div>
    </div>
</g:else>

<g:javascript>initializeDonuts();</g:javascript>