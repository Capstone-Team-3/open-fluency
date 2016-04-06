<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main"/>
	
	
</head>
<body>
<%@ page import="com.openfluency.media.Audio" %>



<style>

	#units-div { 
		width: 315px; 
		float: left
	}
	
	#units-list-container li { 
		padding: 5px; 
		font-size: 1.5em; 
		cursor:pointer;
	}
	
	#units-list-container li:hover { 
		background-color:#e6e6ff; 
		border-right: 5px solid #F5876E;
	}
	
	#units-list-container li:hover { 
		background-color: #ccccff; 
	}

  	.hidden{
    	display: none;
	}
	
	#unit-mapping-div {
		width: 1250px;
		margin-left: auto;
		margin-right: auto;
	}

	.unit-mapping-selection {
    	cursor: pointer;
	}  

	.orange { border-left: 5px solid #F5876E; }

	.item-mapping-span {
		font-size: 12px;
	}
	
	#unit-mapping-card-container {
		width:320px;
		margin-left: 450px;
	}
		
	#pronunciation-droppable {
	    min-height: 30px;
	    min-width: 50%;
	    border: 1px dashed;
	}

	#audio-url-droppable {
	    min-height: 30px;
	    min-width: 50%;
	    border-left: 1px dashed;
	    border-right: 1px dashed;
	    border-bottom: 1px dashed;
	    overflow: hidden;
	    white-space: nowrap;
	}
	
	.draggable-unit {
		word-wrap: break-word;
		width: 315px;
		z-index: 10;
		font: 24px/100% arial, sans-serif;
	}
	
	/* Close Button */
	[class*='close-'] {
	  color: #777;
	  font: 14px/100% arial, sans-serif;
	  position: relative;
	  right: 5px;
	  text-decoration: none;
	  text-shadow: 0 1px 0 #fff;
	  top: 5px;
	  float: right;
	  cursor: pointer;
	}

	.close-thin:after {
	  content: '×'; /* UTF-8 symbol */
	}
		
	[class*='label-'] {
	  color: black;
	  font: 14px/100% arial, sans-serif;
	  position: relative;
	  text-decoration: none;
	  top: -5px;
	  cursor: pointer;
	}
	
	.label-literal:after {
	  content: '(literal)'; /* UTF-8 symbol */
	}	
	
	#unit-ul {
		list-style-type: none;
		padding: 0px; 
	}
	
	.unit-li {
		margin-bottom: 5px;
	}
	
	#flashcard-literal {
		min-height: 162px;
	}
		
		
	#audio-url-display {
		font: 14px/100% arial, sans-serif;
	}	
	
	#meaning-droppable {
		min-height: 50px
	}	
		
	.unit-dragging {
		border: 5px solid #61A8DC;
		text-align: center;
	}	
		
	#unit-mapping-card-container {
		width: 320px;
		margin-left: 450px;
	}	
	
	
	
  </style>
	
  <link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">

<div  id="unit-mapping-div" class="row">

<div id="units-div">
  	<div id="units-list-container" class=""> 
  		<h4>Units</h4>
  		<ul id="unit-ul"> 
		  	<g:each status="i" var="item" in="${test}">
		  		<li class="orange unit-li"><div class='draggable draggable-unit'>${item}</div></li>
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
 
 
 
 



 
 
 
 
 
  <script src="//code.jquery.com/jquery-1.10.2.js"></script>
  <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>	
 
 
 
 
 
<script>


$(".play-audio").click(function() {
	var audioSrcID = $(this).next(".flashcard-audio").attr("id");
	$('#' + audioSrcID).load().get(0).play();
});


function initializeUnitMappingDraggable() {
	window.unitMappingLiteral = null;
	window.unitMappingPronunciation = null;
	window.unitMappingAudioUrl = null;
	window.unitMappingBackgroundImage = null;
	window.unitMappingMeaning = null;
	
    $( ".draggable" ).draggable({
        helper: 'clone',
		cursor: "move",
		start: function(e, ui) {
			$(ui.helper).addClass('unit-dragging');
		},
		stop: function(e, ui) {
			$(ui.helper).removeClass('unit-dragging');
		}
     });
    
    $( "#flashcard-literal" ).droppable({
      drop: function( event, ui ) {
        $(this).html($(ui.draggable).html());
        unitMappingLiteral = $(ui.draggable).html();
      }
  	});

    $( "#pronunciation-droppable" ).droppable({
        drop: function( event, ui ) {
            console.log('here');
          $('#pronounced').html("pronounced " + $(ui.draggable).html());
          unitMappingPronunciation = $(ui.draggable).html();
        }
     });

    $('#audio-url-droppable').droppable({
		drop: function(event, ui) {
			$('#audio-url-display').html($(ui.draggable).html());
			$('.play-audio').css('visibility', 'visible');
			unitMappingAudioUrl = $(ui.draggable).html();
		}
    });

    $("#flashcard-image").droppable({
		drop: function(event, ui) {
			$('#flashcard-image').css("background-image", "url(" + $(ui.draggable).html() + ")");
			unitMappingBackgroundImage = $(ui.draggable).html();
		}
    });

    $("#meaning-droppable").droppable({
		drop: function(event, ui) {
			$("#meaning-display").html($(ui.draggable).html());
			unitMappingMeaning = $(ui.draggable).html();
		}
    });

    $('#clear-literal').click(function() {
    	$('#flashcard-literal').html("");
    	unitMappingLiteral = null;
    });

    $('#clear-pronunciation').click(function(){
    	$('#pronounced').html('pronunciation text');
    	unitMappingPronunciation = null;
    });

    $('#clear-audio-url').click(function(){
    	$('#audio-url-display').html("(audio url)");
    	unitMappingAudioUrl = null;
    });

    $('#clear-image').click(function(){
    	$('#flashcard-image').css('background-image', '');
    	unitMappingBackgroundImage = null;
    });

    $('#clear-meaning').click(function(){
    	$('#meaning-display').html("(meaning text)");
    	unitMappingMeaning = null;
    });
    
  }





initializeUnitMappingDraggable();


  </script>
	
	
	
</body>
</html>