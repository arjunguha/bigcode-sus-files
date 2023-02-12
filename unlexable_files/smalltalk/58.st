"
Description
-------------------

I allow to edit a rule during his creation.

Example
-------------------

	 html render: (CPPSRuleCreatorComponent rule: aModelQuery asRule)
"
Class {
	#name : #CPPSRuleCreatorComponent,
	#superclass : #CPPSAbstractRuleEditorComponent,
	#category : #'CPPS-Toolkit-RulesBrowser'
}

{ #category : #rendering }
CPPSRuleCreatorComponent >> renderCancelButtonOn: html [

	html mdlButton
		bePush;
		id: self rootDialogCloseId;
		with: (self
				 english: 'Discard'
				 french: 'Annuler')
]

{ #category : #actions }
CPPSRuleCreatorComponent >> save [

	self rulesManager addRule: self rule
]

{ #category : #accessing }
CPPSRuleCreatorComponent >> title [

	^ self
		  english: 'Create your rule'
		  french: 'Créez votre régle'
]
