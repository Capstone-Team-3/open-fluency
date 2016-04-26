<div id="customize-container" class="hidden">

	<button id="closeCustomization" class="btn btn-danger pull-right"><span class="glyphicon glyphicon-remove"></span></button>
	
	<!--g:form action="save" controller="customization" name="createCustomizationForm"-->
		<input type="hidden" id="c_fId" name="flashcardId" value="${flashcardInstance.id}"/>
		<input type="hidden" id="c_umId" name="unitMappingId" value="${flashcardInstance.unitMapping.id}"/>
		<input type="hidden" id="c_pId" name="pronunciationId" value="${flashcardInstance.pronunciation.id}"/>
		
		<h3>Customize Flashcard</h3>
			
		<div class="customize-image">

			<h4 id="custom-image"><span class="small glyphicon glyphicon-chevron-right"></span> Add/Change Image</h4>
			
			<div id="custom-image-container">
				<div class="form-group">
					<label for="c_imageLink">Paste an image URL:</label>
					<div class="row">
						<div class="col-lg-4">
							<input class="form-control" type="text" size="80" id="c_imageLink" name="c_imageLink" value="${flashcardInstance?.image?.url}"/>
						</div>
					</div>
				</div>

				<div class="flickr-search-container">
					<label>Or, search Flickr for an image:</label>
					<div class="row">
						<div class="col-lg-4">
							<input class="form-control" id="c_query" name="c_k" type="text" size="60" placeholder="Enter a keyword here to search photos" />
						</div>
						<button id="c_flickr_search" class="btn btn-info">Search</button>
					</div>
					
					<div id="c_results"></div>
					<div class="pagination">
						<button id="c_flickr_back" class="btn btn-default hidden">Back</button>
						<label id="c_flickr_page_number"></label>
						<button id="c_flickr_next" class="btn btn-default hidden">Next</button>
					</div>
				</div><!-- end flickr-search-container -->
			
			</div><!-- end custom-image-container -->

		</div><!-- end customize-image -->
		
		<button id="customizationCreate" class="center btn btn-success">Save Changes</button>
		<button id="customizationDelete" class="center btn btn-danger">Remove Current Customizations</button>
		<a href="#" id="cancel-customize">Cancel</a>
	<!--/g:form-->

</div><!-- end customize-container -->

<g:javascript src="customize.js"/>