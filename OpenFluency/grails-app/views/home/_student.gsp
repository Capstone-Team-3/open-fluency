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

<g:if test="${deckInstanceList}">
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
                        </div>
                        <div class="continue">
                            <g:link class="btn btn-success" action="show" controller="deck" id="${it.id}">Continue</g:link>
                        </div>
                    </div>
                </div>
            </div>
        </g:each>
    </div>
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