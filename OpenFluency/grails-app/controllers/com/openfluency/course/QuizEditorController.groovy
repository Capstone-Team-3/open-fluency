
package com.openfluency.course

import grails.plugin.springsecurity.annotation.Secured
import au.com.bytecode.opencsv.CSVReader
import grails.converters.JSON
import java.text.DateFormat
import java.text.SimpleDateFormat
import cscie99.team2.lingolearn.shared.Image;
import cscie99.team2.lingolearn.shared.Sound;

import com.openfluency.Constants

class QuizEditorController {

	def springSecurityService
	def quizService
	 private File mediaFile;
     private String mediaTopDir;
     private String mediaFolder;

	@Secured(['ROLE_INSTRUCTOR'])
	  def uploadFile(){

	  	log.info "Ajax is finally working"

	  def f =params.file
	  log.info "Do we have a file passed into controller?"
	  log.info f
	  Sound soundInstance
      // File file=new File("demodan.mp3")
   /*      if(file.exists())
            f.transferTo(file)
         else{
            file.createNewFile()
            f.transferTo(file)
         }  */
				def extension = f.fileItem.name.lastIndexOf('.').with {it != -1 ? f.fileItem.name.substring(it + 1) : f.fileItem.name}
                File outputFile = new File("${new Date().time}.${extension}")
                f.transferTo(outputFile)
                 
                if (extension != "mp3" && extension != "wav"){
                    outputFile.delete()
                    log.info "File needs to be a .mp3 file or .wav file.  No other types supported."
                } 

                if (extension == "mp3"){
                	log.info "we have identified the file as an mp3 file...."
                	File destinationname = new File("web-app" + File.separator + "media");
           			File folder = File.createTempFile(QuizService.getUniqueName(),"", destinationname); 
           			Boolean isDeleted = folder.delete(); 
            		folder.deleteOnExit();
           		    folder.mkdir();
           			mediaFolder = folder.getAbsolutePath();
                    //csvFile = outputFile
                      this.mediaFile = new File(mediaFolder + File.separator + outputFile.getName());
                   // this.mediaFile = new File(mediaFolder + File.separator + f.getName());
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
                       //  need 
                        // /OpenFluency/media/28d304f3-74a3-4999-b325-87965a8055141904306693715463725/image/1.jpg')
                        // have
                       // /OpenFluency//cs599/project/OpenFluency/web-app/media/4e2ec1d6-19da-4841-aa2c-71f705527b75282433027753533035/1462297410876.mp3
                        soundInstance = new Sound()
                        int x = mediaFolder.indexOf(grailsApplication.config.mediaFolder)
                        String theFolder = mediaFolder.substring(x)
                     /*   String soundUri = "/OpenFluency/" + theFolder + File.separator + this.mediaFile.getName()
                     	soundInstance = new Sound(
                        	soundUri : soundUri
						).save(failOnError: true)   */
                      // soundInstance.setSoundUri("/OpenFluency/" + theFolder + File.separator + this.mediaFile)
                        soundInstance.setSoundUri("/OpenFluency/" + theFolder + File.separator + this.mediaFile.getName())
                        log.info soundInstance.getSoundUri()
                    	
                }

	   //  soundInstance = new Sound()

	  	   // Create the audio instance
       // Audio audioInstance = mediaService.createAudio(params.url, params.blob.getBytes(), params['pronunciation.id'])

        //JSON.use('deep')
        render soundInstance as JSON   



	  //	response.contentType = grailsApplication.config.grails.mime.types[params.format]
	//	response.setHeader("Content-disposition", "attachment; filename=${quizTitle}.mp3")
    /*   File file=new File("demo.csv")
         if(file.exists())
            fileData.transferTo(file)
         else{
            file.createNewFile()
            fileData.transferTo(file)
         }
         render "Uploaded Successfully"
         */
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

		try {
			String title = params.title
			Integer maxCardTime = params.maxCardTime ? params.maxCardTime as Integer : 0
			Date liveTime = params.liveTime
			Date endTime = params.endTime
			
			// The format is: question_type, question, correct_answer, wrong_answer1, wrong_answer2, ...
			String csv = params.questions
			
			quizInstance.title = title
			quizInstance.liveTime = liveTime
			quizInstance.endTime = endTime
			quizInstance.maxCardTime = maxCardTime
			quizInstance.save(failOnError: true)
			
			List<QuestionOption> oldOptions = new ArrayList<QuestionOption>()
			
			Question.findAllByQuiz(quizInstance).each {
				QuestionOption.findAllByQuestion(it).each {
					oldOptions.add(it)
				}
			}
			
			saveQuestions(quizInstance, csv)
			
			oldOptions.each {
				it.delete();
			}
			
			redirect controller: "quiz", action: "show", id: quizInstance.id
		}
		catch (Exception e) {
			
			log.error "Error: ${e.message}", e
			
			flash.message = "Something went wrong, please try again"
			redirect action: "create", id: quizInstance.course.id
		}
	}
	
	private void saveQuestions(Quiz quizInstance, String csv) {
		CSVReader reader = new CSVReader(new StringReader(csv))
		

		Sound snd = null
        Image im = null

		String[] line;
		while ((line = reader.readNext()) != null) {
			int lineSize = line.size()
			if (lineSize > 3) {
				String questionType = line[0]
				String questionString = line[1]
				im = new Image()
                im.setImageUri(line[2])
				String correctAnswer = line[3]
				
				
				List<String> wrongAnswers = new ArrayList<String>()
				
				for (int i = 4; i < lineSize; i++) {
					wrongAnswers.add(line[i])
				}
					
				Question question = new Question(quiz: quizInstance, question: questionString, questionType: Constants.MANUAL, image: im).save(failOnError: true)
				
				new QuestionOption(question: question, option: correctAnswer, answerKey: 1).save(failOnError: true)
				
				wrongAnswers.each {
					new QuestionOption(question: question, option: it, answerKey: 0).save(failOnError: true)
				}
			}
		}
	}
}