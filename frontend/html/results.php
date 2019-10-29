<?php
include 'config/settings.php';
$Country = "";
if (isset($_POST['CountryVal'])){
    $Country=$_POST['CountryVal'];
    $Country = urlencode($Country);
}

$service_url = $base_service_url . '/dbinfo';
if (strlen($Country) > 0){
    $service_url = $service_url . "/getCountry/" . $Country;
}
$curl = curl_init($service_url);
curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
$curl_response = curl_exec($curl);
//echo $curl_response;

$tHeader = "";
$tHeader = $tHeader . "<table>";
$tHeader = $tHeader . "<thead>";
$tHeader = $tHeader . "<tr class='row100 head'>";
$tHeader = $tHeader . "<th class='cell100 column1'>Country</th>";
$tHeader = $tHeader . "<th class='cell100 column2 text-right'>Population 2019</th>";
$tHeader = $tHeader . "<th class='cell100 column3 text-right'>GDP (IMF)</th>";
$tHeader = $tHeader . "<th class='cell100 column4 text-right'>GDP (UN16)</th>";
$tHeader = $tHeader . "<th class='cell100 column5 text-right'>GDP Per Capita</th>";
$tHeader = $tHeader . "</tr>";
$tHeader = $tHeader . "</thead>";
$tHeader = $tHeader . "</table>";

$decoded = json_decode($curl_response);
//$search = $decoded->body->results->search;

$output = "<table>";
$output = $output . "<tbody>";
$someArray = $decoded->body->results->rows;
    foreach ($someArray as $key => $value) {
        $output = $output . "<tr  class='row100 body'>";
        $output = $output . "<td class='cell100 column1'>" . $value[0] . "</td>";
        $output = $output . "<td class='cell100 column2 text-right'>" . $value[1] . "</td>";
        $output = $output . "<td class='cell100 column3 text-right'>" . $value[2] . "</td>";
        $output = $output . "<td class='cell100 column4 text-right'>" . $value[3] . "</td>";
        $output = $output . "<td class='cell100 column5 text-right'>" . $value[3] . "</td>";
        $output = $output . "</tr>";
    }
    $output = $output . "</tbody>";
    $output = $output . "</table>";
?>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>Demo App - Results</title>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
<!--===============================================================================================-->	
	<link rel="icon" type="image/png" href="images/icons/favicon.ico"/>
<!--===============================================================================================-->
	<link rel="stylesheet" type="text/css" href="vendor/bootstrap/css/bootstrap.min.css">
<!--===============================================================================================-->
	<link rel="stylesheet" type="text/css" href="fonts/font-awesome-4.7.0/css/font-awesome.min.css">
<!--===============================================================================================-->
	<link rel="stylesheet" type="text/css" href="vendor/animate/animate.css">
<!--===============================================================================================-->
	<link rel="stylesheet" type="text/css" href="vendor/select2/select2.min.css">
<!--===============================================================================================-->
	<link rel="stylesheet" type="text/css" href="vendor/perfect-scrollbar/perfect-scrollbar.css">
<!--===============================================================================================-->
	<link rel="stylesheet" type="text/css" href="css/util.css">
	<link rel="stylesheet" type="text/css" href="css/main.css">
<!--===============================================================================================-->
</head>
<body>
	
	<div class="limiter">
		<div class="container-table100">
			<div class="wrap-table100">
				<div class="table100 ver1 m-b-110">
					<div class="table100-head">
            <?php echo $tHeader ?>
					</div>

					<div class="table100-body js-pscroll">
            <?php echo $output ?>
					</div>
				</div>
				

					</div>
				</div>
			</div>
		</div>
	</div>


<!--===============================================================================================-->	
	<script src="vendor/jquery/jquery-3.2.1.min.js"></script>
<!--===============================================================================================-->
	<script src="vendor/bootstrap/js/popper.js"></script>
	<script src="vendor/bootstrap/js/bootstrap.min.js"></script>
<!--===============================================================================================-->
	<script src="vendor/select2/select2.min.js"></script>
<!--===============================================================================================-->
	<script src="vendor/perfect-scrollbar/perfect-scrollbar.min.js"></script>
	<script>
		$('.js-pscroll').each(function(){
			var ps = new PerfectScrollbar(this);

			$(window).on('resize', function(){
				ps.update();
			})
		});
			
		
	</script>
<!--===============================================================================================-->
	<script src="js/main.js"></script>

</body>
</html>