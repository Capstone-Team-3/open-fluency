package com.openfluency.course

import grails.plugin.springsecurity.annotation.Secured
import java.text.DateFormat
import java.text.SimpleDateFormat
 import java.util.zip.*

class QuizController {

	def springSecurityService
		def courseService
		def quizService
		def exportService 
		def grailsApplication 

	@Secured(['ROLE_INSTRUCTOR'])
	def create(Course courseInstance) { 
			[courseInstance: courseInstance]
	}

	@Secured(['ROLE_INSTRUCTOR'])
		def save() {
			Course courseInstance = Course.load(params["course.id"] as Long)

				// First check that it's the owner of the course who's creating it
				if(courseInstance.owner.id != springSecurityService.principal.id){
					flash.message = "You're not authorized to create a quiz for a course you don't own!"
						redirect action: "index", controller: "home"
						return
				}

			println "TEST ELEMENT ${params.testElement}"

				// Build the quiz
				Quiz quizInstance = quizService.createQuiz(
						params.title, 
						params.liveTime,
						params.endTime,
						params.maxCardTime ? params.maxCardTime as Integer : 0, 
						params.testElement as Integer,
						params.list('flashcardId'),
						courseInstance
						)

				if(!quizInstance) {
					flash.message = "Something went wrong, please try again"
						redirect action: "create", id: params["course.id"]
				}

			if(quizInstance.hasErrors()) {
				flash.message = "Something went wrong, please try again"
			}

			redirect action: "show", id: quizInstance.id
		}

	@Secured(['ROLE_INSTRUCTOR'])
		def show(Quiz quizInstance) {
			// Only the instructor that owns the course should be allowed access
			if(springSecurityService.principal.id != quizInstance?.course?.owner?.id) {
				flash.message = "You're not allowed to view this quiz"
					redirect action: "index", controller: "home"
					return
			}

			[quizInstance: quizInstance,
				isOwner: (springSecurityService.principal.id == quizInstance.course.owner.id)
					]
		}

	@Secured(['ROLE_INSTRUCTOR'])
		def edit(Quiz quizInstance){
			// Only the instructor that owns the course should be allowed access
			if(springSecurityService.principal.id != quizInstance?.course?.owner?.id) {
				flash.message = "You're not allowed to edit this quiz"
					redirect action: "index", controller: "home"
					return
			}

			[quizInstance: quizInstance,
				isOwner: (springSecurityService.principal.id == quizInstance.course.owner.id)
					]
		}

	@Secured(['ROLE_INSTRUCTOR'])
		def update(Quiz quizInstance) {
			// First check that it's the owner of the course who's creating it
			if(quizInstance.course.owner.id != springSecurityService.principal.id){
				flash.message = "You're not authorized to edit a quiz for a course you don't own!"
					redirect action: "index", controller: "home"
					return
			}

			// Build the quiz
			quizService.updateQuiz(
					quizInstance,
					params.title, 
					params.liveTime,
					params.endTime,
					params.maxCardTime as Integer, 
					params.testElement as Integer,
					params.list('flashcardId')
					)

				if(quizInstance.hasErrors()) {
					flash.message = "Something went wrong, please try again"
				}

			redirect action: "show", id: quizInstance.id
		}

	@Secured(['ROLE_INSTRUCTOR'])
		def delete(Quiz quizInstance) {
			// Only allow editing for owner
			if(springSecurityService.principal.id != quizInstance.course?.owner?.id) {
				flash.message = "You don't have permissions to delete this quiz"
					redirect action: "index", controller: "home"
					return
			}

			// Get course id to redirect afterwards
			Long courseId = quizInstance.course.id

				// Delete it
				courseService.deleteQuiz(quizInstance)

				redirect action: "show", controller: "course", id: courseId
		}

	@Secured(['ROLE_INSTRUCTOR'])
		def deleteQuestion(Question questionInstance) {
			// Only allow editing for owner
			if(springSecurityService.principal.id != questionInstance.quiz.course?.owner?.id) {
				flash.message = "You don't have permissions to delete this question"
					redirect action: "index", controller: "home"
					return
			}

			// Get course id to redirect afterwards
			Long quizId = questionInstance.quiz.id

				// Delete it
				courseService.deleteQuestion(questionInstance)

				redirect action: "show", controller: "quiz", id: quizId
		}

	@Secured(['ROLE_STUDENT'])
		def take(Quiz quizInstance) {

			// Check that the quiz actually exists
			if(!quizInstance) {
				flash.message = "The test doesn't exist!"
					return
			}

			// Check if the student has completed this quiz
			Grade gradeInstance = quizService.getGrade(quizInstance)
				if(gradeInstance) {
					redirect action: "report", id: gradeInstance.id
						return
				}

			// Check that it's live
			if(quizInstance.liveTime != null && quizInstance.liveTime > new Date()) {
				flash.message = "The test is not live yet!"
					redirect action: "show", controller: "course", id: quizInstance.course.id
					return
			}

			// Validate the quiz can be started
			Answer firstAnswer = quizService.startQuiz(quizInstance, session.id)
				if(firstAnswer) {
					// Render view to answer first question
					render view: "quiz", model: [answerInstance: firstAnswer, quizInstance: quizInstance]
				}
				else {
					flash.message = "Quiz cannot be started"
						redirect action: "show", controller: "course", id: quizInstance.course.id
						return
				}

		}

	def nextQuestion(Answer answerInstance) {
		Quiz quizInstance = Quiz.load(params.quiz)

			// Answer current question
			quizService.answerQuestion(answerInstance, params.option as Long, session.id)

			Answer answer = quizService.nextQuestion(answerInstance.question.quiz)

			// No more questions left - check if the user has completed the course
			if(!answer) {
				Grade gradeInstance = quizService.finalizeQuiz(answerInstance.question.quiz)
					if(gradeInstance) {
						redirect action: "report", id: gradeInstance.id
							return
					}
			}

		render view: "quiz", model: [answerInstance: answer, quizInstance: quizInstance]
	}

	def report(Grade gradeInstance) {
		[answerInstanceList: quizService.getAnswersByLoggedUser(gradeInstance.quiz), gradeInstance: gradeInstance]
	}

	@Secured(['ROLE_INSTRUCTOR'])
		def quizImport(Course courseInstance) {
			[courseInstance: courseInstance]
		}

	/**
	 * Upload Quiz from either a .csv or .zip file.  
	 */

	def importQuiz(){

		 Course courseInstance = Course.load(params["course.id"] as Long)

		 //First check that it's the owner of the course who's creating it
			 if(courseInstance.owner.id != springSecurityService.principal.id){
				 flash.message = "You're not authorized to create a quiz for a course you don't own!"
				 redirect action: "index", controller: "home"
				 return
			 }
			String title = params.title
			// convert string formatted tate to date objects
			String lTime = params.liveTime;
			String eTime = params.endTime;
			Date liveTime = null;

			if (lTime==null || lTime==""){
  			// if liveTime is not set simply default to today's date.
				liveTime = new Date();
			} else {
			DateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
			liveTime = format.parse(lTime);
			}
			Date endTime = null;
			if (eTime==null || eTime==""){
				endTime = null;
			} else {
			DateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
			endTime = format.parse(eTime);
			}

			Integer maxCardTime = params.maxCardTime ? params.maxCardTime as Integer : 0

			request.getMultiFileMap().csvData.eachWithIndex { f, i ->
				List result = quizService.importQuiz(title, liveTime, endTime, maxCardTime, courseInstance, f)
					if(result.isEmpty()) {
						flash.message = "You succesfully uploaded your Quiz!"
					}
					else {
						flash.message = result.join(",\n")
					}
			}

		redirect(action: "show", controller: "course", id: courseInstance.id)
	}



/*
This method exports a quiz to a csv file or if it has images and/or sound associated with the quiz, exports the
quiz to a .zip file.   It leverages the grails export plugin to create the csv file
*/

@Secured(['ROLE_INSTRUCTOR'])
def export(Quiz quizInstance) {
		def quizTitle = quizInstance.title

		response.contentType = grailsApplication.config.grails.mime.types[params.format]
		response.setHeader("Content-disposition", "attachment; filename=${quizTitle}.csv")
		boolean hasMedia = false

		def listQuestions = quizInstance.getQuestions()

		def mediaList = listQuestions.findAll{it -> it.image != null || it.sound !=null}
		// if it has any media associated with the qustions of the quiz, we need to export a .zip file
		if (mediaList.size() > 0){
			hasMedia = true
		}

		List llist = quizInstance ? Question.findAllByQuiz(quizInstance) : []	
		int i = 0

		List fields = ["question_number",
					   "standard_question",	 
					   "image_question",
					   "audio_question", 
					   "correct_answer",
					   "wrong_answers"]
					   
		Map labels = ["question_number":"question_number",
					   "standard_question":"standard_question",	
					     "image_question":"image_question",
					   "audio_question":"audio_question",	   
					   "correct_answer":"correct_answer",
					   "wrong_answers":"wrong_answers"]
					  
					 
		Map formatters = ["question_number": {domain, value -> return ++i},
						"standard_question": {domain, value -> return domain?.question},
						"image_question": {domain, value -> String image_link = domain?.image?.getImageUri()
                                       if (image_link != null){
                                       	int x = image_link.lastIndexOf('/')
                                       	String imageFile = image_link.substring(x)
                                       	return "image" + imageFile
                                       } else {return null}
							},
						"audio_question": {domain, value -> String audio_link = domain?.sound?.getSoundUri()
 										if (audio_link != null){
                                       	int x = audio_link.lastIndexOf('/')
                                       	String audioFile = audio_link.substring(x)
                                       	return "sound" + audioFile
                                       } else {return null}
						},
						"correct_answer": {domain, value -> return domain.getOptions().findAll{it.answerKey==1}.option},
						"wrong_answers": {domain, value -> return domain.getOptions().findAll{it.answerKey==0}.option}]

		Map parameters = ["separator": ","]
		
		if (hasMedia){
		File quizDir = new File(quizInstance.title)
		File quizDirZip = new File(quizDir.getName() + ".zip")
		def fileOutputZipStream = new FileOutputStream(quizDirZip)
		quizDir.mkdir()
		File file = new File(quizDir.getName() + File.separator + "${quizTitle}.csv")
    	def fileOutputStream = new FileOutputStream(file)
    	exportService.export("csv", fileOutputStream, llist, fields, labels, formatters, parameters)
    	File imageDir = new File(quizDir.getName() + File.separator + "image")
    	File soundDir = new File(quizDir.getName() + File.separator + "sound")
    	imageDir.mkdir()
    	soundDir.mkdir()
    	
    	def imageList = listQuestions.findAll{it -> it.image != null

    		String image_link = it?.image?.getImageUri()
    		if (image_link != null){
            String imageFile = image_link.substring(image_link.lastIndexOf('/'))
            String sName = it.image.getImageUri()
            String iFile = sName.substring(sName.indexOf('/media'))
      
    		def src = new File(request.getSession().getServletContext().getRealPath("/") + iFile)
     		def dst = new File(quizDir.getName() + File.separator + "image" + imageFile)
            dst << src.bytes
    	}
    }

    	def soundList = listQuestions.findAll{it -> it.sound != null

    		String sound_link = it?.sound?.getSoundUri()
    		if (sound_link != null){
            String soundFile = sound_link.substring(sound_link.lastIndexOf('/'))
            String sName = it.sound.getSoundUri()
            String iFile = sName.substring(sName.indexOf('/media'))
      
    		def src = new File(request.getSession().getServletContext().getRealPath("/") + iFile)
     		def dst = new File(quizDir.getName() + File.separator + "sound" + soundFile)
            dst << src.bytes
    	}
    }
   	   createZipFile(quizDir, quizDirZip)

       response.setContentType("application/octet-stream")
       response.setHeader("Content-disposition", "attachment;filename=${quizDirZip.getName()}")
       quizDirZip.withInputStream { response.outputStream << it }
       quizDir.deleteDir()
       quizDirZip.delete() 

    	} else{
    		// plain vanilla csv file
    		exportService.export("csv", response.outputStream, llist, fields, labels, formatters, parameters)
    	}
		
		 [questionInstanceList: Question.list(params)]
		 
	}



	def createZipFile(File sourceDir, File zipFile) {
               			log.info "Started creating Zip file";
                        FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
                        ZipOutputStream zipOutputStream  = new ZipOutputStream(fileOutputStream)
                      	addDirectory(zipOutputStream, sourceDir)
                        zipOutputStream .close()
                        log.info "Finished creating Zip file";
        }
 
    def addDirectory(ZipOutputStream zipOutputStream, File fileSource) {
               
                File[] files = fileSource.listFiles();

                for(int i=0; i < files.length; i++)
                {
                        if(files[i].isDirectory())
                        {
                                addDirectory(zipOutputStream, files[i]);
                                continue;
                        }
                                log.info "Adding file: {files[i].getName()}"
                               
                                byte[] buffer = new byte[1024];
                               
                                FileInputStream fileInputStream = new FileInputStream(files[i]);
                                log.info "${files[i].getName()}"
                                if (files[i].getName().contains(".mp3")||(files[i].getName().contains(".wav"))){
                               	zipOutputStream.putNextEntry(new ZipEntry("/sound/"+files[i].getName()));
                            	} else if (files[i].getName().contains(".jpg") || (files[i].getName().contains(".gif"))) {
                            		  zipOutputStream.putNextEntry(new ZipEntry("/image/"+files[i].getName()));
                            	}
                            	else if (files[i].getName().contains(".csv")) {
                            		  zipOutputStream.putNextEntry(new ZipEntry(files[i].getName()));
                            	}

                                int length;
                         
                                while((length = fileInputStream.read(buffer)) > 0)
                                {
                                   zipOutputStream.write(buffer, 0, length);
                                }
                                 zipOutputStream.closeEntry();
                                 fileInputStream.close();

                }
               
        }
 
}

