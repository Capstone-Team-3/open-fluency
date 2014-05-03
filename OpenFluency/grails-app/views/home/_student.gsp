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
            <div class="col-lg-3">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4>
                            <g:link action="show" controller="deck" id="${it.id}">${it.title}</g:link>
                        </h4>
                    </div>
                    <div class="panel-body">
                        <p>Meaning Progress</p>
                        <div class="progress">
                            <div class="progress-bar" role="progressbar" aria-valuenow="${it.progress[0]}" aria-valuemin="0" aria-valuemax="100" style="width:${it.progress[0]}%">${it.progress[0]}%</div>
                        </div>
                        <p>Pronunciation Progress</p>
                        <div class="progress">
                            <div class="progress-bar" role="progressbar" aria-valuenow="${it.progress[1]}" aria-valuemin="0" aria-valuemax="100" style="width:${it.progress[1]}%">${it.progress}%</div>
                        </div>
                    </div>
                    <div class="panel-footer">
                        <div class="continue">
                            <g:link class="btn btn-success" action="show" controller="deck" id="${it.id}">Continue</g:link>
                        </div>
                    </div>
                </div><!-- end panel -->
            </div><!-- end col-lg-3 -->
        </g:each>

    </div><!-- dashboard-container -->

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