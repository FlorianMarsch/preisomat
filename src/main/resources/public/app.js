/**
			 * You must include the dependency on 'ngMaterial' 
			 */
			 var app = angular.module('myApp', ['ngMaterial','ngRoute']);
			
			 app.config(function($mdThemingProvider) {
					$mdThemingProvider.theme('default')
						.primaryPalette('indigo')
						.accentPalette('amber')
						.dark();
				});
			 
			
			 app.config(function($locationProvider) {
				 $locationProvider.html5Mode({
					  enabled: true,
					  requireBase: false
					});
			 });
			 
			 app.config(function($routeProvider) {
				  $routeProvider
				  .when('/', {
				      templateUrl: '/template/overview.html'
				    })
				    .when('/error', {
				      templateUrl: '/template/error.html'
				    })
				    .when('/overview', {
				      templateUrl: '/template/overview.html'
				    })
				  	.when('/create', {
				      templateUrl: '/template/create-cost-centre.html'
				    })
				    .when('/:costcentreId/costs', {
				      templateUrl: '/template/cost.html'
				    })
				    .when('/:costcentreId/add', {
					      templateUrl: '/template/add.html'
					})
				    .when('/:costcentreId/show', {
					      templateUrl: '/template/show.html'
					})
				    .when('/:costcentreId/bill', {
					      templateUrl: '/template/bill.html'
					 })
					 .when('/:costcentreId/guv', {
					      templateUrl: '/template/guv.html'
					 })
					 .when('/:costcentreId/settings', {
					      templateUrl: '/template/settings.html'
					 })
				    .otherwise({  templateUrl: '/template/404.html'  });
				});
			 
			 app.controller('generalCtrl', function($scope,$timeout, $http,$location,$mdToast,$window) {
					
					
					var timeout = $timeout;
					var scope = $scope;
					var http = $http;
					var me = this;
					

					scope.costCentres = [];
					scope.loading = true;
					
					
					$http({
				 		method : 'GET',
				 		url : '/api/costCentres',
				 		responseType : 'json'
				 	}).then(function(response) {
				 		scope.loading = false;
				 		scope.costCentres=response.data;
				 	},function(error) {
				 		scope.loading = false;
				 		$location.path("/error");
				 	});
					
					
		 

					
					scope.goto = function(target){
				 		$location.path("/"+scope.costCentre.id+"/"+target);
					};
					
					scope.back = function(){
						 $window.history.back();
					};
					
					
					
					
					scope.costCentre ={};
					scope.costCentre.name = '';
					scope.costCentre.description = '';
					scope.costCentre.participants = [];
					scope.costCentre.costs = [];
					
					
					
					
					scope.addParticipant = function(){
						var participant = {};
						participant.name = '';
						participant.days = 1;
						scope.costCentre.participants.push(participant);
					};
					scope.removeParticipant = function(aParticipant){

						var newParticipants = [];
						scope.costCentre.participants.forEach(
							function(participant){
								if(participant.id !== aParticipant.id){
									newParticipants.push(participant);
								}
							});
						scope.costCentre.participants = newParticipants;
						
					};
					
					
					scope.editCost = function(cost){
						scope.new = JSON.parse(JSON.stringify(cost));
						scope.goto("add");
					};
					scope.showCost = function(cost){
						scope.new = JSON.parse(JSON.stringify(cost));
						scope.goto("show");
					};
					
					scope.newCost = function(){
						scope.new = {};
						scope.new.payer;
						scope.new.description;
						scope.new.fixcost = true;
						scope.new.participants = [];
						scope.new.price;
						scope.new.nativeCurrency="EUR";
						scope.goto("add");
					}
					
					scope.save = function(){

				 		scope.loading = true;

				 		scope.costCentre.share = undefined;
				 		scope.costCentre.chargings = undefined;
				 		scope.costCentre.costs = undefined;
						$http({
					 		method : 'POST',
					 		url : '/api/costCentres',
					 		responseType : 'json',
					 		data : scope.costCentre
					 	}).then(function(response) {
					 		scope.loading = false;
					 		scope.costCentre=response.data;
					 		localStorage.last= scope.costCentre;
					 		$location.path("/"+scope.costCentre.id+"/costs");
					 	},function(error) {
					 		scope.loading = false;
					 		$location.path("/error");
					 	});
					};
					scope.restore = function(){
						scope.costCentre = localStorage.last;
						goto("costs);
					};
		 
					scope.select = function(x){
						scope.costCentre = x;
				 		localStorage.last= scope.costCentre;
					};
					
					scope.new = {};

					scope.new.payer;
					scope.new.description;
					scope.new.fixcost = true;
					scope.new.participants = [];
					scope.new.price;
					scope.new.nativeCurrency="EUR";
					
					
					scope.guv= {payer:null,g:[],v:[]};
					scope.guv.changed= function(element){
						var me = scope.guv.changed;
						
						scope.guv= {changed:me, payer:element, person:element,g:[],v:[],charged:0, payed:0};
						console.log(scope.guv);
						scope.costCentre.costs.forEach(function(element) {
							  if(element.payer.id == scope.guv.person.id){
								  scope.guv.g.push({description:element.description, price:element.price, cost:element});
								  scope.guv.payed+=element.price;
							  }
							  if(Object.keys(element.share).includes(scope.guv.person.name)){
								  scope.guv.v.push({description:element.description, price:element.share[scope.guv.person.name], cost:element});
								  scope.guv.charged+=element.share[scope.guv.person.name];
							  }
						});
					};
					
					scope.charged = function(element){
						return scope.new.participants.some(function(participant){return element.id == participant.id});
					};
					scope.toggleCostParticipant = function(element){
						if (scope.charged(element)) {
							scope.removeFromCost(element);
						}else{
							scope.addToCost(element);
						}
					};

					scope.addToCost = function(element){
						scope.new.participants.push(element);
					};
					scope.removeFromCost = function(aParticipant){

						var newParticipants = [];
						scope.new.participants.forEach(
							function(participant){
								if(participant.id !== aParticipant.id){
									newParticipants.push(participant);
								}
							});
						scope.new.participants = newParticipants;
					};
					scope.saveCost = function(){


						scope.new.share = undefined;
				 		scope.loading = true;
						$http({
					 		method : 'POST',
					 		url : '/api/costCentres/'+scope.costCentre.id+'/costs',
					 		responseType : 'json',
					 		data : scope.new
					 	}).then(function(response) {
					 		scope.loading = false;
					 		scope.costCentre=response.data;

					 		localStorage.last= scope.costCentre;
					 		$mdToast.show(
				 			      $mdToast.simple()
				 			        .textContent('Kosten hinzugefügt!')
				 			        .position('top right')
				 			        .hideDelay(3000)
				 			    );
					 		
					 		scope.new = {};
							scope.new.payer;
							scope.new.description;
							scope.new.fixcost = true;
							scope.new.participants = [];
							scope.new.price;
							scope.new.nativeCurrency="EUR";
							scope.back()
					 	},function(error) {
					 		scope.loading = false;
					 		$location.path("/error");
					 	});
					};
					
			});