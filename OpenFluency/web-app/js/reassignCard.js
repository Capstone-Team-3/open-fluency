var dsFlashcardId = null;
var selectedCards = {};

$('.reassign-btn').click(function(event) {
	event.stopImmediatePropagation();
    $('#myModal2').modal();
    dsFlashCardId = this.dataset.id;
});

$('#reassign-submit').click(function(){ 
     console.log("Here");
     var deckdest_id = $('.decks-rb:checked').val();
     if(!deckdest_id) return;

     console.log(this.dataset.id);
     var query_string = '';
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
});
