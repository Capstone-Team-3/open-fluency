$(document).ready(function(){
    var rankTypeVal = $('h3').data('rank-type');

/*----------------------------------------------------------------------------*/
/* Show/hide customization panel
/*----------------------------------------------------------------------------*/

    $('#customizationBtn').on('click', function(){
        $('#customize-container').removeClass('hidden');
        $('#custom-image-container').hide();
        $('#custom-audio-container').hide();
        $('#customizationBtn').hide();
    });

    $("#closeCustomization, #cancel-customize").on('click', function(){
        resetChevrons();
        $("#customizationBtn").show();
        $('#customize-container').addClass('hidden');
    });

/*----------------------------------------------------------------------------*/
/* Show/hide the image and audio sections of the customization panel
/*----------------------------------------------------------------------------*/

    $('#custom-image').on('click', function(){
        $('#custom-image-container').toggle('slow');
        toggleChevron(this);
    });

    $('#custom-audio').on('click', function(){
        $('#custom-audio-container').toggle('slow');
        toggleChevron(this);
    });

    function toggleChevron(container) {
        var $glyph = $('.glyphicon', container);
        $glyph.toggleClass('glyphicon-chevron-right')
              .toggleClass('glyphicon-chevron-down');
    }

    function resetChevrons() {
        var $audioContainer = $('#custom-audio span'),
            $imageContainer = $('#custom-image span');
        
        if ($imageContainer.hasClass('glyphicon-chevron-down')){
            toggleChevron('#custom-image');
        }
        if ($audioContainer.hasClass('glyphicon-chevron-down')){
            toggleChevron('#custom-audio');
        }
    }

/*----------------------------------------------------------------------------*/
/* Create audio recording
/*----------------------------------------------------------------------------*/

    $("#c_start_rec_button").on('click', function(){
        setWorkerPath("../../js/recorderWorker.js");
        startRecording();
        $("#c_audioSaveMessage").removeClass('hidden');
        $("#customizationCreate").removeClass('btn-success')
                                 .addClass('btn-warning');
    });

    $("#c_stop_rec_button").on('click', function(){
        stopRecording("#c_audioClip", $("#c_pId").val(), "");
        $("#c_save_rec_button").removeClass('hidden');
    });

    $("#c_save_rec_button").on('click', function(){
        saveAudioRecording(formData);
        $("#c_audioSaveMessage").addClass('hidden');
        $(this).addClass('hidden');
        $("#customizationCreate").removeClass('btn-warning')
                                 .addClass('btn-success');
    });

    function saveAudioRecording(formDataObj){
        // Create the packet
        var fd = formDataObj;
        // Send it
        $.ajax({
            type: 'POST',
            url: '/OpenFluency/audio/save',
            data: fd,
            processData: false,
            contentType: false
        }).done(function(audioInstance) {
            if(audioInstance.id) {
                console.log("Success!");
                $("#c_audio_id").val(audioInstance.id);
                console.log(audioInstance.id);
            } else {
                console.log("Something went wrong!");
            }
        });
    }

/*----------------------------------------------------------------------------*/
/* Add image customization
/*----------------------------------------------------------------------------*/

    $("#c_flickr_search").on('click', function(){
        $('#c_flickr_back').removeClass('hidden');
        $('#c_flickr_next').removeClass('hidden');

        $("#c_flickr_page_number").val(1).text(1);
        searchImage("#c_query", "#c_results", "#c_imageLink",1);
    });

    $("#c_flickr_next").on('click', function(){
        var targetPage = $("#c_flickr_page_number").val();
        targetPage++;
        searchImage("#c_query", "#c_results", "#c_imageLink", targetPage);
        $("#c_flickr_page_number").val(targetPage).text(targetPage);
    });

    $("#c_flickr_back").on('click', function(){
        var targetPage = $("#c_flickr_page_number").val();
        if (targetPage > 1) {
            targetPage--;
            searchImage("#c_query", "#c_results", "#c_imageLink", targetPage);
            $("#c_flickr_page_number").val(targetPage).text(targetPage);
        }
    });

/*----------------------------------------------------------------------------*/
/* Remove Customizations (image and audio file) from flashcard */
/*----------------------------------------------------------------------------*/
    
    $('#customizationDelete').on('click', function(){
        deleteCustomization();
    });

    function deleteCustomization(){
        // Create deletion packet
        var custDeleteData = new FormData();
        custDeleteData.append('flashcardId', $('#c_fId').val());
        console.log('deleting: ' + $('#c_fId').val());
        // Send it
        $.ajax({
            type: 'POST',
            url: '/OpenFluency/customization/remove',
            data: custDeleteData,
            processData: false,
            contentType: false
        }).done(function(resp) {
            //window.location.replace(document.URL);
            console.log('Delete Done');
        });
    }

/*----------------------------------------------------------------------------*/
/* Save new customizations (image and/or audio file)
/*----------------------------------------------------------------------------*/

    $('#customizationCreate').on('click', function(){
        resetChevrons();
        $('#customize-container').addClass('hidden');
        $('#customizationBtn').show();
        saveCustomization();
    });

    function saveCustomization(){
        // Create the packet
        var custData = new FormData();
        custData.append('flashcardId', $('#c_fId').val());
        custData.append('unitMappingId', $('#c_umId').val());
        custData.append('imageLink', $('#c_imageLink').val());
        custData.append('audioId', $('#c_audio_id').val());
        // Send it
        $.ajax({
            type: 'POST',
            url: '/OpenFluency/customization/save',
            data: custData,
            processData: false,
            contentType: false
        }).done(function(customizationInstance) {
            // set url so has ranking type at end
            window.location.replace(window.location.href + "?rankingType=" + rankTypeVal);

            // force reload from server
            window.location.reload(true);
        });
    }

});