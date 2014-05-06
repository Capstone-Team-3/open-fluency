<div class="researcher-dashboard">

	<h2>Available Research Data</h2>

	<div class="row">
		<div class="col-lg-6">
		
			<ul class="data-access list-group">
				
				<li class="list-group-item clearfix">
					<div class="list-group-item-heading">
						<h4 class="pull-left">User Biographical Data</h4> 
						<div class="pull-right h4"><span class="glyphicon glyphicon-download"></span><export:formats class="pull-right" id="bio-data" formats="['csv']" action="exportBioData" controller="${controller}"/></div>
					</div>
					<p class="list-group-item-text">Includes data on... </p>
				</li>
				<li class="list-group-item">
					<div class="list-group-item-heading">
						<h4 class="pull-left">Flashcard Practice Usage Data</h4>
						<div class="pull-right h4"><span class="glyphicon glyphicon-download"></span><export:formats class="pull-right" id="practice-data" formats="['csv']" action="exportPracticeData" controller="${controller}"/></div>
					</div>
					<p class="list-group-item-text">Includes data on... </p>
				</li>	
				<li class="list-group-item">
					<div class="list-group-item-heading">
						<h4 class="pull-left">Flashcard Customization Data</h4>
						<div class="pull-right h4"><span class="glyphicon glyphicon-download"></span><export:formats class="pull-right" id="customization-data" formats="['csv']" action="exportCustomizationData" controller="${controller}"/></div>
					</div>
					<p class="list-group-item-text">Includes data on... </p>
				</li>
				<li class="list-group-item">
					<div class="list-group-item-heading">
						<h4 class="pull-left">Detailed Quiz Answer Data</h4>
						<div class="pull-right h4"><span class="glyphicon glyphicon-download"></span><export:formats class="pull-right" id="grades-data" formats="['csv']" action="exportAnswerData" controller="${controller}"/></div>
					</div>
					<p class="list-group-item-text">Includes data on... </p>
				</li>
				<li class="list-group-item">
					<div class="list-group-item-heading">
						<h4 class="pull-left">Quiz Data</h4>
						<div class="pull-right h4"><span class="glyphicon glyphicon-download"></span><export:formats class="pull-right" id="quiz-data" formats="['csv']" action="exportQuizData" controller="${controller}"/></div>
					</div>
					<p class="list-group-item-text">Includes data on... </p>
				</li>
			</ul>

		</div><!-- end col -->

		<div class="col-lg-6">

			<ul class="data-access list-group">
								<li class="list-group-item">
					<div class="list-group-item-heading">
						<h4 class="pull-left">Course Data</h4>
						<div class="pull-right h4"><span class="glyphicon glyphicon-download"></span><export:formats class="pull-right" id="course-data" formats="['csv']" action="exportCourseData" controller="${controller}"/></div>
					</div>
					<p class="list-group-item-text">Includes data on... </p>
				</li>
				<li class="list-group-item">
					<div class="list-group-item-heading">
						<h4 class="pull-left">Flashcard Data</h4>
						<div class="pull-right h4"><span class="glyphicon glyphicon-download"></span><export:formats class="pull-right" id="flashcard-data" formats="['csv']" action="exportFlashcardData" controller="${controller}"/></div>
					</div>
					<p class="list-group-item-text">Includes data on... </p>
				</li>
				<li class="list-group-item">
					<div class="list-group-item-heading">
						<h4 class="pull-left">Deck Data</h4>
						<div class="pull-right h4"><span class="glyphicon glyphicon-download"></span><export:formats class="pull-right" id="deck-data" formats="['csv']" action="exportDeckData" controller="${controller}"/></div>
					</div>
					<p class="list-group-item-text">Includes data on... </p>
				</li>
				<li class="list-group-item">
					<div class="list-group-item-heading">
						<h4 class="pull-left">Languages and Alphabets</h4>
						<div class="pull-right h4"><span class="glyphicon glyphicon-download"></span><export:formats class="pull-right" id="language-data" formats="['csv']" action="exportLanguageData" controller="${controller}"/></div>
					</div>
					<p class="list-group-item-text">Includes data on... </p>
				</li>
			</ul>

		</div>
	</div>

</div>