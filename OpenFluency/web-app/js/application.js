/*----------------------------------------------------------------------------*/
/* Tooltips
/*----------------------------------------------------------------------------*/

/**
 * Initialize all tooltips with the class .tooltiper. Uses the tooltip.js
 * plugin included with Boostrap.
 */
var initializeStyledElements = function() {
	$('.tooltiper').tooltip();
};

/*----------------------------------------------------------------------------*/
/* Sign Up - Add Language Proficiencies
/*----------------------------------------------------------------------------*/

/**
 * Add a language proficiency selection box to the application Sign Up form.
 */
var initializeProficiencyAdder = function() {
	$('#addproficiency').click(function(){
		$.ajax({
			url: "../language/addLanguageProficiency",
			context: document.body
		}).done(function(data) {
			$('.proficiencies').append(data);
			initializeProficiencyRemovers();
		});
	});
};

/**
 * Remove a language proficiency selection box form the application 
 * Sign Up form.
 */
var initializeProficiencyRemovers = function() {
	$('.remove-proficiency').unbind('click').click(function(){
		$(this).parents('.panel').remove();
		return false;
	});
};

/*----------------------------------------------------------------------------*/
/* Flashcard Search
/*----------------------------------------------------------------------------*/

/**
 * Add pagination to the flashcard search page.
 */
var setupFlashcardSearchPagination = function() {
	$('.paged-search a').click(function() {
		$("#offset").val((parseInt($(this).text())-1)*10);
		$('.searchUnitForm').submit();
		return false;
	});
};

/*----------------------------------------------------------------------------*/
/* Association Images and Audio
/*----------------------------------------------------------------------------*/

/**
 * Search for an image via Flickr
 * @param query: the search query to run
 * @param resultsId: the selector of the div where the results will be appended 
 * (which is cleared before appending)
 * @param urlField: the selector for the field where the url of the image should 
 * be saved on click
 */
var searchImage = function(query, results, urlField, resultPage) {
	
	// set helpful and need variables
	var apiKey = "ec50db25dd7a2b1d0c5d7b3ec404cce6";
	var sMethod = "flickr.photos.search";
	var respFormat = "&format=json&jsoncallback=?";
	var numPics = "6";
	var src;

	// Build base Flickr query url
	var baseUrl = "https://api.flickr.com/services/rest/?api_key=" + apiKey +
				  "&method=" + sMethod + "&sort=relevance&per_page=" + numPics +
				  "&page=" + resultPage + "&text=";

	// Remove previous pics
	$(results).empty();

	// Build full URL
	var queryStr = baseUrl + $(query).val() + respFormat;

	// Run query
	$.getJSON(queryStr, function(data){

		// Add responses to target
		$.each(data.photos.photo, function(i,item){
			src = "http://farm" + item.farm + ".static.flickr.com/" +
				   item.server + "/" + item.id + "_" + item.secret + "_m.jpg";

			// Append results to result list
			$("<div/>").css("background-image", "url(" + src + ")")
				.data('imageLink', src).attr("class", "img-rounded img-result")
				.appendTo(results)
				.click(function(){
					// when the image is clicked, set the url and also the imageLink in the input field
					$(urlField).val($(this).data('imageLink'));
					$('.flashcard-image-create').css('background-image', 'url(' + $(this).data('imageLink') + ')');
				});

			if (i >= numPics) {
				return false;
			}
		});
	});
};


/**
 * Play audio pronunciation file when .play-audio button clicked.
 */
var initializeAudio = function() {
	$(".play-audio").click(function() {
		var audioSrcID = $(this).next(".flashcard-audio").attr("id");
		$('#' + audioSrcID).load().get(0).play();
	});
	

	$('.play-unit-audio').click(function() {
	    var resource = this.dataset.audio;
	    console.log(resource);
	    var audio = new Audio(resource);
	    audio.play();
	});
};

/*----------------------------------------------------------------------------*/
/* Practice Flashcards
/*----------------------------------------------------------------------------*/

/**
 * Hide the relevant fields on practice flashcards and initializes flashcard
 * difficulty rankings.
 */
var initializePracticeCards = function() {
	var type = $('#practice-container').data("rank-type").toLowerCase();
	var pArray = $('.pronunciation');
	var uArray = $('.flashcard-unit');
	
	// ensure that pronunciation is hidden if it equals unit literal
	for (var i = 0; i < uArray.length; i++) {
	    if (uArray[i].dataset.unit.replace(/^\s+|\s+$/g,'') == pArray[i].dataset.pronunciation.replace(/^\s+|\s+$/g,'')) {
	        $(pArray[i]).hide();
	    }
	}

	if (type === 'meaning' || type === 'pronunciation'){
		hideElement("Meaning", ".meaning", "show-meaning");
		hideElement("Image", "image-container", "show-image");
		hideElement("Pronunciation", ".pronunciation", "show-pronunciation");
	} else if (type === 'symbol'){
		hideElement("Word/Character", ".flashcard-unit", "show-flashcard-unit");
		hideElement("Pronunciation", ".pronunciation", "show-pronunciation");
	}
		
	initializePracticeRanking();
};

/**
* Hide an element and replace it with a button with a given title and an id. 
* When the button is clicked the element is displayed
* @param title: the name of the flashcard practice mode.
* @param selector: flashcard container to show/hide during practce.
* @param id: id to assign the 'show' button.
*/
var hideElement = function(title, selector, id) {
	var $elementContainer = $(selector);
	var elementHTML = $elementContainer.html();
	
	$elementContainer.html('<button class="btn" id="' + id + '">Show ' +
		title + '</button>');
	$('#' + id).on('click', function() {
		$elementContainer.html(elementHTML);
		if (title === "Pronunciation"){
			initializeAudio();
		}
	});
};

/**
 * Submit the flashcard difficulty ranking selected by the user and slide the 
 * current flashcard off the screen.
 */
var initializePracticeRanking = function() {
	$(".ranker").click(function() {
        // Do some fancy UI transitions
        $(".flashcard").removeClass("slideInDown")
        		.addClass("slideOutLeft").delay(1000);

        // Set the ranking value in the form and submit it
        $("#ranking").val($(this).data('value'));
        $('.rankCardForm').submit();
    });
};


/*----------------------------------------------------------------------------*/
/* Practice Progress Donuts
/*----------------------------------------------------------------------------*/

/** 
 * Initialize the flashcard progress donuts.
 */
var initializeDonuts = function() {
	$('.progress-donut').each(function() {
		drawDonut($(this).data('progress'), "#" + $(this).attr('id'));
	});
};

/**
 * Draw a flashcard progress donut.
 * @param value: percentage of deck practice complete.
 * @param selector: specifies the container in which the donut should be drawn.
 */
var drawDonut = function(value, selector) {
	var	percentages = [value, (100 - value)];
	
	//var width = $(selector).width();
	var height = $(selector).height();
	var width = height;
	var radius = radius = Math.min(width, height) / 2;
	
	//var height = width, radius = Math.min(width, height) / 2;

	var color = d3.scale.category20();

	var pie = d3.layout.pie()
	.sort(null);

	var arc = d3.svg.arc().innerRadius(radius - 15).outerRadius(radius);

	var svg = d3.select(selector).append("svg")
	.attr("width", width)
	.attr("height", height)
	.append("g")
	.attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");

	svg.append("text")
	.text(Math.ceil(value) + "%")
	.style("text-anchor", "middle")
	.attr("transform", "translate(" + 0 + "," + 4 + ")");

	var path = svg.selectAll("path")
	.data(pie(percentages))
	.enter().append("path")
	.attr("fill", function(d, i) { return color(i); })
	.attr("d", arc);
};

/*----------------------------------------------------------------------------*/
/* Quizzes
/*----------------------------------------------------------------------------*/

/**
 * Initialize the form used to create quizzes.
 */
var initializeQuizCreator = function() {
	$('.chapter-selector').change(function() {
		if($(this).prop('checked')) {
			that = this;
			$.ajax({
				url: "/OpenFluency/chapter/flashcardSelect/" +
					$(this).data('chapter')
			})
			.done(function(html) {
				$("#include-chapters").append(html);
				$("input[type='checkbox']",
					"#include-chapters").prop("checked", true);
			});
		} else {
			var element = $("#flashcard-select-" + $(this).data('chapter'));
			element.slideUp("slow", function() {
				element.remove();
			});
		}
	});
};


/**
 * Initialize the timer displayed on timed quizzes.
 */
var initCountdown = function() {
	var time = $('#maxCardTime').val();
	if (time !== 0){
		var minutes = Math.floor(time/60);
		var seconds = time - minutes*60;
		countdown('clock', minutes, seconds);
	}
};

/**
 * Countdown the time remaining on timed quizzes.
 * @param element: the ID of the container in which the countdown is displayed.
 * @param minutes: minutes remaining in countdown.
 * @param seconds: seconds remaining in countdown.
 */
function countdown(element, minutes, seconds) {
	var time = minutes*60 + seconds;
	
	var interval = setInterval(function() {
		var el = $('#' + element);
		if (time === 0){
			$('.seconds').html('00');
			$('.seconds, .minutes').css('color', 'red');
			clearInterval(interval);
			$("input:radio").attr("disabled", true);
			return;
		}
		var minutes = Math.floor( time / 60 );
		if (minutes < 10) minutes = "0" + minutes;
		var seconds = time % 60;
		if (seconds < 10){
			seconds = "0" + seconds;
		}
		var html = '<span class="minutes bg-info">' + minutes + '</span> : ' +
				   '<span class="seconds bg-info">' + seconds + '</span>';
		el.html(html);
		time--;
	}, 1000);
}



/* --------------------------------------------------------------------*/
//						OpenFluency2
/* --------------------------------------------------------------------*/

/**
 * toggles dictionary modal
 */
$('.show-dictionary-button').click(function() {
	$('#dictionary-table').show();
	$('.show-dictionary-button').hide();
});


/**
 * Sends ajax request to dictionary service
 */
$('#dictionary-search-button').click(function() {
	var searchTerm = $('#dictionary-search-textbox').val();

	$.ajax({
		type: "GET",
		url: "/OpenFluency/dictionary/search",
		data: {
			term: searchTerm,
			count: 15
		},
		dataType: "html",
		success: function(output) {			
			$("#dictionary-results-table").html(output);
			$("#dictionary-results-table").css('visibility', 'visible');
		},
		error: function(err) {
			console.log(err);
		}
	});
	console.log(searchTerm);
});



/*----------------------------------------------------------------------------*/
/* Unit Mapping
/*----------------------------------------------------------------------------*/

window.unitMappingLiteral = null;
window.unitMappingPronunciation = null;
window.unitMappingAudioUrl = null;
window.unitMappingBackgroundImage = null;
window.unitMappingMeaning = null;


var initializeUnitMappingDraggable = function() {
	console.log('initializing draggable');
	

	
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
    	hoverClass: "dashed-border-black",
    	drop: function( event, ui ) {
	        $(this).html($(ui.draggable).html());
	        unitMappingLiteral = $(ui.draggable).data("index");
	        $('#literal-options').modal();
    	}
  	});
    
    $( "#pronunciation-droppable" ).droppable({
    	hoverClass: "light-green-hover-background",
        drop: function( event, ui ) {
        	$('#pronounced').html("pronounced " + $(ui.draggable).html());
        	unitMappingPronunciation = $(ui.draggable).data("index");
        	$('#pronunciation-options').modal();
        }
     });

    $('#audio-url-droppable').droppable({
    	hoverClass: "light-green-hover-background",
		drop: function(event, ui) {
			$('#audio-url-display').html($(ui.draggable).data('transfer'));
			$('.play-audio').css('visibility', 'visible');
			$('#um-flashcard-audio').attr('src', $(ui.draggable).data('transfer'));
			unitMappingAudioUrl = $(ui.draggable).data("index");
		}
    });

    $("#flashcard-image").droppable({
    	hoverClass: "dashed-border-black",
		drop: function(event, ui) {
			$('#flashcard-image').css("background-image", "url(" + $(ui.draggable).data('transfer') + ")");
			unitMappingBackgroundImage = $(ui.draggable).data("index");
		}
    });

    $("#meaning-droppable").droppable({
    	hoverClass: "dashed-border-black",
		drop: function(event, ui) {
			$("#meaning-display").html($(ui.draggable).html());
			unitMappingMeaning = $(ui.draggable).data("index");
			$('#meaning-options').modal();
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
    	$('.play-audio').css('visibility', 'hidden');
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



/* --------------------------------------------------------------
 *    OpenFluency2 - Flashcard 
 * --------------------------------------------------------------*/

var of2FlashcardFontSize = function() {
	// get all cards
	var flashcardUnitsArray = $('.flashcard-unit');
	
	// get the height of a card would have with a single character as literal
			// TODO: avoid hardcoding height of card

	for (var i = 0; i < flashcardUnitsArray.length; i++) {
	    var h = $(flashcardUnitsArray[i]).height();
	    var fontSize = $(flashcardUnitsArray[i]).css('font-size').replace(/[^-\d\.]/g, '');
	    while (h > 110) {
	        $(flashcardUnitsArray[i]).css('font-size', --fontSize + 'px');
	        h = $(flashcardUnitsArray[i]).height();
	    } 
	    $(flashcardUnitsArray[i]).height(139.593);
	}
}

/* -----------------------------------------
 * breadcrumb fixed
 * ---------------------------------------*/
$(document).ready(function() {
	try {
		$("#breadcrumb").sticky({topSpacing:50, zIndex: 1});
	} catch(err){}
});
