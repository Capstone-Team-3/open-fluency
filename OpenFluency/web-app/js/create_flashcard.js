$("#flickr_search").click(function(){
	$("#flickr_page_number").val(1).text(1);
	searchImage("#query", "#results", "#imageLink",1);
});
$("#flickr_next").click(function(){
	var targetPage = $("#flickr_page_number").val();
	targetPage++;
	searchImage("#query", "#results", "#imageLink", targetPage);
	$("#flickr_page_number").val(targetPage).text(targetPage);
});
$("#flickr_back").click(function(){
	var targetPage = $("#flickr_page_number").val();
	if (targetPage > 1) { 
		targetPage--;
		searchImage("#query", "#results", "#imageLink",targetPage);
		$("#flickr_page_number").val(targetPage).text(targetPage);
	}
});