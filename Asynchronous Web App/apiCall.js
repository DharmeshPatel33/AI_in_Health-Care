window.onload = function(){

	//disabling the loader
	document.getElementById("loader").className = "loader1";

	//adding event listeners
	document.getElementById("form").addEventListener('submit',apiCall);	
}

function apiCall(e){

	//prevent form from submitting
	e.preventDefault();
	
	//enabling the loader
	document.getElementById("loader").className = "loader";	

	//async request
	var xhr = new XMLHttpRequest();

	//generating url
	var url = "https://intense-citadel-71493.herokuapp.com/heart/predict/";
	var params = getParams();

	//opeing the connection
	xhr.open("GET",url+params,true);

	//here, all things are defined
	xhr.onload = function(){

		document.getElementById("loader").className = "loader1";

		if(xhr.status == 200){
			console.log(xhr.responseText);
			var json = JSON.parse(xhr.responseText);

			parseResult(json);
		}
	}

	//sending the request
	xhr.send();
}

//generating params
function getParams(){
	return document.getElementById("1").value + "-" +
					document.getElementById("2").value + "-" +
					document.getElementById("3").value + "-" +
					document.getElementById("4").value + "-" +
					document.getElementById("5").value + "-" +
					document.getElementById("6").value + "-" +
					document.getElementById("7").value + "-" +
					document.getElementById("8").value + "-" +
					document.getElementById("9").value + "-" +
					document.getElementById("10").value + "-" +
					document.getElementById("11").value + "-" +
					document.getElementById("12").value + "-" +
					document.getElementById("13").value;
}

//parsing the json
function parseResult(json){

	document.getElementById("displayDiv").style.display = 'block';

	var accuracy = json['Accuracy'];
	var predictions = json['Predictions'];
	var result = json['Result'];

	accuracy_list = [
		accuracy[0]['ann'],
		accuracy[1]['knn'],
		accuracy[2]['decisionTree'],
		accuracy[3]['logisticRegression'],
		accuracy[4]['naiveBayes'],
		accuracy[5]['svm']
	];

	predictions_list = [
		predictions[0]['ann'],
		predictions[1]['knn'],
		predictions[2]['dtree'],
		predictions[3]['logisticRegression'],
		predictions[4]['naiveBayes'],
		predictions[5]['svm']
	];

	algo_list = [
		'Artificial Neural Network',
		'K-Nearest Neighbors',
		'Decision Tree',
		'Logistic Regression',
		'Naive Bayes',
		'Support Vector Machines'
	]

	//alert(accuracy_list);
	//alert(predictions_list);

	//---------------------------------------------------------------------
	
	//dynamically generating the table
	var table = document.createElement("TABLE");
	table.setAttribute("id", "result_table");
	table.setAttribute("class", "table table-dark");

	document.getElementById("answer").appendChild(table);

	var header = table.createTHead();
	var row = header.insertRow(-1);

	var th0 = row.insertCell(0);
	var th1 = row.insertCell(1);
	var th2 = row.insertCell(2);

	th0.innerHTML = "<b>ML Algorithm</b>";
	th1.innerHTML = "<b>Accuracy</b>";
	th2.innerHTML = "<b>Predictions</b>";

	for(i=0;i<6;i++){

		var acc = accuracy_list[i];
		var pred = predictions_list[i];

		var row = table.insertRow(-1);

		cell_0 = row.insertCell(0);
		cell_1 = row.insertCell(1);
		cell_2 = row.insertCell(2);
		
		cell_0.innerHTML = algo_list[i];
		cell_1.innerHTML = acc;
		cell_2.innerHTML = pred;
		
	}
	
	//---------------------------------------------------------------------
}