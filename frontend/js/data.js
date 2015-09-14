(function() {
	
	var pocketdocData = angular.module('pocketdocData', []);
	
	pocketdocData.factory('DataService', function() {
		
		var DataService = {
			
			questions: function() {
				return [
					{
						"id": 0,
						"type": "yesnomaybe",
						"description": [
							{
								"lang": 0,
								"text": "Hat das Problem innerhalb der letzten 24 Stunden angefangen?"
							}, {
								"lang": 1,
								"text": "Did the problem start in the last 24 hours?"
							}
						],
						"answers": [
							{
								"id": 0,
								"desc": [
									{
										"lang": 0,
										"text": "Ja"
									}, {
										"lang": 1,
										"text": "Yes"
									}
								],
								"style": "md-accent",
								"next_questions": [1]
							}, {
								"id": 2,
								"desc": [
									{
										"lang": 0,
										"text": "Weiss nicht"
									}, {
										"lang": 1,
										"text": "Don't know"
									}
								],
								"style": "md-primary",
								"next_questions": [1]
							}, {
								"id": 1,
								"desc": [
									{
										"lang": 0,
										"text": "Nein"
									}, {
										"lang": 1,
										"text": "No"
									}
								],
								"style": "md-warn",
								"next_questions": [2]
							}
						]
					}, {
					"id": 1,
						"type": "yesnomaybe",
						"description": [
							{
								"lang": 0,
								"text": "Hat das Problem innerhalb der letzten 4 Stunden angefangen?"
							}, {
								"lang": 1,
								"text": "Did the problem start in the last 4 hours?"
							}
						],
						"answers": [
							{
								"id": 0,
								"desc": [
									{
										"lang": 0,
										"text": "Ja"
									}, {
										"lang": 1,
										"text": "Yes"
									}
								],
								"style": "md-accent",
								"next_questions": [5]
							}, {
								"id": 2,
								"desc": [
									{
										"lang": 0,
										"text": "Weiss nicht"
									}, {
										"lang": 1,
										"text": "Don't know"
									}
								],
								"style": "md-primary",
								"next_questions": [5]
							}, {
								"id": 1,
								"desc": [
									{
										"lang": 0,
										"text": "Nein"
									}, {
										"lang": 1,
										"text": "No"
									}
								],
								"style": "md-warn",
								"next_questions": [3]
							}
						]
					}, {
					"id": 2,
						"type": "yesnomaybe",
						"description": [
							{
								"lang": 0,
								"text": "Hat das Problem innerhalb der letzten 2 Wochen angefangen?"
							}, {
								"lang": 1,
								"text": "Did the problem start in the last 2 weeks?"
							}
						],
						"answers": [
							{
								"id": 0,
								"desc": [
									{
										"lang": 0,
										"text": "Ja"
									}, {
										"lang": 1,
										"text": "Yes"
									}
								],
								"style": "md-accent",
								"next_questions": [4]
							}, {
								"id": 2,
								"desc": [
									{
										"lang": 0,
										"text": "Weiss nicht"
									}, {
										"lang": 1,
										"text": "Don't know"
									}
								],
								"style": "md-primary",
								"next_questions": [4]
							}, {
								"id": 1,
								"desc": [
									{
										"lang": 0,
										"text": "Nein"
									}, {
										"lang": 1,
										"text": "No"
									}
								],
								"style": "md-warn",
								"next_questions": [5]
							}
						]
					}, {
					"id": 3,
						"type": "yesnomaybe",
						"description": [
							{
								"lang": 0,
								"text": "Hat das Problem innerhalb der letzten 8 Stunden angefangen?"
							}, {
								"lang": 1,
								"text": "Did the problem start in the last 8 hours?"
							}
						],
						"answers": [
							{
								"id": 0,
								"desc": [
									{
										"lang": 0,
										"text": "Ja"
									}, {
										"lang": 1,
										"text": "Yes"
									}
								],
								"style": "md-accent",
								"next_questions": [5]
							}, {
								"id": 2,
								"desc": [
									{
										"lang": 0,
										"text": "Weiss nicht"
									}, {
										"lang": 1,
										"text": "Don't know"
									}
								],
								"style": "md-primary",
								"next_questions": [5]
							}, {
								"id": 1,
								"desc": [
									{
										"lang": 0,
										"text": "Nein"
									}, {
										"lang": 1,
										"text": "No"
									}
								],
								"style": "md-warn",
								"next_questions": [5]
							}
						]
					}, {
					"id": 4,
						"type": "yesnomaybe",
						"description": [
							{
								"lang": 0,
								"text": "Hat das Problem innerhalb der letzten Woche angefangen?"
							}, {
								"lang": 1,
								"text": "Did the problem start in the last week?"
							}
						],
						"answers": [
							{
								"id": 0,
								"desc": [
									{
										"lang": 0,
										"text": "Ja"
									}, {
										"lang": 1,
										"text": "Yes"
									}
								],
								"style": "md-accent",
								"next_questions": [5]
							}, {
								"id": 2,
								"desc": [
									{
										"lang": 0,
										"text": "Weiss nicht"
									}, {
										"lang": 1,
										"text": "Don't know"
									}
								],
								"style": "md-primary",
								"next_questions": [5]
							}, {
								"id": 1,
								"desc": [
									{
										"lang": 0,
										"text": "Nein"
									}, {
										"lang": 1,
										"text": "No"
									}
								],
								"style": "md-warn",
								"next_questions": [5]
							}
						]
					}, {
						"id": 5,
						"type": "yesnomaybe",
						"description": [
							{
								"lang": 0,
								"text": "Haben Sie Fieber?"
							}, {
								"lang": 1,
								"text": "Do you have a temperature?"
							}
						],
						"answers": [
							{
								"id": 0,
								"desc": [
									{
										"lang": 0,
										"text": "Ja"
									}, {
										"lang": 1,
										"text": "Yes"
									}
								],
								"style": "md-accent",
								"next_questions": [6]
							}, {
								"id": 2,
								"desc": [
									{
										"lang": 0,
										"text": "Weiss nicht"
									}, {
										"lang": 1,
										"text": "Don't know"
									}
								],
								"style": "md-primary",
								"next_questions": [6]
							}, {
								"id": 1,
								"desc": [
									{
										"lang": 0,
										"text": "Nein"
									}, {
										"lang": 1,
										"text": "No"
									}
								],
								"style": "md-warn",
								"next_questions": [7]
							}
						]
					}, {
						"id": 6,
						"type": "yesnomaybe",
						"description": [
							{
								"lang": 0,
								"text": "Hohes Fieber?"
							}, {
								"lang": 1,
								"text": "High Temperature?"
							}
						],
						"answers": [
							{
								"id": 0,
								"desc": [
									{
										"lang": 0,
										"text": "Ja"
									}, {
										"lang": 1,
										"text": "Yes"
									}
								],
								"style": "md-accent",
								"next_questions": [7]
							}, {
								"id": 2,
								"desc": [
									{
										"lang": 0,
										"text": "Weiss nicht"
									}, {
										"lang": 1,
										"text": "Don't know"
									}
								],
								"style": "md-primary",
								"next_questions": [7]
							}, {
								"id": 1,
								"desc": [
									{
										"lang": 0,
										"text": "Nein"
									}, {
										"lang": 1,
										"text": "No"
									}
								],
								"style": "md-warn",
								"next_questions": [7]
							}
						]
					}, {
						"id": 7,
						"type": "yesnomaybe",
						"description": [
							{
								"lang": 0,
								"text": "Haben Sie Schmerzen?"
							}, {
								"lang": 1,
								"text": "Do you have pain?"
							}
						],
						"answers": [
							{
								"id": 0,
								"desc": [
									{
										"lang": 0,
										"text": "Ja"
									}, {
										"lang": 1,
										"text": "Yes"
									}
								],
								"style": "md-accent",
								"next_questions": [8]
							}, {
								"id": 2,
								"desc": [
									{
										"lang": 0,
										"text": "Weiss nicht"
									}, {
										"lang": 1,
										"text": "Don't know"
									}
								],
								"style": "md-primary",
								"next_questions": [8]
							}, {
								"id": 1,
								"desc": [
									{
										"lang": 0,
										"text": "Nein"
									}, {
										"lang": 1,
										"text": "No"
									}
								],
								"style": "md-warn",
								"next_questions": [22]
							}
						]
					}, {
						"id": 8,
						"type": "yesnomaybe",
						"description": [
							{
								"lang": 0,
								"text": "Sind die Schmerzen leicht?"
							}, {
								"lang": 1,
								"text": "Is the pain weak?"
							}
						],
						"answers": [
							{
								"id": 0,
								"desc": [
									{
										"lang": 0,
										"text": "Ja"
									}, {
										"lang": 1,
										"text": "Yes"
									}
								],
								"style": "md-accent",
								"next_questions": [10]
							}, {
								"id": 2,
								"desc": [
									{
										"lang": 0,
										"text": "Weiss nicht"
									}, {
										"lang": 1,
										"text": "Don't know"
									}
								],
								"style": "md-primary",
								"next_questions": [10]
							}, {
								"id": 1,
								"desc": [
									{
										"lang": 0,
										"text": "Nein"
									}, {
										"lang": 1,
										"text": "No"
									}
								],
								"style": "md-warn",
								"next_questions": [9]
							}
						]
					}, {
						"id": 9,
						"type": "yesnomaybe",
						"description": [
							{
								"lang": 0,
								"text": "Sind die Schmerzen stark?"
							}, {
								"lang": 1,
								"text": "Is the pain strong?"
							}
						],
						"answers": [
							{
								"id": 0,
								"desc": [
									{
										"lang": 0,
										"text": "Ja"
									}, {
										"lang": 1,
										"text": "Yes"
									}
								],
								"style": "md-accent",
								"next_questions": [11]
							}, {
								"id": 2,
								"desc": [
									{
										"lang": 0,
										"text": "Weiss nicht"
									}, {
										"lang": 1,
										"text": "Don't know"
									}
								],
								"style": "md-primary",
								"next_questions": [11]
							}, {
								"id": 1,
								"desc": [
									{
										"lang": 0,
										"text": "Nein"
									}, {
										"lang": 1,
										"text": "No"
									}
								],
								"style": "md-warn",
								"next_questions": [12]
							}
						]
					}, {
						"id": 10,
						"type": "yesnomaybe",
						"description": [
							{
								"lang": 0,
								"text": "Sind die Schmerzen sehr leicht?"
							}, {
								"lang": 1,
								"text": "Is the pain very weak?"
							}
						],
						"answers": [
							{
								"id": 0,
								"desc": [
									{
										"lang": 0,
										"text": "Ja"
									}, {
										"lang": 1,
										"text": "Yes"
									}
								],
								"style": "md-accent",
								"next_questions": [12]
							}, {
								"id": 2,
								"desc": [
									{
										"lang": 0,
										"text": "Weiss nicht"
									}, {
										"lang": 1,
										"text": "Don't know"
									}
								],
								"style": "md-primary",
								"next_questions": [12]
							}, {
								"id": 1,
								"desc": [
									{
										"lang": 0,
										"text": "Nein"
									}, {
										"lang": 1,
										"text": "No"
									}
								],
								"style": "md-warn",
								"next_questions": [12]
							}
						]
					}, {
						"id": 11,
						"type": "yesnomaybe",
						"description": [
							{
								"lang": 0,
								"text": "Sind die Schmerzen sehr stark?"
							}, {
								"lang": 1,
								"text": "Is the pain very strong?"
							}
						],
						"answers": [
							{
								"id": 0,
								"desc": [
									{
										"lang": 0,
										"text": "Ja"
									}, {
										"lang": 1,
										"text": "Yes"
									}
								],
								"style": "md-accent",
								"next_questions": [17]
							}, {
								"id": 2,
								"desc": [
									{
										"lang": 0,
										"text": "Weiss nicht"
									}, {
										"lang": 1,
										"text": "Don't know"
									}
								],
								"style": "md-primary",
								"next_questions": [17]
							}, {
								"id": 1,
								"desc": [
									{
										"lang": 0,
										"text": "Nein"
									}, {
										"lang": 1,
										"text": "No"
									}
								],
								"style": "md-warn",
								"next_questions": [18]
							}
						]
					}, {
						"id": 12,
						"type": "yesnomaybe",
						"description": [
							{
								"lang": 0,
								"text": "Haben Sie Sodbrennen?"
							}, {
								"lang": 1,
								"text": "Do you experience heartburn?"
							}
						],
						"answers": [
							{
								"id": 0,
								"desc": [
									{
										"lang": 0,
										"text": "Ja"
									}, {
										"lang": 1,
										"text": "Yes"
									}
								],
								"style": "md-accent",
								"next_questions": [13]
							}, {
								"id": 2,
								"desc": [
									{
										"lang": 0,
										"text": "Weiss nicht"
									}, {
										"lang": 1,
										"text": "Don't know"
									}
								],
								"style": "md-primary",
								"next_questions": [13]
							}, {
								"id": 1,
								"desc": [
									{
										"lang": 0,
										"text": "Nein"
									}, {
										"lang": 1,
										"text": "No"
									}
								],
								"style": "md-warn",
								"next_questions": [13]
							}
						]
					}, {
						"id": 13,
						"type": "yesnomaybe",
						"description": [
							{
								"lang": 0,
								"text": "Handelt es sich um Kopfschmerzen?"
							}, {
								"lang": 1,
								"text": "Are you experiencing headaches?"
							}
						],
						"answers": [
							{
								"id": 0,
								"desc": [
									{
										"lang": 0,
										"text": "Ja"
									}, {
										"lang": 1,
										"text": "Yes"
									}
								],
								"style": "md-accent",
								"next_questions": [14],
								"diagnosis": 0,
								"action_suggestion": 3
							}, {
								"id": 2,
								"desc": [
									{
										"lang": 0,
										"text": "Weiss nicht"
									}, {
										"lang": 1,
										"text": "Don't know"
									}
								],
								"style": "md-primary",
								"next_questions": [14]
							}, {
								"id": 1,
								"desc": [
									{
										"lang": 0,
										"text": "Nein"
									}, {
										"lang": 1,
										"text": "No"
									}
								],
								"style": "md-warn",
								"next_questions": [14]
							}
						]
					}, {
						"id": 14,
						"type": "yesnomaybe",
						"description": [
							{
								"lang": 0,
								"text": "Sind die Schmerzen stetig und ununterbrochen da?"
							}, {
								"lang": 1,
								"text": "Is the pain static and always there?"
							}
						],
						"answers": [
							{
								"id": 0,
								"desc": [
									{
										"lang": 0,
										"text": "Ja"
									}, {
										"lang": 1,
										"text": "Yes"
									}
								],
								"style": "md-accent",
								"next_questions": [15]
							}, {
								"id": 2,
								"desc": [
									{
										"lang": 0,
										"text": "Weiss nicht"
									}, {
										"lang": 1,
										"text": "Don't know"
									}
								],
								"style": "md-primary",
								"next_questions": [15]
							}, {
								"id": 1,
								"desc": [
									{
										"lang": 0,
										"text": "Nein"
									}, {
										"lang": 1,
										"text": "No"
									}
								],
								"style": "md-warn",
								"next_questions": [22]
							}
						]
					}, {
						"id": 15,
						"type": "yesnomaybe",
						"description": [
							{
								"lang": 0,
								"text": "Gibt es etwas, was die Schmerzen viel schlimmer werden lässt?"
							}, {
								"lang": 1,
								"text": "Is there anything that makes the pain worse?"
							}
						],
						"answers": [
							{
								"id": 0,
								"desc": [
									{
										"lang": 0,
										"text": "Ja"
									}, {
										"lang": 1,
										"text": "Yes"
									}
								],
								"style": "md-accent",
								"next_questions": [16]
							}, {
								"id": 2,
								"desc": [
									{
										"lang": 0,
										"text": "Weiss nicht"
									}, {
										"lang": 1,
										"text": "Don't know"
									}
								],
								"style": "md-primary",
								"next_questions": [16]
							}, {
								"id": 1,
								"desc": [
									{
										"lang": 0,
										"text": "Nein"
									}, {
										"lang": 1,
										"text": "No"
									}
								],
								"style": "md-warn",
								"next_questions": [16]
							}
						]
					}, {
						"id": 16,
						"type": "yesnomaybe",
						"description": [
							{
								"lang": 0,
								"text": "Leiden Sie unter Nackenschmerzen?"
							}, {
								"lang": 1,
								"text": "Do you suffer from neck pain?"
							}
						],
						"answers": [
							{
								"id": 0,
								"desc": [
									{
										"lang": 0,
										"text": "Ja"
									}, {
										"lang": 1,
										"text": "Yes"
									}
								],
								"style": "md-accent",
								"next_questions": [20],
								"diagnosis": 2,
								"action_suggestion": 2
							}, {
								"id": 2,
								"desc": [
									{
										"lang": 0,
										"text": "Weiss nicht"
									}, {
										"lang": 1,
										"text": "Don't know"
									}
								],
								"style": "md-primary",
								"next_questions": [20]
							}, {
								"id": 1,
								"desc": [
									{
										"lang": 0,
										"text": "Nein"
									}, {
										"lang": 1,
										"text": "No"
									}
								],
								"style": "md-warn",
								"next_questions": [20]
							}
						]
					}, {
						"id": 17,
						"type": "yesnomaybe",
						"description": [
							{
								"lang": 0,
								"text": "Sind die Schmerzen stetig und ununterbrochen da?"
							}, {
								"lang": 1,
								"text": "Is the pain static and always there?"
							}
						],
						"answers": [
							{
								"id": 0,
								"desc": [
									{
										"lang": 0,
										"text": "Ja"
									}, {
										"lang": 1,
										"text": "Yes"
									}
								],
								"style": "md-accent",
								"next_questions": [22]
							}, {
								"id": 2,
								"desc": [
									{
										"lang": 0,
										"text": "Weiss nicht"
									}, {
										"lang": 1,
										"text": "Don't know"
									}
								],
								"style": "md-primary",
								"next_questions": [22]
							}, {
								"id": 1,
								"desc": [
									{
										"lang": 0,
										"text": "Nein"
									}, {
										"lang": 1,
										"text": "No"
									}
								],
								"style": "md-warn",
								"next_questions": [22]
							}
						]
					}, {
						"id": 18,
						"type": "yesnomaybe",
						"description": [
							{
								"lang": 0,
								"text": "Sind die Schmerzen stetig und ununterbrochen da?"
							}, {
								"lang": 1,
								"text": "Is the pain static and always there?"
							}
						],
						"answers": [
							{
								"id": 0,
								"desc": [
									{
										"lang": 0,
										"text": "Ja"
									}, {
										"lang": 1,
										"text": "Yes"
									}
								],
								"style": "md-accent",
								"next_questions": [19]
							}, {
								"id": 2,
								"desc": [
									{
										"lang": 0,
										"text": "Weiss nicht"
									}, {
										"lang": 1,
										"text": "Don't know"
									}
								],
								"style": "md-primary",
								"next_questions": [19]
							}, {
								"id": 1,
								"desc": [
									{
										"lang": 0,
										"text": "Nein"
									}, {
										"lang": 1,
										"text": "No"
									}
								],
								"style": "md-warn",
								"next_questions": [22]
							}
						]
					}, {
						"id": 19,
						"type": "yesnomaybe",
						"description": [
							{
								"lang": 0,
								"text": "Gibt es etwas, was die Schmerzen viel schlimmer werden lässt?"
							}, {
								"lang": 1,
								"text": "Is there anything that makes the pain worse?"
							}
						],
						"answers": [
							{
								"id": 0,
								"desc": [
									{
										"lang": 0,
										"text": "Ja"
									}, {
										"lang": 1,
										"text": "Yes"
									}
								],
								"style": "md-accent",
								"next_questions": [20]
							}, {
								"id": 2,
								"desc": [
									{
										"lang": 0,
										"text": "Weiss nicht"
									}, {
										"lang": 1,
										"text": "Don't know"
									}
								],
								"style": "md-primary",
								"next_questions": [20]
							}, {
								"id": 1,
								"desc": [
									{
										"lang": 0,
										"text": "Nein"
									}, {
										"lang": 1,
										"text": "No"
									}
								],
								"style": "md-warn",
								"next_questions": [20]
							}
						]
					}, {
						"id": 20,
						"type": "yesnomaybe",
						"description": [
							{
								"lang": 0,
								"text": "Haben Sie Husten?"
							}, {
								"lang": 1,
								"text": "Do you have a cough?"
							}
						],
						"answers": [
							{
								"id": 0,
								"desc": [
									{
										"lang": 0,
										"text": "Ja"
									}, {
										"lang": 1,
										"text": "Yes"
									}
								],
								"style": "md-accent",
								"next_questions": [21]
							}, {
								"id": 2,
								"desc": [
									{
										"lang": 0,
										"text": "Weiss nicht"
									}, {
										"lang": 1,
										"text": "Don't know"
									}
								],
								"style": "md-primary",
								"next_questions": [21]
							}, {
								"id": 1,
								"desc": [
									{
										"lang": 0,
										"text": "Nein"
									}, {
										"lang": 1,
										"text": "No"
									}
								],
								"style": "md-warn",
								"next_questions": [21]
							}
						]
					}, {
						"id": 21,
						"type": "yesnomaybe",
						"description": [
							{
								"lang": 0,
								"text": "Haben Sie Brustschmerzen?"
							}, {
								"lang": 1,
								"text": "Do you have chest pain?"
							}
						],
						"answers": [
							{
								"id": 0,
								"desc": [
									{
										"lang": 0,
										"text": "Ja"
									}, {
										"lang": 1,
										"text": "Yes"
									}
								],
								"style": "md-accent",
								"next_questions": [22],
								"diagnosis": 1,
								"action_suggestion": 1
							}, {
								"id": 2,
								"desc": [
									{
										"lang": 0,
										"text": "Weiss nicht"
									}, {
										"lang": 1,
										"text": "Don't know"
									}
								],
								"style": "md-primary",
								"next_questions": [22]
							}, {
								"id": 1,
								"desc": [
									{
										"lang": 0,
										"text": "Nein"
									}, {
										"lang": 1,
										"text": "No"
									}
								],
								"style": "md-warn",
								"next_questions": [22]
							}
						]
					}, {
						"id": 22,
						"type": "yesnomaybe",
						"description": [
							{
								"lang": 0,
								"text": "Haben Sie Auswurf?"
							}, {
								"lang": 1,
								"text": "Are you having sputum?"
							}
						],
						"answers": [
							{
								"id": 0,
								"desc": [
									{
										"lang": 0,
										"text": "Ja"
									}, {
										"lang": 1,
										"text": "Yes"
									}
								],
								"style": "md-accent",
								"next_questions": [23]
							}, {
								"id": 2,
								"desc": [
									{
										"lang": 0,
										"text": "Weiss nicht"
									}, {
										"lang": 1,
										"text": "Don't know"
									}
								],
								"style": "md-primary",
								"next_questions": [23]
							}, {
								"id": 1,
								"desc": [
									{
										"lang": 0,
										"text": "Nein"
									}, {
										"lang": 1,
										"text": "No"
									}
								],
								"style": "md-warn",
								"next_questions": [-1]
							}
						]
					}, {
						"id": 23,
						"type": "yesnomaybe",
						"description": [
							{
								"lang": 0,
								"text": "Haben Sie viel Auswurf?"
							}, {
								"lang": 1,
								"text": "Do you have a lot of sputum?"
							}
						],
						"answers": [
							{
								"id": 0,
								"desc": [
									{
										"lang": 0,
										"text": "Ja"
									}, {
										"lang": 1,
										"text": "Yes"
									}
								],
								"style": "md-accent",
								"next_questions": [-1],
								"diagnosis": 1,
								"action_suggestion": 1
							}, {
								"id": 2,
								"desc": [
									{
										"lang": 0,
										"text": "Weiss nicht"
									}, {
										"lang": 1,
										"text": "Don't know"
									}
								],
								"style": "md-primary",
								"next_questions": [-1]
							}, {
								"id": 1,
								"desc": [
									{
										"lang": 0,
										"text": "Nein"
									}, {
										"lang": 1,
										"text": "No"
									}
								],
								"style": "md-warn",
								"next_questions": [-1],
								"diagnosis": 1,
								"action_suggestion": 1
							}
						]
					}
				];
			},
			
			diagnoses: function() {
				return [
					{
						"id": 0,
						"short_desc": [
							{
								"lang": 0,
								"text": "Zu wenig getrunken"
							}, {
								"lang": 1,
								"text": "drank too little"
							}
						],
						"description": [
							{
								"lang": 0,
								"text": "Sie haben möglicherweise zu wenig getrunken."
							}, {
								"lang": 1,
								"text": "You probably haven't drunken enough."
							}
						]
					}, {
						"id": 1,
						"short_desc": [
							{
								"lang": 0,
								"text": "Lungenentzündung"
							}, {
								"lang": 1,
								"text": "Pneumonia"
							}
						],
						"description": [
							{
								"lang": 0,
								"text": "Sie könnten eine Lungenentzündung haben."
							}, {
								"lang": 1,
								"text": "You could suffer from Pneumonia."
							}
						]
					}, {
						"id": 2,
						"short_desc": [
							{
								"lang": 0,
								"text": "Spannungskopfschmerzen"
							}, {
								"lang": 1,
								"text": "Tension Headache"
							}
						],
						"description": [
							{
								"lang": 0,
								"text": "Sie könnten an Spannungskopfschmerzen leiden. Diese Kopfschmerzen werden durch muskuläre Verspannungen verursacht."
							}, {
								"lang": 1,
								"text": "You could suffer from tension headaches. This can be caused by muscle tension."
							}
						]
					}
				];
			},
			
			actionSuggestions: function() {
				return [
					{
						"id": 0,
						"description": [
							{
								"lang": 0,
								"text": "Behandeln Sie sich zu Hause."
							}, {
								"lang": 1,
								"text": "Stay at home."
							}
						]
					}, {
						"id": 1,
						"description": [
							{
								"lang": 0,
								"text": "Suchen Sie sofort einen Arzt auf."
							}, {
								"lang": 1,
								"text": "Go visit a doctor immediately."
							}
						]
					}, {
						"id": 2,
						"description": [
							{
								"lang": 0,
								"text": "Sie können sich selber behandeln. Nehmen Sie ein Schmerzmittel, wie Aspirin oder Dafalgan. Falls dies nicht hilft oder sich die Symptome verstärken empfehlen wir Ihnen, trotzdem einen Arzt aufzusuchen."
							}, {
								"lang": 1,
								"text": "You can treat yourself. Take an Aspirine or Dafalgan. If that doesn't seem to work, we recommend that you visit a doctor."
							}
						]
					}, {
						"id": 3,
						"description": [
							{
								"lang": 0,
								"text": "Trinken Sie mehr Wasser."
							}, {
								"lang": 1,
								"text": "Drink more water."
							}
						]
					}
				];
			},
			
			users: function() {
				return [
					{
						"id": 0,
						"name": "User123",
						"email": "user123@gmail.com",
						"password": "abcdefg",
						"gender": 0,
						"age_category": 3,
						"lang": "de"
					}, {
						"id": 1,
						"name": "Alfred",
						"email": "alfred@waynemanor.com",
						"password": "alfred",
						"gender": 0,
						"age_category": 3,
						"lang": "de"
					}
				];
			},
			
			histories: function() {
				return [
					{
						"id": 0,
						"user_id": 0,
						"timestamp": "2015-04-10 09:12:34",
						"patient": {
							"self": true /* if false, here would be more information to the "treated" pationt, like name, age, ... */
						},
						"content": [
							{
								"type": "answer",
								"id": 0,
								"answer": 0
							}, {
								"type": "answer",
								"id": 1,
								"answer": 1
							}, {
								"type": "diagnosis",
								"id": 2,
								"accepted": true
							}, {
								"type": "actionSuggestion",
								"id": 0
							}
						]
					}
				];
			},

			followUps: function() {
				return [
					{
						"id": 0,
						"user": 0,
						"oldDiagnosis": 1,
						"oldActionSuggestion": 1,
						"startQuestion": 5,
						"timeAdded": 1430985246414
					},
					{
						"id": 1,
						"user": 1,
						"oldDiagnosis": 1,
						"oldActionSuggestion": 1,
						"startQuestion": 5,
						"timeAdded": 1430985236314
					}
				];
			},
			
			languages: function() {
				return [
					{ "id": 0, "locale": "de", "nativeName": "Deutsch" },
					{ "id": 1, "locale": "en", "nativeName": "English" }
				];
			},

			ageRanges: function() {
				return [
					{ "id": 0, "start":  0, "end":  10 },
					{ "id": 1, "start": 11, "end":  25 },
					{ "id": 2, "start": 26, "end":  50 },
					{ "id": 3, "start": 51, "end":  70 },
					{ "id": 4, "start": 71, "end": 100 }
				];
			}
			
		};
		
		return DataService;
		
	});
	
})();