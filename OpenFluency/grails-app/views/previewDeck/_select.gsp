<style>

#units-div {
	height: 0px; 
}

</style>



<div  id="unit-mapping-div" class="row" style="margin-left: auto; margin-right: auto;">

	<button id="submit-unitMapping-button" class="btn btn-success btn-sm" style="float:right">Submit</button>

	<div id="units-div">
	  	<div id="units-list-container" class=""> 
	  		<h4>Units <button id="previous-unit" class="btn btn-sm btn-primary" style="margin-right:5px;">previous</button><button id="next-unit" class="btn btn-sm btn-primary">next</button></h4>
	  		<ul id="unit-ul"> 
			  	<g:each status="i" var="item" in="${test}">
			  		<li class="orange unit-li"><div class='draggable draggable-unit' data-index="${i}">${item}</div></li>
			  	</g:each>
		  	</ul>
		</div>
	</div>
	 
	 
	<div id="unit-mapping-card-container">
		<h4>Drag and drop to card</h4>
		<div class="flashcard animated slideInRight"> 
			<div class="panel panel-default">
				<div class="panel-body">
					<div class="flashcard-header">
					
						<span id="clear-literal" class="close-thin"></span>
						<div style="min-height: 190px;">
							<span class="label-literal"></span>
							<h1 id="flashcard-literal" class="flashcard-unit droppable"></h1>
						</div>
						
						<div class="pronunciation">
							<span id="clear-pronunciation" class="close-thin"></span>
							<div id="pronunciation-droppable">
								<span id="pronounced">(pronunciation text)</span>
								<span class="play-audio glyphicon glyphicon-volume-up" style="visibility: hidden;"></span>
								<audio class="flashcard-audio hidden" id="um-flashcard-audio" src="" controls></audio>
							</div>
						</div>
						<div id="audio-url-droppable">
							<span id="clear-audio-url" class="close-thin"></span>
							<span id="audio-url-display">(audio url)</span>
						</div>
						</div>
		
						<div id="image-container">
							<span id="clear-image" class="close-thin"></span>
							<div id="flashcard-image" class="flashcard-img" style="font: 14px/100% arial, sans-serif;text-shadow: 1px 1px 0 #fff;">(Image Url)</div>
						</div>
		
					<div id="meaning-droppable">
						<span id="clear-meaning" class="close-thin"></span>
						<span id="meaning-display">(meaning text)</span>
					</div>
				</div>
				</div>
			</div> 
		</div>
		
		
	 
	<br style="clear:both">
	
	<!-- Modal -->
	  <div class="modal fade" id="literal-options" role="dialog">
	    <div class="modal-dialog">
	    
	      <!-- Modal content-->
	      <div class="modal-content">
	        <div class="modal-header">
	          <h4 class="modal-title">Select Alphabet Type</h4>
	        </div>
	        <div class="modal-body">
	        	<div class="ul-container" id="literal-options">
		        	<ul>
		        		<li><button data-alph="English" class="btn btn-sm btn-info l-alpha-options">English</button></li>
		        		<g:each in="${fieldIndices }">
		        			<g:if test="${it != 'English' }" >
		        				<li><button data-alph="${it }" class="btn btn-sm btn-info l-alpha-options">${it }</button></li>
	        				</g:if>
		        		</g:each>
		        	</ul>
	        	</div>
	        	More Alphabets:<select id="select-alpha" class="form-control" name="select-alpha">
	        			<option value="" selected></option>
                        <g:each in="${languageInstanceList}">
                            <g:if test="${it.id == languageId}">
                                <option value="${it.name}" >${it.name}</option>
                            </g:if>
                            <g:else>
                                <option value="${it.name}">${it.name}</option>
                            </g:else>
                        </g:each>
                    </select>
                   <br>
	        	<div class="ul-container">
		        	<ul id="select-alphabet-results">
		        		
		        	</ul>
	        	</div>
	        	
	        </div>
	        <div class="modal-footer">
	        </div>
	      </div>
	      
	    </div>
	  </div>
	  
	  
	<!-- Modal -->
	  <div class="modal fade" id="pronunciation-options" role="dialog">
	    <div class="modal-dialog">
	    
	      <!-- Modal content-->
	      <div class="modal-content">
	        <div class="modal-header">
	          <h4 class="modal-title">Select Alphabet Type</h4>
	        </div>
	        <div class="modal-body" id="pronunciation-alph-options">
	        	<div class="ul-container">
		        	<ul>
		        		<g:each in="${fieldIndices }">
		        			<li><button data-alph="${it }" class="btn btn-sm btn-info p-alpha-options">${it }</button></li>
		        		</g:each>
		        	</ul>
	        	</div>
	        	
	        	More Alphabets:<select id="p-select-alpha" class="form-control" name="select-alpha">
	        			<option value="" selected></option>
                        <g:each in="${languageInstanceList}">
                            <g:if test="${it.id == languageId}">
                                <option value="${it.name}" >${it.name}</option>
                            </g:if>
                            <g:else>
                                <option value="${it.name}">${it.name}</option>
                            </g:else>
                        </g:each>
                    </select>
                   <br>
	        	<div class="ul-container">
		        	<ul id="p-select-alphabet-results">
		        		
		        	</ul>
	        	</div>
	        	
	        </div>
	        <div class="modal-footer">
	        </div>
	      </div>
	      
	    </div>
	  </div>
	  
	  
	  
	  
	  <!-- Modal -->
	  <div class="modal fade" id="meaning-options" role="dialog">
	    <div class="modal-dialog">
	    
	      <!-- Modal content-->
	      <div class="modal-content">
	        <div class="modal-header">
	          <h4 class="modal-title">Select Alphabet Type</h4>
	        </div>
	        <div class="modal-body" id="meaning-options">
	        	<div class="ul-container">
		        	<ul>
		        		<g:each in="${fieldIndices }">
		        			<li><button data-alph="${it }" class="btn btn-sm btn-info m-alpha-options">${it }</button></li>
		        		</g:each>
		        	</ul>
	        	</div>
	        	
	        	More Alphabets:<select id="m-select-alpha" class="form-control" name="select-alpha">
	        			<option value="" selected></option>
                        <g:each in="${languageInstanceList}">
                            <g:if test="${it.id == languageId}">
                                <option value="${it.name}" >${it.name}</option>
                            </g:if>
                            <g:else>
                                <option value="${it.name}">${it.name}</option>
                            </g:else>
                        </g:each>
                    </select>
                   <br>
	        	<div class="ul-container">
		        	<ul id="m-select-alphabet-results">
		        		
		        	</ul>
	        	</div>
	        	
	        </div>
	        <div class="modal-footer">
	        </div>
	      </div>
	      
	    </div>
	  </div>
	  
	  
	  <!-- Modal -->
	  <div class="modal fade" id="algorithm-options" role="dialog">
	    <div class="modal-dialog">
	    
	      <!-- Modal content-->
	      <div class="modal-content">
	        <div class="modal-header">
	          <h4 class="modal-title">Select algorithm type</h4>
	        </div>
	        <div class="modal-body">
	        	<div class="ul-container">
		        	<ul>
		        		<li><button data-algorithm="sm2" class="btn btn-sm btn-info algorithm-options">SM2-Spaced-Repetition</button></li>
		        		<li><button data-algorithm="lws" class="btn btn-sm btn-info algorithm-options">Linear-With-Shuffle</button></li>
		        	</ul>
	        	</div>
	        </div>
	        <div class="modal-footer">Selecting will create OpenFluencyDeck</div>
	      </div>
	      
	    </div>
	  </div>
	
</div>

<!-- storing values that need to be visible by the js functionality -->
<input type="hidden" id="preview-deck-instance-id" value="${previewDeckInstance.id}"/>
<input type="hidden" id="preview-card-instance-list" value="${previewCardInstanceList}"/>

<g:javascript src="unitMapping.js" />
<script>
$(document).ready(function(){
	initializeAudio();
	initializeUnitMappingDraggable();
});
	
</script>

