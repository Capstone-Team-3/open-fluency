
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="layout" content="main"/>
</head>
<body>
    <div class="container unit-index">
        <h1>Flashcard Search</h1>
        <p class="instructions">Choose a character, word, or phrase for which you'd like to create a flashcard.</p>

        <div class="row">
            
            <div class="col-lg-4">
                <select id="filter-lang" class="form-control" name="filter-lang">
                    <g:each in="${alphabetInstanceList}">
                        <option value="${it.id}">
                            ${it.language} - ${it.name}
                        </option>
                    </g:each>
                </select>
            </div>
            
           <div class="col-lg-4">
                <div class="input-group">
                    <g:textField class="form-control" name="search-text" placeholder="Type a keyword" id="search-text" />
                    <span class="input-group-btn">
                        <button class="btn btn-default" type="button"><span class="glyphicon glyphicon-search"></span></button>
                    </span>
                </div><!-- end input-group -->
            </div><!-- end col-lg-4 -->

        </div><!-- end row -->

        <table class="table">
            <thead>
                <tr>
                    <th>Character, Word or Phrase</th>
                    <th>Meaning(s)</th>
                    <th>Stroke Count</th>
                    <th>Grade Level</th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                <g:each in="${unitInstanceList}">
                    <tr>
                        <td>${it?.print}</td>
                        <td>
                            <g:each var="meaning" in="${it?.getMappings()}">
                                <span class="meaning">${meaning?.print}</span>
                            </g:each>
                        </td>
                        <td>${it?.strokeCount}</td>
                        <td>${it?.grade}</td>
                        <td>
                            <g:form action="create" controller="flashcard" name="createFlashcardForm">
                                <input type="hidden" name="unit" value="${it?.id}"/>
                                <button class="btn btn-success" type="submit">Create Flashcard</button>
                            </g:form>
                        </td>
                    </tr>
                </g:each>
            </tbody>
        </table>
    </div><!-- end container -->
</body>
</html>