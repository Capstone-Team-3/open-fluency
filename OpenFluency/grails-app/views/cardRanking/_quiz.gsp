    <g:if test="${quizzes}">
      <h4>Quiz Results</h4>
    </g:if>
    <div class="quiztable">
        <g:each in="${quizzes}">
            <g:if test="${it.correctAnswers == it.numberOfQuestions}">
            <span class="grade-star"> &nbsp;&nbsp; </span>
            </g:if>
            <g:link controller="quiz" action="report" id="${it.id}">
            ${it.quiz?.title}
            </g:link>
            :${it.correctAnswers}/${it.numberOfQuestions}, &nbsp;
        </g:each>
    </div>
