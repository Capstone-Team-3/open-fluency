var dsFlashcardId = null;
var selectedCards = [];

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
	 $.ajax({
	     url:"/OpenFluency/flashcard/reassign?flashcard_id="+dsFlashCardId+"&deckdest_id="+deckdest_id,
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
	console.log('.flashcard-result clicked')
	var selected = $(this).hasClass('card-selected');
	
	if (selected) {
		$(this).removeClass('card-selected');
		console.log(this);
	}		
	else {
		$(this).addClass('card-selected');
	}
});