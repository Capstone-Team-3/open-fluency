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
					<p class="list-group-item-text">Anonymous OpenFluency user data, including each user's role (admin, student, instructor, or researcher), languages and language proficiency levels, flashcard decks, and courses.</p>
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
					<p class="list-group-item-text">Data on the images and audio files added by users to specific flashcards. A '0' or '1' in the 'Audio' or 'Image' columns specify that a customized image/audio file was ('0') or was not added ('1') to a flashcard, respectively.</p>
				</li>
				<li class="list-group-item">
					<div class="list-group-item-heading">
						<h4 class="pull-left">Detailed Quiz Answer Data</h4>
						<div class="pull-right h4"><span class="glyphicon glyphicon-download"></span><export:formats class="pull-right" id="grades-data" formats="['csv']" action="exportAnswerData" controller="${controller}"/></div>
					</div>
					<p class="list-group-item-text">Detailed data for each answer to a quiz question, including the correct answer, the specific student's answer, the element tested (e.g. pronunciation or English-Japanese meaning), and the answer status ('1' if the question was skipped). IDs are provided for the user, course, quiz, and quiz question.</p>
				</li>
				<li class="list-group-item">
					<div class="list-group-item-heading">
						<h4 class="pull-left">Quiz Data</h4>
						<div class="pull-right h4"><span class="glyphicon glyphicon-download"></span><export:formats class="pull-right" id="quiz-data" formats="['csv']" action="exportQuizData" controller="${controller}"/></div>
					</div>
					<p class="list-group-item-text">Data for each quiz, including the quiz's ID, type, time limit per question, number of questions, and student grades.</p>
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
					<p class="list-group-item-text">Data for each course, including the course's ID, title, description, language, start date, end date, course number, flashcard decks, and quizzes.</p>
				</li>
				<li class="list-group-item">
					<div class="list-group-item-heading">
						<h4 class="pull-left">Flashcard Data</h4>
						<div class="pull-right h4"><span class="glyphicon glyphicon-download"></span><export:formats class="pull-right" id="flashcard-data" formats="['csv']" action="exportFlashcardData" controller="${controller}"/></div>
					</div>
					<p class="list-group-item-text">Data for each flashcards, including the flashcard's ID, parent deck, focal and base alphabets, phrase/word/character and translation, and pronunciation. Also specifies whether an image and/or audio file were included, and the image URI, if applicable.</p>
				</li>
				<li class="list-group-item">
					<div class="list-group-item-heading">
						<h4 class="pull-left">Deck Data</h4>
						<div class="pull-right h4"><span class="glyphicon glyphicon-download"></span><export:formats class="pull-right" id="deck-data" formats="['csv']" action="exportDeckData" controller="${controller}"/></div>
					</div>
					<p class="list-group-item-text">Data for each deck, including the deck's ID, creator, title, description, algorithm, focal language, and base language.</p>
				</li>
				<li class="list-group-item">
					<div class="list-group-item-heading">
						<h4 class="pull-left">Languages and Alphabets</h4>
						<div class="pull-right h4"><span class="glyphicon glyphicon-download"></span><export:formats class="pull-right" id="language-data" formats="['csv']" action="exportLanguageData" controller="${controller}"/></div>
					</div>
					<p class="list-group-item-text">Includes the ID and name of each OpenFluency language and its alphabets.</p>
				</li>
			</ul>

		</div>
	</div>

</div>