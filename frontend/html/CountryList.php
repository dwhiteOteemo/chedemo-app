<?php
session_start();
$LoggedIn = false;
if (isset($_SESSION["appuser_loggedin"])){
    $LoggedIn = $_SESSION["appuser_loggedin"];
}
$CanContinue = false;
if ($LoggedIn) {
    $CanContinue = true;
}

if ($CanContinue){

    include 'config/settings.php';
	$Topic = null;
	$Service = null;
	if (isset($_GET['topic'])){
		$Topic=$_GET['topic'];
	}
	
	$service_url = "";
	$service_url = $base_service_url . '/topic/getlist/rec';	
	$curl = curl_init($service_url);
	curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
	$curl_response = curl_exec($curl);

	$decoded = json_decode($curl_response);
	$body = $decoded->Body;
	
	$TopicLookup = "";
	$topicArray = $body->Topics;
		foreach ($topicArray as $key => $value) {
            $IsSelected = "";
            $eName = str_replace("/","^",$value);
            $cName = $value;        
			if ($Topic == $eName) {
				$IsSelected = " selected";
			}
			$TopicLookup = $TopicLookup . "<option value='" . $eName . "'" . $IsSelected . ">" . $cName . "</option>";
        }
    $Years = null;
    $DataSets = null;
    if (isset($Topic)){
        $EncodedTopic = urlencode($Topic);
        $report_url = $base_service_url . "/report/bubble/time/" . $EncodedTopic;
        $curl = curl_init($report_url);
        curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
        $curl_response = curl_exec($curl);
        $decoded = json_decode($curl_response);
        $Years = $decoded->Body->Years;
        $DataSets = $decoded->Body->Results->DataSets;
    } else {
        $report_url = $base_service_url . "/report/bubble/time";
        $curl = curl_init($report_url);
        curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
        $curl_response = curl_exec($curl);
        $decoded = json_decode($curl_response);
        $Years = $decoded->Body->Years;
        $DataSets = $decoded->Body->Results->DataSets;
    }
?>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Learning</title>

    <!-- Bootstrap Core CSS -->
    <link href="../vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

    <!-- MetisMenu CSS -->
    <link href="../vendor/metisMenu/metisMenu.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="../dist/css/sb-admin-2.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="../vendor/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
    google.charts.load('current', {'packages':['corechart']});
    var timerStart = true;
    var rowData = [];
    <?php 
        foreach($DataSets as $key=>$value) {
            echo "rowData[" . $key . "] = " . json_encode($value) . ";\n";
        }
    ?>
    // <%
    // 		for (int f=0; f<dataSets.length(); f++){
    // 			out.println("rowData[" + f + "] = " + dataSets.get(f).toString() + ";");
    // 		}
    // %>
	var dNum = parseInt(0);
    function init() {
        var options = {
        		pointSize: 40,
        		pointShape: 'circle',
                width: 850,
                height: 450,
                animation:{
                    duration: 1000,
                    easing: 'out'
                  },
                chart: {
                  title: 'Recommendations per country and topic',
                },
                hAxis: {title: '', textPosition: 'none',
                		maxValue: 5, format: '0', ticks: [0, 1, 2, 3, 4]
                	},
                vAxis: {
                	title: 'Recommendations',
                    viewWindow: {
                        min: 0,
                        max: 15
                    },
                    ticks: [0, 5, 10, 15, 20, 25, 30, 35] // display labels every 25
                }
              };
        var data = [];
        <?php 
        foreach($DataSets as $key=>$value) {
            echo "data[" . $key . "] = new google.visualization.arrayToDataTable(rowData[" . $key . "]);\n";
        }
        ?>

        // <%
		// 	for (int f=0; f<dataSets.length(); f++){
		// 		out.println("data[" + f + "] = new google.visualization.arrayToDataTable(rowData[" + f + "]);");
		// 	}
		// %>
        var years = [];
        years = <?php echo json_encode($Years) ?>;
        var dMax = <?php echo count($DataSets) ?>;
        var chart = new google.visualization.ScatterChart(document.getElementById('scatterchart_material'));
        var button = document.getElementById('b1');

        function drawChart() {
        	
          // Disabling the button while the chart is drawing.
          button.disabled = true;
          google.visualization.events.addListener(chart, 'ready',
              function() {
                button.disabled = false;
              });
          /* chart.draw(data[dNum], google.charts.Scatter.convertOptions(options)); */
          chart.draw(data[dNum], options);
        }

        button.onclick = function() {
        	  dNum = dNum + 1;
        	  if (dNum > dMax-1){
        		  dNum = 0;
        	  }
        	  document.getElementById("yName").textContent = years[dNum];        	  
          drawChart();
        }
        drawChart();
      }
    
    setInterval(myMethod, 3000);

    function myMethod( )
    {
    		if (timerStart == true){
    			document.getElementById('b1').click();	
    		}
    }
    function stopTimer(){
    		if (timerStart == true){
    			timerStart = false;	
    			document.getElementById('b3').classList.remove("btn-info");
    			document.getElementById('b3').classList.add("btn-success");
    			document.getElementById('b3').textContent = "Play";
    		} else {
    			timerStart = true;
    			document.getElementById('b3').classList.remove("btn-success");
    			document.getElementById('b3').classList.add("btn-info");
    			document.getElementById('b3').textContent = "Pause";
    		}
    		
    }
    </script>

</head>

<body onLoad="init()">

    <div id="wrapper">

        <!-- Navigation -->
        <?php include 'include/navigation.php';?>
        <!-- Page Content -->
        <div id="page-wrapper">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-lg-12">
                        <h1 class="page-header" id="yName">1999</h1>
	                    <form name="topicForm" method="get" action="bubble1.php">
		                    <select name="topic" onChange="document.topicForm.submit()">
		                    		<option value="">Select Topic</option>
		                    		<?php echo $TopicLookup ?>
		                    </select>
	                    </form>
                        <div id="scatterchart_material" style="width: 800px; height: 400px;"></div>
                    </div>
                    <!-- /.col-lg-12 -->
                </div>
                <!-- /.row -->
                <div class="row">
                    <div class="col-lg-12">
                    		<div style="width: 10px">&nbsp;</div>
                        <div style="position: absolute;
    top: -30px;
    left: 220px;
    width: 580px;
    height: 100px;">
    &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                        							LI&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                        							LMI&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                        							UMI&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                        							HI&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
                        							&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; </div>
                    </div>
                    <div><input type="button" id="b1" value="next" style="display:none"></div>
                    <p>&nbsp;</p>
                    <button type="button" id="b3" class="btn btn-info" onClick="stopTimer()">Pause</button>
                    <!-- /.col-lg-12 -->
                </div>
                <!-- /.row -->
            </div>
            <!-- /.container-fluid -->
        </div>
        <!-- /#page-wrapper -->

    </div>
    <!-- /#wrapper -->

    <!-- jQuery -->
    <script src="../vendor/jquery/jquery.min.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="../vendor/bootstrap/js/bootstrap.min.js"></script>

    <!-- Metis Menu Plugin JavaScript -->
    <script src="../vendor/metisMenu/metisMenu.min.js"></script>

    <!-- Custom Theme JavaScript -->
    <script src="../dist/js/sb-admin-2.js"></script>

</body>

</html>
<?php
	} else {
    	include 'noaccess.php';
	}
?>
