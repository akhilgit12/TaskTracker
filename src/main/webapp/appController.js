(function(){
	var app = angular.module("ToDoApp");
	app.controller('TodoCtrl', function(TaskList){
		var self = this;
		self.date = new Date();
		self.dateLong = self.date.setHours(0,0,0,0);
		self.successPercent = 0;
		self.loadTasks = function(){
			TaskList.getTaskList(self.dateLong)
			.then(function(response){
				self.toDoList = response.tasksData;
				self.successPercent = response.percentage;
				console.log(self.toDoList);
			});
		};
		self.loadTasks();
		self.inEditMode = false;
		self.editMode = "Edit";
		self.addToList = function(){
			var taskData = {"task":self.newTask.taskName,"date":self.dateLong,"striked":false};
			self.newTask.taskName = "";
			TaskList.addTask(taskData)
				.then(function(response){
					self.loadTasks();
					self.successMsg = response;
				})
				.then(function(response){
					self.errorMsg = response;
				});
		};
		self.toggleTool = function(task){
			if(task.inEditMode){
				self.editMode = "Edit";
				task.inEditMode = false;
				TaskList.updateTask(task)
					.then(function(response){
						console.log(response);
					});
			}
			else{
				task.inEditMode = true;
				self.editMode = "Done";
			}
		};
		self.deleteTask = function(index){
			self.toDoList.splice(index,1);
		};
		self.strikeTask = function(selectedTask,index){
			console.log(selectedTask);
			TaskList.strikeTask(selectedTask)
				.then(function(response){
					console.log("successfully striked");
					self.successMsg = response;
					if(!self.toDoList[index].striked){
						self.successPercent = Math.round(self.successPercent + (100/self.toDoList.length));
					}
					else{
						self.successPercent = Math.round(self.successPercent - (100/self.toDoList.length));
					}
					self.toDoList[index].striked = !self.toDoList[index].striked;
				})
				.then(function(response){
					console.log("Error in updating");
					self.errorMsg = response;	
				})
		};
		self.dateChanged = function(){
			if(self.date != undefined){
				self.dateLong = self.date.setHours(0,0,0,0);
			}
			else{
				alert("choose a valid date");
			}
			self.loadTasks();
			/*TaskList.getTaskList(self.dateLong)
				.then(function(response){
					self.toDoList = response;
				})*/
		};
		self.liClass = function(striked){
			if(striked){
				return "list-group-item list-group-item-success";
			}
			else{
				return "list-group-item list-group-item-warning";
			}
		}
	});
})();