<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"/>
	<title>OpenFluency</title>
</head>
<body>
	<div class="container flashcard-create">

		<ul class="breadcrumb">
        <li>Home</li>
		</ul>

		<h1>Create Flashcard</h1>

<div class="row marketing">
        <div class="col-lg-6">
           <g:render template="/dictionary/dictionaryTable"/>
        </div> 
        <div class="col-lg-6">
        <form>
  <div class="form-group row">
    <label for="inputConcept" class="col-sm-2 form-control-label">Concept</label>
    <div class="col-sm-10">
      <input type="text" class="form-control" id="inputConcept" placeholder="concept">
    </div>
  </div>
  <div class="form-group row">
    <label for="inputMeaning" class="col-sm-2 form-control-label">Meaning</label>
    <div class="col-sm-10">
      <input type="text" class="form-control" id="inputMeaning" placeholder="meaning">
    </div>
  </div>
  <div class="form-group row">
    <label for="inputPronunciation" class="col-sm-2 form-control-label">Pronunciation</label>
    <div class="col-sm-10">
      <input type="text" class="form-control" id="inputPronunciation" placeholder="pronunciation">
    </div>
  </div>

  <div class="form-group row">
    <label for="select" class="col-sm-2 form-control-label">Deck Select</label>
    <div class="col-sm-10">
      <select id="select">
        <option value="">Choose...</option>
        <optgroup label="Option group 1">
          <option value="">Option 1</option>
          <option value="">Option 2</option>
          <option value="">Option 3</option>
        </optgroup>
      </select>
    </div>
  </div>


  <div class="form-group row">
    <div class="col-sm-offset-2 col-sm-10">
      <button type="submit" class="btn btn-secondary">Submit</button>
    </div>
  </div>
</form>
</div> 
</div>
<g:javascript>
$(document).ajaxSuccess(function() {
$("tr[id^='dictionary-row-']").each(function(){
    console.log("Hellol");
    console.log($(this).attr('id'));
    var elem_id = $(this).attr('id');
    $("#" + elem_id).click(function() {
        var dom_id = "#" + elem_id;
        $("#inputConcept").attr("value", $(dom_id).data('concept'));
        $("#inputMeaning").attr("value", $(dom_id).data('meaning'));
        $("#inputPronunciation").attr("value",$(dom_id).data('pronunciation'));
    });
});
});
</g:javascript>
</body>
</html>

