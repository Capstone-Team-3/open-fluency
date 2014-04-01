<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"/>
</head>
<body>
	<div class="container course-search">

		<h1>Course Search</h1>

        <div class="row">

            <div class="col-lg-4">
                <select id="filter-lang" class="form-control" name="filter-lang">
                    <g:each in="${languageInstanceList}">
                        <option value="${it.id}">
                            ${it.name}
                        </option>
                    </g:each>
                </select>
            </div><!-- end col-lg-4 -->
            
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
			<tr>
				<th>Title</th>
				<th>Description</th>
				<th>Instructor</th>
				<th></th>
			</tr>
			<g:each in="${courseInstanceList}">
				<tr>
					<td>
						<g:link action="show" controller="course" id="${it.id}">${it.title}</g:link>
					</td>
					<td>
						${it.description}
					</td>
					<td>A. Name</td>
					<td>
						<g:link action="enroll" controller="course" id="${it.id}" class="btn btn-info">Enroll</g:link>
					</td>
				</tr>
			</g:each>
		</table>

	</div><!-- end container -->
</body>
</html>