<div  id="unit-mapping-div" class="row" style="margin-left: auto; margin-right: auto;">

	<div id="units-div">
	  	<div id="units-list-container" class=""> 
	  		<h4>Units</h4>
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
</div>


<g:javascript>
var cardIndex = 0;
var previewCardData = JSON.parse('${previewCardInstanceList}');

loadUnitMappingPreviewCard(previewCardData, 0);
initializeUnitMappingDraggable();

function loadUnitMappingPreviewCard(previewCardData, cardIndex) {
	for (var i = 0; i < previewCardData[cardIndex].units.length; i++) {
		$('#unit-ul').append('<li class="orange unit-li"><h6><span style="background: #ccffcc;">' + previewCardData[cardIndex].fields[i] + '</span></h6><div class="draggable draggable-unit" data-index="' + i + '">' + previewCardData[cardIndex].units[i] + '</div></li>');
	}
}
</g:javascript>