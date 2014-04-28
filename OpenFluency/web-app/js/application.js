var initializeStyledElements = function() {
	$('.tooltiper').tooltip();
}

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
	var respFormat = "&format=json&jsoncallback=?"
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
}

var initializePracticeCards = function() {
	var $meaningContainer = $('.meaning');
    var meaning = $meaningContainer.html();
    $meaningContainer.html('<button class="btn" id="show-meaning">Show Meaning</button>');
    $('#show-meaning').on('click', function() { 
        $meaningContainer.html(meaning);
    });

    $(".ranker").click(function() {
        // Set the ranking value in the form and submit it
        $("#ranking").val($(this).data('value'));
        $('.rankCardForm').submit();
    });
}

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
			});
		} else {
			var element = $("#flashcard-select-" + $(this).data('chapter'));
			element.slideUp("slow", function() {
				element.remove();	
			})
		}
	});
}