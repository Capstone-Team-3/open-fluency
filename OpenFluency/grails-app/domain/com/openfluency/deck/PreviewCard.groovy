package com.openfluency.deck

import javax.persistence.JoinColumn
import org.hibernate.mapping.ManyToOne



class PreviewCard {
	PreviewDeck deck;
	List units
	List types
	List fields
	static belongsTo = [deck: PreviewDeck]
	static hasMany = [units: String, fields: String, types: String]

    static constraints = {
		units (nullable: true,blank: true)
		types (nullable: true,blank: true)
		fields (nullable: true,blank: true)
    }
	
	static mapping = {
		units joinTable: [name: 'previewcard_units',
							key: 'previewcard_id',
							column: 'unit',
							type: "text"]

		fields joinTable: [name: 'previewcard_fields',
							key: 'previewcard_id',
							column: 'field',
							type: "text"]

		types joinTable: [name: 'previewcard_types',
							key: 'previewcard_id',
							column: 'type',
							type: "text"]
	}
}
