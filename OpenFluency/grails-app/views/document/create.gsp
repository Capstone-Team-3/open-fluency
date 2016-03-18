<%@ page import="com.openfluency.deck.Document" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <title>Upload New Anki Deck</title>
    </head>
    <body>
        <div class="container deck-create">
         <ul class="breadcrumb">
            <li>
                <a href="${createLink(uri:'/') }">Home</a>
            </li>
            <li>Decks</li>
            <li>
                <g:link action="create" controller="deck" >Create New Deck</g:link>
            </li>

        </ul>

        <!-- Need to give ownership to uploaded deck -->
        <div class="nav" role="navigation">
            <ul><li><g:link class="deck" action="list">Uploaded Decks</g:link></li></ul>
        </div>
        <div class="content scaffold-create" role="main">
            <h1>Upload New Anki Deck</h1>
            <b>
        Select file downloaded from Ankiweb (You will need an account).
        The system will upload this file and preview all the data fields in a card.
        Please select fields that you want to upload to this system and tag them to the alphabet/type in our system.
        The tagging will enable automatic quiz generation.
            </b>
        <hr>
            <g:if test="${flash.message}"><div class="message" role="status">${flash.message}</div></g:if>
            <g:uploadForm action="upload">
                <fieldset class="form">
                    <!--
                    Existing Deck:<select id="filter-deck" class="form-control" name="filter-deck">
                        <option value="0" selected>Create New Deck</option>
                        <g:each in="${deckInstanceList}">
                            <g:if test="${it.id == deckId}">
                                <option value="${it.id}" selected>${it.title}</option>
                            </g:if>
                            <g:else>
                                <option value="${it.id}">${it.title}</option>
                            </g:else>
                        </g:each>
                    </select>
                    -->
                    Description:<input type="text" class = "form-control" name="Description" />
                    Language:<select id="filter-lang" class="form-control" name="filter-lang">
                        <g:each in="${languageInstanceList}">
                            <g:if test="${it.id == languageId}">
                                <option value="${it.id}" selected>${it.name}</option>
                            </g:if>
                            <g:else>
                                <option value="${it.id}">${it.name}</option>
                            </g:else>
                        </g:each>
                    </select>
                </fieldset>
                <br>
                <fieldset class="buttons">
                    <input type="file" name="file" />
                    <g:submitButton name="upload" class="save" value="Upload Anki File (.apkg)" />
                </fieldset>
            </g:uploadForm>
        </div>
        </div>
    </body>
</html>

