
    package com.openfluency.course

    import com.openfluency.Constants
    import com.openfluency.flashcard.Flashcard
    import com.openfluency.language.Alphabet
    import com.openfluency.language.Language
    import com.openfluency.flashcard.Deck
    import com.openfluency.auth.User
    import com.openfluency.confuser.ConfuserFactory
    import com.openfluency.confuser.ConfuserInterface
    import grails.transaction.Transactional

    import java.io.File;
    import java.io.FileInputStream;
    import java.io.FileOutputStream;
    import java.io.IOException;
    import java.util.List;
    import java.util.zip.ZipEntry;
    import java.util.zip.ZipInputStream;

    @Transactional
    class QuizService {

        def deckService
        def springSecurityService
        Quiz createQuiz(String title, Date liveTime, Date endTime, Integer maxCardTime, Integer testElement, List flashcardIds, Course courseInstance) {
       
            // Create the quiz
            Quiz quizInstance = new Quiz(
                course: courseInstance, 
                title: title, 
                enabled: true, 
                liveTime: liveTime, 
                endTime: endTime, 
                maxCardTime: maxCardTime,
                quizType: Constants.FLASHCARD_QUIZ
                ).save()
            if(quizInstance.hasErrors()) {
                return quizInstance
            }

            // Now create the questions for each flashcard
            createQuestions(quizInstance, flashcardIds, testElement)

            return quizInstance
        }
        


        Quiz createQuiz(String title, Date liveTime, Integer maxCarTime, List<Question> questions, Course courseInstance) {
            
            // Save all of the Questions
            
        }
        void updateQuiz(Quiz quizInstance, String title, Date liveTime, Date endTime, Integer maxCardTime, Integer testElement, List flashcardIds) {

            // Create the quiz
            quizInstance.title = title
            quizInstance.enabled = true
            quizInstance.liveTime = liveTime
            quizInstance.endTime = endTime
            quizInstance.maxCardTime = maxCardTime
            quizInstance.save()

            if(quizInstance.hasErrors()) {
                return
            }

            createQuestions(quizInstance, flashcardIds, testElement)
        }

        void createQuestions(Quiz quizInstance, List flashcardIds, Integer testElement) {
            
            // Now create the questions for each flashcard
            flashcardIds.each {
                Flashcard flashcardInstance = Flashcard.get(it)


                // Create a number of options - right now it's hard coded to 3 but it can be easily user defined
                int maxOptions = 3

                if(flashcardInstance.deck.flashcardCount < maxOptions + 1) { // need at least 4 cards in the deck
                    log.info "Cannot create a quiz for a deck that has less cards than the required options"
                }

                // Create 3 options that are different
                Flashcard flashcard1 = deckService.getRandomFlashcard(flashcardInstance)
                Flashcard flashcard2 = deckService.getRandomFlashcard(flashcardInstance, [flashcardInstance.id, flashcard1.id])
                Flashcard flashcard3 = deckService.getRandomFlashcard(flashcardInstance, [flashcardInstance.id, flashcard1.id, flashcard2.id])
                
                // Create the question itself
                
                switch (testElement) {
        
                    case Constants.PRONUNCIATION:
                        Question question = new Question(quiz: quizInstance, question: flashcardInstance.primaryUnit.print, questionType: Constants.MANUAL).save(failOnError: true)
                        new QuestionOption(question: question, option: flashcardInstance.pronunciation.print, answerKey: 1).save(failOnError: true)
                        new QuestionOption(question: question, option: flashcard1.pronunciation.print, answerKey: 0).save(failOnError: true)
                        new QuestionOption(question: question, option: flashcard2.pronunciation.print, answerKey: 0).save(failOnError: true)
                        new QuestionOption(question: question, option: flashcard3.pronunciation.print, answerKey: 0).save(failOnError: true)
                        break;
                    case Constants.SYMBOL:
                        Question question = new Question(quiz: quizInstance, question: flashcardInstance.secondaryUnit.print, questionType: Constants.MANUAL).save(failOnError: true)
                        new QuestionOption(question: question, option: flashcardInstance.primaryUnit.print, answerKey: 1).save(failOnError: true)
                        new QuestionOption(question: question, option: flashcard1.primaryUnit.print, answerKey: 0).save(failOnError: true)
                        new QuestionOption(question: question, option: flashcard2.primaryUnit.print, answerKey: 0).save(failOnError: true)
                        new QuestionOption(question: question, option: flashcard3.primaryUnit.print, answerKey: 0).save(failOnError: true)
                        break;
                    case Constants.MEANING:
                    default:
                        Question question = new Question(quiz: quizInstance, question: flashcardInstance.primaryUnit.print, questionType: Constants.MANUAL).save(failOnError: true)
                        new QuestionOption(question: question, option: flashcardInstance.secondaryUnit.print, answerKey: 1).save(failOnError: true)
                        new QuestionOption(question: question, option: flashcard1.secondaryUnit.print, answerKey: 0).save(failOnError: true)
                        new QuestionOption(question: question, option: flashcard2.secondaryUnit.print, answerKey: 0).save(failOnError: true)
                        new QuestionOption(question: question, option: flashcard3.secondaryUnit.print, answerKey: 0).save(failOnError: true)
                        break;
                }
            }
        }
        
        void createConfuserQuestion(Quiz quizInstance, String question, String answer, Language language, Alphabet alphabet) {
            
            ConfuserFactory consuferFactory = new ConfuserFactory();
            ConfuserInterface confuser = consuferFactory.getConfuser(language);
            
            List<String> confusers = confuser.getConfusers(answer, alphabet, 3);
            
            Question q = new Question(quiz: quizInstance, question: question, questionType: Constants.CONFUSER).save(failOnError: true)
            
            new QuestionOption(question: q, option: answer, answerKey: 1).save(failOnError: true)
            
            confusers.each {
                new QuestionOption(question: q, option: it, answerKey: 0).save(failOnError: true)
            }
            
            
            
        }

        /**
        * Initialize the quiz: create an answer for every question in the quiz for the logged student
        * @return true on success - for now this cannot fail
        */
        Answer startQuiz(Quiz quizInstance, String sessionId) {

            // First check that the user hasn't started this course yet
            Integer answeredQuestions = Answer.executeQuery("""
                SELECT count(id) FROM Answer WHERE user.id = ? AND question.quiz.id = ?
                """, [springSecurityService.principal.id, quizInstance.id])[0]

            // This will continue the test where they left off. We have to check if this is allowed
            if(answeredQuestions > 0) {
                return nextQuestion(quizInstance, sessionId)
            }

            // This is the first time this user starts a test so create all the answers
            quizInstance.questions.each {
                new Answer(
                    user: User.load(springSecurityService.principal.id),
                    question: it,
                    selection: null, // not answered yet
                    status: Constants.NOT_ANSWERED,
                    sessionId: sessionId
                    ).save(flush: true)
            }

            return nextQuestion(quizInstance)
        }

        /**
        * Get the next question in the queue that has not been answered already
        */
        Answer nextQuestion(Quiz quizInstance, String sessionId=null) {
            // Find the next answer in the quiz that has not yet been answered
            Answer answer = Answer.createCriteria().list(max: 1) {
                user { 
                    eq('id', springSecurityService.principal.id) 
                }
                question { 
                    eq('quiz', quizInstance) 
                }
                eq('status', Constants.NOT_ANSWERED) 
             }[0]

            // Change the status of this answer to viewed
            if(answer && ((sessionId == null) || (answer.sessionId == sessionId))) {
                // Here it might be a good idea to change the session so that the user can continue the test
                answer.status = Constants.VIEWED
                answer.save()
            } 

            // Have to finalize the quiz since no more answers can be submitted by the student
            else {
                finalizeQuiz(quizInstance, true)
                return null
            }

            return answer
        }

        /**
        * Answer a question - it will only be answered if the status is VIEWED, the session is the same and the timing is right
        */
        Boolean answerQuestion(Answer answer, Long selection, String sessionId) {

            if(answer.question.quiz.maxCardTime != 0) {
                // If questions are timed, calculate how much time passed since the user viewed the answer and it actually answered it
                long l1 = answer.lastUpdated.getTime()
                long l2 = new Date().getTime();
                long diff = (l2 - l1)/1000;   
                // Oops, user took longer than expected!
                if(diff > answer.question.quiz.maxCardTime) {
                    answer.status = Constants.ANSWERED
                    answer.save()
                    return false
                }
            }
            
            // Check that the answer can actually be answered
            if(answer.status == Constants.VIEWED && answer.sessionId == sessionId) {
                answer.selection = QuestionOption.load(selection)
                answer.status = Constants.ANSWERED
                answer.save()
                return true
            }
            else {
                // The use is trying to answer the questions with a different session, shut the quiz down
                finalizeQuiz(answer.question.quiz, true)
            }

            return false
        }

        /**
        * Get answers by logged student
        */
        List<Answer> getAnswersByLoggedUser(Quiz quizInstance) {
            return getAnswers(quizInstance, springSecurityService.principal.id)
        }

        /**
        * Return all the answers by a given student
        */
        List<Answer> getAnswers(Quiz quizInstance, Long userId) {
            return Answer.withCriteria {
                user {
                    eq('id', userId)
                }
                question {
                    eq('quiz', quizInstance)
                }
            }
        }

        /**
        * Finalize the course and create a grade for the student, only if the student has viewed all the questions
        */
        Grade finalizeQuiz(Quiz quizInstance, boolean force=false) {
            // Check if the number of answers that a user viewed or answered is the same as the number of questions in the quiz
            Integer completedQuestions = Answer.executeQuery("""
                SELECT count(id) 
                FROM Answer 
                WHERE (status = ? OR status = ?) AND question.quiz.id = ? AND user.id = ?
                """,
                [Constants.VIEWED, Constants.ANSWERED, quizInstance.id, springSecurityService.principal.id])[0]

            // only finalize it if the if the user has completed all questions or if the quiz is forced to finalize
            if((quizInstance.countQuestions() == completedQuestions) || force)  {
            
                
            List<Answer> answers = getAnswers(quizInstance, springSecurityService.principal.id)
                
             Integer correctAnswers = 0;
             
             answers.each() {
                 if (it.selection.id == it.question.getCorrectOption().id) {
                     correctAnswers++;
                 }
             }

             return new Grade(user: User.load(springSecurityService.principal.id), correctAnswers: correctAnswers, quiz: quizInstance).save()
         }

            // The user has not completed the course yet
            return null
        }

        /**
        * Return the number of correct answers for a quiz for a given user
        */
        Grade getGrade(Quiz quizInstance, Long userId) {
          return Grade.findByUserAndQuiz(User.load(userId), quizInstance)
      }

        /**
        * Return the number of correct answers for a quiz for the logged user
        */
        Grade getGrade(Quiz quizInstance) {
            return getGrade(quizInstance, springSecurityService.principal.id)
        }

        /**
        * Return the percentage grade for a quiz for the logged user
        */
        String getPercentageGrade(Quiz quizInstance, Long userId) {

            Grade gradeInstance = getGrade(quizInstance, userId)

            // Check if the student has a grade for the quiz
            if(!gradeInstance) {
                return null
            }

            return "${Math.ceil(gradeInstance.correctAnswers/getAnswers(quizInstance, userId).size()*100)}"
        }

        /**
        * Return the percentage grade for a quiz for the logged user
        */
        String getPercentageGrade(Quiz quizInstance) {

            Grade gradeInstance = getGrade(quizInstance)

            // Check if the student has a grade for the quiz
            if(!gradeInstance) {
                return null
            }

            return "${gradeInstance.correctAnswers/getAnswersByLoggedUser(quizInstance).size()*100}"
        }

    	/**
    	 * Load a deck from a CSV - returns a list with any errors that might have happened during upload
    	 */
    	 List loadQuizFromCSV(String title, Date liveTime, Date endTime, Integer maxCardTime, Course courseInstance, def f) {
           log.info "In Quiz Service Service"
    		 
    		 List result
             result = []
    		 
    		 if(f.fileItem){
    			 // Create a temporary file with the uploaded contents
    			 def extension = f.fileItem.name.lastIndexOf('.').with {it != -1 ? f.fileItem.name.substring(it + 1) : f.fileItem.name}
                 log.info "The extension is: ${extension}"

                 Class cls = f.getClass()
                 log.info f.getClass()
                 log.info  "Class Name ${f.getName()}"
    			 def outputFile = new File("${new Date().time}.${extension}")
    			 f.transferTo(outputFile)
                  log.info "extension ${extension}"
                  if (extension != "zip" && extension != "csv"){
                    return result << "File needs to be a .zip file or .csv file.  No other types supported."

                 } 
                 if (extension == "zip"){
                  // unZipIt(outputFile, "/cs599")
                   //unzip(outputFile, "/cs599")
                  List zList = getJarFiles(outputFile)
                  log.info zList
                   zList.each {
                   log.info "File or Folder: $it" 
                 }
                 return

                 }
     
    			 // Validate the file first
    			//result = validateCSV(outputFile.path)
    			// if(!result.isEmpty()) {
    			//	 return result
    			// }
    			 
                 
                 Quiz quizInstance = new Quiz(
                         course: courseInstance, //courseId,
                         title: title,
                         testElement: Constants.MANUAL,
                         enabled: true,
                         liveTime: liveTime,
                         endTime: endTime,
                         maxCardTime: maxCardTime
                         ).save(failOnError: true)

    			 // Everything looks ok, lets save
    			// new File(outputFile.path).toCsvReader(['skipLines':1, 'charset':'UTF-8']).eachLine { tokens ->
                    // FILE theFile = new File(this.tmpFolder + File.separator + ze.getName());

                   // log.info "outputfile path: ${outputfile.path}"
                    FileInputStream fis = new FileInputStream(outputFile.path); 

                    fis.toCsvReader(['charset':'UTF-8','skipLines':1],).eachLine { tokens -> 

                     if (tokens[3].equals("")){
                        return
                     }
    				 log.info "this should be printing Japanese characters ${tokens[3]}"	 
    				 Question question = new Question(quiz: quizInstance, question: tokens[3], questionType: Constants.MANUAL).save(failOnError: true)
    				 new QuestionOption(question: question, option: tokens[4].substring(1, tokens[4].length()-1), answerKey: 1).save(failOnError: true)

                    log.info "token 5 ${tokens[5]}"
                    String[] wrongAnswers = tokens[5].substring(1, tokens[5].length()-1).split(",[ ]*");
                    log.info "the OTOKEN length: ${wrongAnswers.length}"
                    for (int i = 0; i < wrongAnswers.length; i++) {
                    new QuestionOption(question: question, option: wrongAnswers[i], answerKey: 0).save(failOnError: true)
                     }

    			 }
     
    			 // Cleanup
    			 outputFile.delete()
    		 }
    		 else {
    			 result << "File not found"
    		 }
     
    		 return result
    	 }

             /**
        * Check that each row has a unit, a meaning and a pronunciation
        * Returns a list with any errors
        */
        List validateCSV(String filePath) {
            List result = []
            int i = 0
            new File(filePath).toCsvReader(['skipLines':1]).eachLine { tokens ->

                // Check that there's a meaning a pronunciation and a symbol
                if(!tokens[3]) {
                    result << "Row ${i} is missing a question"
                }

                if(!tokens[4]) {
                    result << "Row ${i} is missing the answer"   
                }
                
                if(!tokens[5]) {
                    result << "Row ${i} is missing an option"
                }

                i++
            }

            return result
        }


        /**
         * Extracts a zip file specified by the zipFilePath to a directory specified by
         * destDirectory (will be created if does not exists)
         * @param zipFilePath
         * @param destDirectory
         * @throws IOException
         */
        public void unzip(File zipFilePath, String destDirectory) throws IOException {
            File destDir = new File(destDirectory);
            if (!destDir.exists()) {
                destDir.mkdir();
            }
            ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
            ZipEntry entry = zipIn.getNextEntry();
            // iterates over entries in the zip file
           
            while (entry != null) {
                String filePath = destDirectory + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                File parent = new File(filePath).getParentFile();
                if(!parent.exists()) {
                parent.mkdirs();
                }
                extractFile(zipIn, filePath);
                } else {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                dir.mkdir();
                }
               
                entry = zipIn.getNextEntry();
                }
             zipIn.closeEntry();



            zipIn.close();
        }
        /**
         * Extracts a zip entry (file entry)
         * @param zipIn
         * @param filePath
         * @throws IOException
         */
        private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
            byte[] bytesIn = new byte[4096];
            int read = 0;
            while ((read = zipIn.read(bytesIn)) != -1) {
                bos.write(bytesIn, 0, read);
            }
            bos.close();
        }
    

public static List<String> getJarFiles(File jarFile) throws IOException {
  List<String> jarFiles=new ArrayList<String>();
  FileInputStream fileInputStream=new FileInputStream(jarFile);
  ZipInputStream zipInputStream=new ZipInputStream(fileInputStream);
  ZipEntry zipEntry;
  while ((zipEntry=zipInputStream.getNextEntry()) != null) {
    jarFiles.add(zipEntry.getName());
  }
  zipInputStream.close();
  fileInputStream.close();
  return jarFiles;
}
}



