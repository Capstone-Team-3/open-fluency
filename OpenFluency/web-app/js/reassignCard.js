var dsFlashcardId = null;
var selectedCards = {};



if (typeof localStorage.selectedCards != 'undefined') {
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

$('#move-cards-menu-button').click(function() {
	$('#myModal2').modal();
});