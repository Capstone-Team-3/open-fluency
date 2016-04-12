package com.openfluency.deck



import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.*

@TestFor(PreviewCardController)
@Mock([PreviewDeck,PreviewCard])
class PreviewCardControllerSpec extends Specification {
	PreviewDeck pd
    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
          params["max"] = 20
		  pd = new PreviewDeck(name:"name",filename:"fn").save(flush:true)
          params.deck = pd
		  def pc = new PreviewCard(deck_id: pd.id).save(flush:true)
		  pc = new PreviewCard(deck_id: pd.id).save(flush:true)
    }

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index(pd)

        then:"The model is correct"
            !model.previewCardInstanceList
            model.previewCardInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.previewCardInstance!= null
    }

    void "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            request.contentType = FORM_CONTENT_TYPE
            def previewCard = new PreviewCard()
            previewCard.validate()
            controller.save(previewCard)

        then:"The create view is rendered again with the correct model"
            model.previewCardInstance!= null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            previewCard = new PreviewCard(params)

            controller.save(previewCard)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/previewCard/show/1'
            controller.flash.message != null
            PreviewCard.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def previewCard = new PreviewCard(params)
            controller.show(previewCard)

        then:"A model is populated containing the domain instance"
            model.previewCardInstance == previewCard
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def previewCard = new PreviewCard(params)
            controller.edit(previewCard)

        then:"A model is populated containing the domain instance"
            model.previewCardInstance == previewCard
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            controller.update(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/previewCard/index'
            flash.message != null


        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def previewCard = new PreviewCard()
            previewCard.validate()
            controller.update(previewCard)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.previewCardInstance == previewCard

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            previewCard = new PreviewCard(params).save(flush: true)
            controller.update(previewCard)

        then:"A redirect is issues to the show action"
            response.redirectedUrl == "/previewCard/show/$previewCard.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            controller.delete(null)

        then:"A 404 is returned"
            response.redirectedUrl == '/previewCard/index'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def previewCard = new PreviewCard(params).save(flush: true)

        then:"It exists"
            PreviewCard.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(previewCard)

        then:"The instance is deleted"
            PreviewCard.count() == 0
            response.redirectedUrl == '/previewCard/index'
            flash.message != null
    }
}
