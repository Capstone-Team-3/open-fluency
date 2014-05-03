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
				</br>
			</div>

			<div class="data-access">
				<h3>Flashcard Practice Usage Data</h3>
				<export:formats id="practice-data" formats="['csv']"/>
				</br>
			</div>

			<div class="data-access">
				<h3>Flashcard Quiz Data</h3>
				<export:formats id="quiz-data" formats="['csv']"/>
				</br>
			</div>

			<div class="data-access">
				<h3>Course Specific Data</h3>
				<export:formats id="course-data" formats="['csv']"/>
				</br>
			</div>

		</div>
	</div>
</body>
</html>