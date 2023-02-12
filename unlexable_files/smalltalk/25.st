Extension { #name : #LensesJsonExecutor }

{ #category : #'*GToolkit-Lenses-Widgets' }
LensesJsonExecutor >> gtChangesFor: aView [

	<gtView>
	self context ifNil: [ ^ aView empty ].

	^ aView forward
		  title: 'Changes';
		  priority: 5;
		  object: [ self context ];
		  view: #gtHistoryFor:
]

{ #category : #'*GToolkit-Lenses-Widgets' }
LensesJsonExecutor >> gtInputFor: aView [

	<gtView>
	self context ifNil: [ ^ aView empty ].

	^ aView forward
		  title: 'Input';
		  priority: 1;
		  object: [ self context initialInput ];
		  view: #gtLiveFor:
]

{ #category : #'*GToolkit-Lenses-Widgets' }
LensesJsonExecutor >> gtOutputFor: aView [

	<gtView>
	self context ifNil: [ ^ aView empty ].

	^ aView forward
		  title: 'Output';
		  priority: 2;
		  object: [ self context output ];
		  view: #gtLiveFor:
]
