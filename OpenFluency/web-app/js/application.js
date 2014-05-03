var initializeStyledElements = function() {
	$('.tooltiper').tooltip();
};

/**
* Searches for an Image in Flicker
* @param query: the search query to run
* @param resultsId: the selector of the div where the results will be appended (it's cleared before appending)
* @param urlField: the selector for the field where the url of the image should be saved on click
*/
var searchImage = function(query, results, urlField, resultPage) {
	
	// set helpful and need variables
	var apiKey = "ec50db25dd7a2b1d0c5d7b3ec404cce6";
	var sMethod = "flickr.photos.search";
	var respFormat = "&format=json&jsoncallback=?";
	var numPics = "6";
	var src;

	// Build base Flickr query url
	var baseUrl = "https://api.flickr.com/services/rest/?api_key=" + apiKey + "&method=" + sMethod + "&sort=relevance&per_page=" + numPics + "&page=" + resultPage + "&text=";

	// Remove previous pics
	$(results).empty();

	// Build full URL
	var queryStr = baseUrl + $(query).val() + respFormat;

	// Run query
	$.getJSON(queryStr, function(data){

		// Add responses to target
		$.each(data.photos.photo, function(i,item){
			src = "http://farm" + item.farm + ".static.flickr.com/" + item.server + "/" + item.id + "_" + item.secret + "_m.jpg";

			// Append results to result list
			$("<div/>").css("background-image", "url(" + src + ")").data('imageLink', src).attr("class", "img-rounded img-result").appendTo(results).click(function(){
				$(urlField).val($(this).data('imageLink'));
			});

			if (i >= numPics) {
				return false;
			}
		});
	});
};

var initializePracticeCards = function() {
	var type = $('#practice-container').data("rank-type").toLowerCase();
	console.log(type);

	if (type === 'meaning'){
		initializePracticeByMeaning();
	} else if (type === 'pronunciation'){
		initializePracticeByPronunciation();
	}
};

var initializePracticeByMeaning = function() {
	var $meaningContainer = $('.meaning');
	var meaning = $meaningContainer.html();
	$meaningContainer.html('<button class="btn" id="show-meaning">Show Meaning</button>');
	$('#show-meaning').on('click', function() {
		$meaningContainer.html(meaning);
	});

	var $imageContainer = $('#image-container');
	var image = $imageContainer.html();
	$imageContainer.html('<button class="btn" id="show-image">Show Image</button>');
	$('#show-image').on('click', function() {
		$imageContainer.html(image);
	});

	initializePracticeRanking();
};

var initializePracticeByPronunciation = function() {
	var $pronContainer = $('.pronunciation');
	var pron = $pronContainer.html();
	console.log($pronContainer);

	$pronContainer.html('<button class="btn" id="show-pronunciation">Show Pronunciation</button>');
	$('#show-pronunciation').on('click', function() {
		$pronContainer.html(pron);
	});

	initializePracticeRanking();
};

var initializePracticeRanking = function() {
	$(".ranker").click(function() {
        // Do some fancy UI transitions
		$(".flashcard").removeClass("slideInDown").addClass("slideOutLeft").delay(1000);

        // Set the ranking value in the form and submit it
		$("#ranking").val($(this).data('value'));
		$('.rankCardForm').submit();
    });
};

/**
* Initialize the form used to create Quizes
*/
var initializeQuizCreator = function() {
	$('.chapter-selector').change(function() {
		if($(this).prop('checked')) {
			that = this;
			$.ajax({
				url: "/OpenFluency/chapter/flashcardSelect/" + $(this).data('chapter')
			})
			.done(function(html) {
				$("#include-chapters").append(html);
				$("input[type='checkbox']", "#include-chapters").prop("checked", true);
			});
		} else {
			var element = $("#flashcard-select-" + $(this).data('chapter'));
			element.slideUp("slow", function() {
				element.remove();
			});
		}
	});
};

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

var initializeProficiencyRemovers = function() {
	$('.remove-proficiency').unbind('click').click(function(){
		$(this).parents('.panel').remove();
		return false;
	});
};

var setupFlashcardSearchPagination = function() {
	$('.paged-search a').click(function() {
		$("#offset").val((parseInt($(this).text())-1)*2);
		$('.searchUnitForm').submit();
		return false;		
	})
}