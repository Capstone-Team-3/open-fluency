<div  id="unit-mapping-div" class="row" style="margin-left: auto; margin-right: auto;">
	<button id="submit-unitMapping-button" class="btn btn-success btn-sm" style="float:right">Submit</button>

	<div id="units-div">
	  	<div id="units-list-container" class=""> 
	  		<h4>Units <button id="previous-unit" class="btn btn-sm btn-primary" style="margin-right:5px;">previous</button><button id="next-unit" class="btn btn-sm btn-primary">next</button></h4>
	  		<ul id="unit-ul"> 
			  	<g:each status="i" var="item" in="${test}">
			  		<li class="orange unit-li"><div class='draggable draggable-unit' data-index="${i}">${item}</div></li>
			  	</g:each>
		  	</ul>
		</div>
	</div>
	 
	 
	<div id="unit-mapping-card-container">
		<h4>Drag and drop to card</h4>
		<div class="flashcard animated slideInRight"> 
			<div class="panel panel-default">
				<div class="panel-body">
					<div class="flashcard-header">
					
						<span id="clear-literal" class="close-thin"></span>
						<div style="min-height: 190px;">
							<span class="label-literal"></span>
							<h1 id="flashcard-literal" class="flashcard-unit droppable"></h1>
						</div>
						
						<div class="pronunciation">
							<span id="clear-pronunciation" class="close-thin"></span>
							<div id="pronunciation-droppable">
								<span id="pronounced">(pronunciation text)</span>
								<span class="play-audio glyphicon glyphicon-volume-up" style="visibility: hidden;"></span>
								<audio class="flashcard-audio hidden" id="flashcard-audio-4" src="/OpenFluency/audio/sourceAudio/4" controls></audio>
							</div>
						</div>
						<div id="audio-url-droppable">
							<span id="clear-audio-url" class="close-thin"></span>
							<span id="audio-url-display">(audio url)</span>
						</div>
						</div>
		
						<div id="image-container">
							<span id="clear-image" class="close-thin"></span>
							<div id="flashcard-image" class="flashcard-img" style="font: 14px/100% arial, sans-serif;text-shadow: 1px 1px 0 #fff;">(Image Url)</div>
						</div>
		
					<div id="meaning-droppable">
						<span id="clear-meaning" class="close-thin"></span>
						<span id="meaning-display">(meaning text)</span>
					</div>
				</div>
				</div>
			</div> 
		</div>
		
		
	 
	<br style="clear:both">
	
	<!-- Modal -->
	  <div class="modal fade" id="literal-options" role="dialog">
	    <div class="modal-dialog">
	    
	      <!-- Modal content-->
	      <div class="modal-content">
	        <div class="modal-header">
	          <h4 class="modal-title">Select Alphabet Type</h4>
	        </div>
	        <div class="modal-body">
	        	<div class="ul-container">
		        	<ul>
		        		<li><button data-alph="English" class="btn btn-sm btn-info l-alpha-options">English</button></li>
		        		<li><button data-alph="Japanese" class="btn btn-sm btn-info l-alpha-options">Japanese</button></li>
		        		<li><button data-alph="Katakana" class="btn btn-sm btn-info l-alpha-options">Katakana</button></li>
		        		<li><button data-alph="Hiragana" class="btn btn-sm btn-info l-alpha-options">Hiragana</button></li>
		        		<li><button data-alph="Romaji" class="btn btn-sm btn-info p-alpha-options">Romaji</button></li>
		        	</ul>
	        	</div>
	        </div>
	        <div class="modal-footer">
	        </div>
	      </div>
	      
	    </div>
	  </div>
	  
	<!-- Modal -->
	  <div class="modal fade" id="pronunciation-options" role="dialog">
	    <div class="modal-dialog">
	    
	      <!-- Modal content-->
	      <div class="modal-content">
	        <div class="modal-header">
	          <h4 class="modal-title">Select Alphabet Type</h4>
	        </div>
	        <div class="modal-body">
	        	<div class="ul-container">
		        	<ul>
		        		<li><button data-alph="English" class="btn btn-sm btn-info p-alpha-options">English</button></li>
		        		<li><button data-alph="Japanese" class="btn btn-sm btn-info p-alpha-options">Japanese</button></li>
		        		<li><button data-alph="Katakana" class="btn btn-sm btn-info p-alpha-options">Katakana</button></li>
		        		<li><button data-alph="Hiragana" class="btn btn-sm btn-info p-alpha-options">Hiragana</button></li>
		        		<li><button data-alph="Romaji" class="btn btn-sm btn-info p-alpha-options">Romaji</button></li>
		        	</ul>
	        	</div>
	        </div>
	        <div class="modal-footer">
	        </div>
	      </div>
	      
	    </div>
	  </div>
	  
	  
	
</div>


<g:javascript>
window.unitMappingLiteral = null;
window.unitMappingPronunciation = null;
window.unitMappingAudioUrl = null;
window.unitMappingBackgroundImage = null;
window.unitMappingMeaning = null;

var cardIndex = 0;
var previewCardData = JSON.parse('${previewCardInstanceList}');

loadUnitMappingPreviewCard(previewCardData, 0);


function loadUnitMappingPreviewCard(previewCardData, cardIndex) {
	$('#unit-ul').html("");
	for (var i = 0; i < previewCardData[cardIndex].units.length; i++) {
		$('#unit-ul').append('<li class="orange unit-li"><h6><span style="background: #ccffcc;">' + previewCardData[cardIndex].fields[i] + '</span>-<span style="background: #66ffff;">' + previewCardData[cardIndex].types[i] + '</span></h6><div class="draggable draggable-unit" data-index="' + i + '">' + previewCardData[cardIndex].units[i] + '</div></li>');
	}
	if (unitMappingLiteral != null) $('#flashcard-literal').html(previewCardData[cardIndex].units[unitMappingLiteral]);
	if (unitMappingPronunciation != null) $('#pronounced').html(previewCardData[cardIndex].units[unitMappingPronunciation]);
	if (unitMappingAudioUrl != null) $('#audio-url-display').html(previewCardData[cardIndex].units[unitMappingAudioUrl]);
	if (unitMappingBackgroundImage != null) $('#flashcard-image').css("background-image", "url(" + units[unitMappingBackgroundImage] + ")");
	if (unitMappingMeaning != null) $('#meaning-display').html(previewCardData[cardIndex].units[unitMappingMeaning]);
	
	initializeUnitMappingDraggable();
}

$('#submit-unitMapping-button').click(function() {
	test123();
});

$('#previous-unit').click(function(){
	if (cardIndex > 0) {
		loadUnitMappingPreviewCard(previewCardData, --cardIndex)
	}
});

$('#next-unit').click(function(){
	if (cardIndex < previewCardData.length - 1) {
		loadUnitMappingPreviewCard(previewCardData, ++cardIndex)
	}
});

</g:javascript>

<script>
var alphab = {};

$('.l-alpha-options').click(function() {
	if (unitMappingLiteral == null) return;
	alphab['' + unitMappingLiteral] = this.dataset.alph;
	$('#literal-options').modal('hide');
});

$('.p-alpha-options').click(function() {
	if (unitMappingPronunciation == null) return;
	alphab['' + unitMappingPronunciation] = this.dataset.alph;
	$('#pronunciation-options').modal('hide');
});

function test123() {
	console.log("posting unit mapping to server");
	var alphaIndices = {};
	var fieldIndices = {};
	
	if (unitMappingLiteral != null) fieldIndices['Literal'] = unitMappingLiteral;
	if (unitMappingPronunciation != null) fieldIndices['Pronunciation'] = unitMappingPronunciation;
	if (unitMappingAudioUrl != null) fieldIndices['Sound'] = unitMappingAudioUrl;
	
	if (unitMappingBackgroundImage != null) {
		// ensure is of type image
		if (previewCardData[cardIndex].types[unitMappingBackgroundImage] == "Image") {
			fieldIndices['Image'] = unitMappingBackgroundImage;
		}		
	}
	
	if (unitMappingMeaning != null) fieldIndices['Meaning'] = unitMappingMeaning;

	var dat = { 
		fieldIndices: fieldIndices,
		alphaIndices: alphab
	};
	
	var obj = JSON.stringify(dat);
	
	$.ajax({
		type: "POST",
		url: "/OpenFluency/previewDeck/unitMappingSubmit",
		data: {payload: obj},
		success: function(output) {
			console.log(output);
		},
		error: function(err) {
			console.log('err', err);
		}
	});
}

</script>