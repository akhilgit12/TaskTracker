(function(){
	var app = angular.module("ToDoApp");
	app.service('TaskList', function($http){
		var self = this;
		self.dateLong = -1;
		self.getTaskList = function(date){
			var inputDate = {"date":date};
			var promise = $http.get("http://localhost:8060/toDOlist?date="+date);
			var promise2 = promise.then(function(response){
				return response.data;
			});
			return promise2;
		};
		self.addTask = function(taskData){
			return $http.post("http://localhost:8060/addTask",taskData)
				.then(function(response){
					return response;
				});
		};
		self.strikeTask = function (task) {
			return $http.post("http://localhost:8060/strikeTask",task)
				.then(function(response){
					return response;
				});
		}
		self.updateTask = function (task) {
			return $http.post("http://localhost:8060/updateTask",task)
				.then(function(response){
					return response;
				});
		}
	});
})();