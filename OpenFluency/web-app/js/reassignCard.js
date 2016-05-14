var dsFlashcardId = null;
var selectedCards = {};		// ds with cards to move
var moveCardsMode = false;  // can click card and select for reassign?


$('#move-cards-button').click(function() {
	if (!moveCardsMode) {
		$('.donut-container').hide();
		$('#selected-cards-display').show();
		moveCardsMode = true;
		$('#move-cards-button').html("Stop moving cards");
		$('#move-cards-button').removeClass('btn-success');
		$('#move-cards-button').addClass('btn-danger');
	}
	
	else {
		$('.donut-container').show();
		$('#selected-cards-display').hide();
		moveCardsMode = false;
		$('#move-cards-button').html("Move cards");
		$('#move-cards-button').removeClass('btn-danger');
		$('#move-cards-button').addClass('btn-success');
	}
});


if (typeof localStorage.selectedCards != 'undefined') {
	$('#move-cards-button').click();
	console.log('here');
	selectedCards = JSON.parse(localStorage.selectedCards);
	localStorage.removeItem('selectedCards');
	
	// mark selected if any
	var flashcards = $('.flashcard');
	for (var i = 0; i < flashcards.length; i++) {
		for (var key in selectedCards) {
			if (selectedCards.hasOwnProperty(key)) {
				if (selectedCards[key] == flashcards[i].dataset.id) {
					$(flashcards[i].parentElement).addClass('card-selected');
				}
			}
		}
	}
}

$('#selected-cards-number').text(Object.keys(selectedCards).length);


$('.reassign-btn').click(function(event) {
	event.stopImmediatePropagation();
    $('#myModal2').modal();
    dsFlashCardId = this.dataset.id;
});

$('#reassign-submit').click(function(){ 
	console.log('clicked');
	var query_string = ''; 
	var deckdest_id = $('.decks-rb:checked').val();
     if(!deckdest_id) return;
     
     for(var key in selectedCards) {
         query_string += 'flashcard_id=' + selectedCards[key] + '&';
     }
     $.ajax({
         url:"/OpenFluency/flashcard/reassign?"+query_string+"deckdest_id="+deckdest_id,
         success : function(output) {
            dsFlashCardId = null;
            console.log("OK");
            document.location.reload(true);
         },
         error : function(err) {
            console.log(err);
            alert('reassign failed');
         }
     });
});  

$('.flashcard-result').click(function() {
	if (!moveCardsMode) return;
    test = this;
	console.log('.flashcard-result clicked')
	var selected = $(this).hasClass('card-selected');
	
	if (selected) {
		$(this).removeClass('card-selected');
        
        delete selectedCards['card_' + $(this).children().data('id')];
		console.log(this);
	}		
	else {
		$(this).addClass('card-selected');
        var flashcard_id = $(this).children().data('id');
        selectedCards['card_' + flashcard_id] = flashcard_id;
	}
	$('#selected-cards-number').text(Object.keys(selectedCards).length);
});


$('.step').click(function() {
	localStorage.selectedCards = JSON.stringify(selectedCards);
});

$('.nextLink').click(function() {
	localStorage.selectedCards = JSON.stringify(selectedCards);
});

$('.prevLink').click(function() {
	localStorage.selectedCards = JSON.stringify(selectedCards);
});

$('#move-cards-menu-button').click(function() {
	$('#myModal2').modal();
});

$('#reset-card-selection').click(function() {
	selectedCards = {};
	var arr = $('.flashcard-result');
	for (var i = 0; i < arr.length; i++) {
		if ($(arr[i]).hasClass('card-selected'))
			$(arr[i]).removeClass('card-selected');
	}
	$('#selected-cards-number').text("0"); 
});


