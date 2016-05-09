package com.openfluency.course

import grails.plugin.springsecurity.annotation.Secured
import au.com.bytecode.opencsv.CSVReader
import grails.converters.JSON
import java.text.DateFormat
import java.text.SimpleDateFormat
import cscie99.team2.lingolearn.shared.Image;
import cscie99.team2.lingolearn.shared.Sound;

import com.openfluency.Constants
import grails.transaction.Transactional

@Transactional

class QuizEditorController {

	def springSecurityService
	def quizService
	def grailsApplication
	def courseService
	private File mediaFile;
	private String mediaTopDir;
	private String mediaFolder;


	@Secured(['ROLE_INSTRUCTOR'])
	def uploadMediaFile(){
		def f =params.file
		Sound soundInstance
		Image imageInstance
		def soundFormats = ["mp3", "wav", "oga", "aac"];
		def imageFormats =  ["jpg", "gif"];

		def extension = f.fileItem.name.lastIndexOf('.').with {
			it != -1 ? f.fileItem.name.substring(it + 1) : f.fileItem.name
		}
		File outputFile = new File("${new Date().time}.${extension}")
		f.transferTo(outputFile)

		// if (extension != "mp3" && extension != "wav"){
		if (!soundFormats.contains(extension) && !imageFormats.contains(extension)){
			outputFile.delete()
			log.info "File needs to be a .mp3, .wav, .oga, .aac, .gif, or .jpg format.  No other file types supported."
		}
		File destinationname = new File("web-app" + File.separator + "media");
		File folder = File.createTempFile(QuizService.getUniqueName(),"", destinationname);
		Boolean isDeleted = folder.delete();
		folder.deleteOnExit();
		folder.mkdir();
		mediaFolder = folder.getAbsolutePath();
		this.mediaFile = new File(mediaFolder + File.separator + outputFile.getName());
		//Create parent folder, ok if already exists
		new File(this.mediaFile.getParent()).mkdirs();
		FileOutputStream fos = new FileOutputStream(this.mediaFile);
		FileInputStream fis = new FileInputStream(outputFile);
		byte[] buffer = new byte[2048];
		int len;
		while ((len = fis.read(buffer)) > 0) {
			fos.write(buffer, 0, len);
		}
		fos.close();

		int x = mediaFolder.indexOf(grailsApplication.config.mediaFolder)
		String theFolder = mediaFolder.substring(x)
		outputFile.delete()
		render "/OpenFluency/" + theFolder + File.separator + this.mediaFile.getName();
	}


	@Secured(['ROLE_INSTRUCTOR'])
	def create(Course courseInstance) {
		[courseInstance: courseInstance]
	}

	@Secured(['ROLE_INSTRUCTOR'])
	def edit(Quiz quizInstance){
		// Only the instructor that owns the course should be allowed access
		if(springSecurityService.principal.id != quizInstance?.course?.owner?.id) {
			flash.message = "You're not allowed to edit this quiz"
			redirect action: "index", controller: "home"
			return
		}

		Course courseInstance = quizInstance.course

		[quizInstance: quizInstance, courseInstance: courseInstance]
	}

	@Secured(['ROLE_INSTRUCTOR'])
	def save () {

		Course courseInstance = Course.load(params["course.id"] as Long)

		// First check that it's the owner of the course who's creating it
		if(courseInstance.owner.id != springSecurityService.principal.id){
			flash.message = "You're not authorized to create a quiz for a course you don't own!"
			redirect action: "index", controller: "home"
			return
		}

		String title = params.title
		Integer maxCardTime = params.maxCardTime ? params.maxCardTime as Integer : 0
		Date liveTime = params.liveTime
		Date endTime = params.endTime

		// The format is: question_type, question, correct_answer, wrong_answer1, wrong_answer2, ...
		String csv = params.questions


		try {

			Quiz quizInstance = new Quiz(
			course: courseInstance,
			title: title,
			enabled: true,
			liveTime: liveTime,
			endTime: endTime,
			maxCardTime: maxCardTime
			).save(failOnError: true)

			saveQuestions(quizInstance, csv)

			redirect controller: "quiz", action: "show", id: quizInstance.id
		}
		catch (Exception e) {

			log.error "Error: ${e.message}", e

			flash.message = "Something went wrong, please try again"
			redirect action: "create", id: courseInstance.id
		}
	}

	@Secured(['ROLE_INSTRUCTOR'])
	def update(Quiz quizInstance) {
		// First check that it's the owner of the course who's creating it
		if(quizInstance.course.owner.id != springSecurityService.principal.id){
			flash.message = "You're not authorized to edit a quiz for a course you don't own!"
			redirect action: "index", controller: "home"
			return
		}

		Course courseInstance = quizInstance.getCourse();
		//quizInstance.delete();
		String title = params.title
		Integer maxCardTime = params.maxCardTime ? params.maxCardTime as Integer : 0
		Date liveTime = params.liveTime
		Date endTime = params.endTime

		// The format is: question_type, question, correct_answer, wrong_answer1, wrong_answer2, ...
		String csv = params.questions

		try {

			Quiz updatedQuiz = new Quiz(
			course: courseInstance,
			title: title,
			enabled: true,
			liveTime: liveTime,
			endTime: endTime,
			maxCardTime: maxCardTime
			).save(failOnError: true)

			saveQuestions(updatedQuiz, csv)


			// Only allow editing for owner
			if(springSecurityService.principal.id != quizInstance.course?.owner?.id) {
				flash.message = "You don't have permissions to delete this quiz"
				redirect action: "index", controller: "home"
				return
			}
			Long courseId = quizInstance.course.id
			courseService.deleteQuiz(quizInstance)

			redirect controller: "quiz", action: "show", id: updatedQuiz.id
		}
		catch (Exception e) {

			log.error "Error: ${e.message}", e

			flash.message = "Something went wrong, please try again"

			redirect action: "create", id: quizInstance.courseId
		}
	}

	private void saveQuestions(Quiz quizInstance, String csv) {
		CSVReader reader = new CSVReader(new StringReader(csv))

		int counter = 0
		Sound snd = null
		Image im = null
		def qt = null
		File destinationname = new File("web-app" + File.separator + "media");
		File folder = File.createTempFile(QuizService.getUniqueName(),"", destinationname);
		Boolean isDeleted = folder.delete();
		folder.deleteOnExit();
		folder.mkdir();
		mediaFolder = folder.getAbsolutePath();

		String[] line;
		while ((line = reader.readNext()) != null) {

			if (line[1].trim().equals("")&& line[2].trim().equals("") && line[3].trim().equals("")) {
				continue;
			}
			int lineSize = line.size()
			if (lineSize > 3) {
				String questionType = line[0]
				qt = com.openfluency.Constants.MANUAL
				String questionString = line[1]


				if (!line[2].trim().equals("")){
					im = new Image()
					int x = mediaFolder.indexOf(grailsApplication.config.mediaFolder)
					String theFolder = mediaFolder.substring(x)
					im.setImageUri(line[2])
					qt = com.openfluency.Constants.IMAGE
					questionString = "Image"
				} else {
					im = null
				}

				if (!line[3].trim().equals("")){
					snd = new Sound()
					int x = mediaFolder.indexOf(grailsApplication.config.mediaFolder)
					String theFolder = mediaFolder.substring(x)
					snd.setSoundUri(line[3])
					qt = com.openfluency.Constants.SOUND
					questionString = "Sound"
				} else {
					snd = null
				}



				String correctAnswer = line[4]

				List<String> wrongAnswers = new ArrayList<String>()

				for (int i = 5; i < lineSize; i++) {
					wrongAnswers.add(line[i])
				}

				Question question = new Question(quiz: quizInstance, question: questionString, questionType: qt, image: im, sound: snd).save(failOnError: true)

				new QuestionOption(question: question, option: correctAnswer, answerKey: 1).save(failOnError: true)

				wrongAnswers.each {
					// only create a question option if field is not blank
					if (!it.trim().equals("")){
					new QuestionOption(question: question, option: it, answerKey: 0).save(failOnError: true)
					}
				}
			}
		}

	}
}
