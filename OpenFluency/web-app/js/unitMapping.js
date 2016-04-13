// track inputs
window.unitMappingLiteral = null;
window.unitMappingPronunciation = null;
window.unitMappingAudioUrl = null;
window.unitMappingBackgroundImage = null;
window.unitMappingMeaning = null;

var cardIndex = 0;	// which card are we looking at
var previewCardData = JSON.parse($('#preview-card-instance-list').val()); // list of cards
var alphab = {}; 	// alphabet mappings

// populate units
loadUnitMappingPreviewCard(previewCardData, 0); 

/**
 * Populates units list with card data
 * @param previewCardData
 * @param cardIndex
 */

var a = null;
var b = null;
function loadUnitMappingPreviewCard(previewCardData, cardIndex) {
	$('#unit-ul').html("");
		
	for (var i = 0; i < previewCardData[cardIndex].units.length; i++) {
		var unitContent = previewCardData[cardIndex].units[i];  // what we'll see
		var transferData = unitContent; // what the receiving end will process
		var mediaLocation = '/OpenFluency/card-media/' + unitContent.replace('\\', '/');  // uri if it is an image/sound resource 
		
		
		if (previewCardData[cardIndex].types[i] == "Image" && previewCardData[cardIndex].units[i] != "") {
			unitContent = '<div style="height:200px;width:300px;background-image:url(' + mediaLocation + '); background-repeat:no-repeat;background-position:50% 50%; background-size:100% auto;"></div>';
			transferData = mediaLocation;
			a = unitContent;
		}
		else if (previewCardData[cardIndex].types[i] == "Sound") {
			unitContent = '<span class="play-unit-audio glyphicon glyphicon-volume-up" data-audio="' + mediaLocation + '"></span>'
			transferData = mediaLocation;
		}
		
		b = '<li class="orange unit-li"><h6><span style="background: #ccffcc;">' + previewCardData[cardIndex].fields[i] + '</span>-<span style="background: #66ffff;">' + previewCardData[cardIndex].types[i] + '</span></h6><div id="unit-' + i + '" class="draggable draggable-unit" data-index="' + i + '" data-transfer="' + transferData +'">' + unitContent + '</div></li>';
		$('#unit-ul').append('<li class="orange unit-li"><h6><span style="background: #ccffcc;">' + previewCardData[cardIndex].fields[i] + '</span>-<span style="background: #66ffff;">' + previewCardData[cardIndex].types[i] + '</span></h6><div id="unit-' + i + '" class="draggable draggable-unit" data-index="' + i + '" data-transfer="' + transferData +'">' + unitContent + '</div></li>');
	}
	if (unitMappingLiteral != null) $('#flashcard-literal').html(previewCardData[cardIndex].units[unitMappingLiteral]);
	if (unitMappingPronunciation != null) $('#pronounced').html("pronounced " + previewCardData[cardIndex].units[unitMappingPronunciation]);
	if (unitMappingAudioUrl != null) {
		$('#audio-url-display').html("/OpenFluency/card-media/" + previewCardData[cardIndex].units[unitMappingAudioUrl]);
		$('#um-flashcard-audio').attr('src', "/OpenFluency/card-media/" + previewCardData[cardIndex].units[unitMappingAudioUrl]);
	}
	if (unitMappingBackgroundImage != null) $('#flashcard-image').css("background-image", "url(" + $("#unit-" + unitMappingBackgroundImage).data('transfer') + ")");
	if (unitMappingMeaning != null) $('#meaning-display').html(previewCardData[cardIndex].units[unitMappingMeaning]);
	
	try {
		initializeUnitMappingDraggable();	// new elements are not registered as draggable.. refresh draggables.
		initializeAudio();					// idem
	} catch(err){}
}

$('#submit-unitMapping-button').click(function() {
	//test123();
	$('#algorithm-options').modal();
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

$('.m-alpha-options').click(function() {
	if (unitMappingMeaning == null) return;
	alphab['' + unitMappingMeaning] = this.dataset.alph;
	$('#meaning-options').modal('hide');
});

$('.algorithm-options').click(function() {
	var algo = this.dataset.algorithm;
	if (algo) {
		requestCreateOpenFluencyDeck(algo);
	}
	$('#algorithm-options').modal('hide');
});


/**
 * Sends ajax post request to server. Creates deck with chosen unit mappings
 * @param algo
 */
function requestCreateOpenFluencyDeck(algo) {
	$('.spinner').show();
	var algorithm = algo || "sw2";
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
		alphaIndices: alphab,
		algorithm: algorithm,
		name: unitMappingPreviewDeckName || "",
		description: unitMappingPreviewDeckDescription || ""
	};
	
	var obj = JSON.stringify(dat);
	var previewDeckInstanceId = $('#preview-deck-instance-id').val();

	// ensure request has required parameters
	if (unitMappingLiteral == null || unitMappingPronunciation == null || unitMappingMeaning == null || previewDeckInstanceId == "") {
		console.log('argument fail');
		return;		
	}
	
	$.ajax({
		type: "POST",
		url: "/OpenFluency/previewDeck/unitMappingSubmit/" + previewDeckInstanceId,
		data: {payload: obj},
		success: function(output) {
			console.log(output);
			$('.spinner').hide();
			alert("OpenFluency deck created!");
			
		},
		error: function(err) {
			console.log('err', err);
			$('.spinner').hide();
			alert("An error has occurred");
		}
	}); 
}


$('#edit-preview-deck-name').click(function() {
	$('#map-edit-name-modal').modal();
});

$('#edit-preview-deck-description').click(function() {
	$('#map-edit-description-modal').modal();
});

$('#map-change-deck-name-submit').click(function() {
	if (typeof unitMappingPreviewDeckName == 'undefined') {
		alert('An error has occured');
		return;
	}
	var name = $('#map-input-deck-name').val();
	if (name || 0 !== name.length) {
		window.unitMappingPreviewDeckName = name;
		$('#map-preview-deck-name').text(name);
	}
});

$('#map-change-deck-description-submit').click(function() {
	if (typeof unitMappingPreviewDeckDescription == 'undefined') {
		alert('An error has occured');
		return;
	}
	var description = $('#map-input-deck-description').val();
	if (description || 0 !== description.length) {
		window.unitMappingPreviewDeckDescription = description;
		$('#map-preview-deck-description').text(description);
	}
});