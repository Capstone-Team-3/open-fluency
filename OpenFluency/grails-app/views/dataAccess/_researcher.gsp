<div class="container">
	<div>
		<h1 id="main">Researcher Data Access</h1>
		
		<div class="data-access">
			<h3>User Biographical Data</h3>
			<export:formats id="bio-data" formats="['csv']" action="exportBioData" controller="${controller}"/>
		</div>

		<div class="data-access">
			<h3>Flashcard Practice Usage Data</h3>
			<export:formats id="practice-data" formats="['csv']" action="exportPracticeData" controller="${controller}"/>
		</div>

		<div class="data-access">
			<h3>Flashcard Customization Data</h3>
			<export:formats id="customization-data" formats="['csv']" action="exportCustomizationData" controller="${controller}"/>
		</div>
			
		<div class="data-access">
			<h3>Detailed Quiz Answer Data</h3>
			<export:formats id="grades-data" formats="['csv']" action="exportAnswerData" controller="${controller}"/>
		</div>
			
		<div class="data-access">
			<h3>Quiz Data</h3>
			<export:formats id="quiz-data" formats="['csv']" action="exportQuizData" controller="${controller}"/>
		</div>

		<div class="data-access">
			<h3>Course Data</h3>
			<export:formats id="course-data" formats="['csv']" action="exportCourseData" controller="${controller}"/>
		</div>

		<div class="data-access">
			<h3>Flashcard Data</h3>
			<export:formats id="flashcard-data" formats="['csv']" action="exportFlashcardData" controller="${controller}"/>
		</div>

		<div class="data-access">
			<h3>Deck Data</h3>
			<export:formats id="deck-data" formats="['csv']" action="exportDeckData" controller="${controller}"/>
		</div>

		<div class="data-access">
			<h3>Languages and Alphabets</h3>
			<export:formats id="language-data" formats="['csv']" action="exportLanguageData" controller="${controller}"/>
		</div>

	</div>
</div>