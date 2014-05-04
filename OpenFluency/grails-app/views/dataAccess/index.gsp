<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"/>
	<r:require module="export"/>
</head>
<body>
	<div class="container">
		<ul class="breadcrumb">
			<li>
				<a href="${createLink(uri:'/') }">Home</a>
			</li>
		</ul>
		<div>
			<h1 id="main">Researcher Data Access</h1>
			
			<div class="data-access">
				<h3>User Biographical Data</h3>
				<export:formats id="bio-data" formats="['csv']" action="exportBioData" />
			</div>

			<div class="data-access">
				<h3>Flashcard Practice Usage Data</h3>
				<export:formats id="practice-data" formats="['csv']" action="exportPracticeData"/>
			</div>

			<div class="data-access">
				<h3>Flashcard Customization Data</h3>
				<export:formats id="customization-data" formats="['csv']" action="exportCustomizationData"/>
			</div>

			<div class="data-access">
				<h3>Flashcard Quiz Data</h3>
				<export:formats id="quiz-data" formats="['csv']" action="exportQuizData"/>
			</div>

			<div class="data-access">
				<h3>Course Performance Data</h3>
				<export:formats id="grades-data" formats="['csv']" action="exportGradeData"/>
			</div>
			
			<div class="data-access">
				<h3>Course Data</h3>
				<export:formats id="course-data" formats="['csv']" action="exportCourseData"/>
			</div>

			<div class="data-access">
				<h3>Flashcard Data</h3>
				<export:formats id="flashcard-data" formats="['csv']" action="exportFlashcardData"/>
			</div>

			<div class="data-access">
				<h3>Deck Data</h3>
				<export:formats id="deck-data" formats="['csv']" action="exportDeckData"/>
			</div>

			<div class="data-access">
				<h3>Languages and Alphabets</h3>
				<export:formats id="language-data" formats="['csv']" action="exportLanguageData"/>
			</div>

		</div>
	</div>
</body>
</html>