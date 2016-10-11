<%@ page isELIgnored="false"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/core" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Pict.DiiLo</title>
<link rel="icon" type="image/png"
	href="<c:out value='${serverRegularPrefix}' />favicon.png" />
<link
	href="<c:out value="${serverRegularPrefix}" />/interface/style.css"
	rel="stylesheet" type="text/css" />
<script type="text/javascript"
	src="http://maps.google.com/maps/api/js?sensor=false"></script>
</head>
<script type="text/javascript"> 
function initialize() {
    var myLatlng = new google.maps.LatLng(
    		<c:out value="${picture.latitude}" default="0"/>,
    		<c:out value="${picture.longitude}" default="0"/>);
	
	//StreetView
    var panoramaOptions = {
      position:myLatlng,
      pov: {
        heading: 165,
        pitch:0,
        zoom:1
      }
    };
    var myPano = new google.maps.StreetViewPanorama(document.getElementById("googlestreetview"), panoramaOptions);
    myPano.setVisible(true);
	
	//Maps
	 var myOptions = {
      zoom: 12,
      center: myLatlng,
      mapTypeId: google.maps.MapTypeId.ROADMAP
    }
    var map = new google.maps.Map(document.getElementById("googlemap"), myOptions);
    
    var marker = new google.maps.Marker({
        position: myLatlng, 
        map: map,
        title:"Here"
    });   
  }
</script>
<body onload="initialize()">
<div id="marginHigh"><img
	src="<c:out value='${serverRegularPrefix}' />interface/img/mainlogo.jpg" />
<a href="http://itunes.apple.com/fr/app/pict-diilo/id409361000" border="0"><img
	src="<c:out value='${serverRegularPrefix}' />interface/img/as_available_appstore_icon.png"
	alt="Available on AppStore" border="0" /></a></div>
<c:if test="${picture.vertical}">
	<div id="contentBox" class="portraitBox">
</c:if>
<c:if test="${picture.vertical == false}">
	<div id="contentBox" class="landscapeBox">
</c:if>

<div id="imageContentBox"><a class="mainPict"
	href="<c:out value='${picture.urlRegularImage}' />" target="_blank">
<img class="mainPict"
	<c:if test="${picture.vertical}">
				width="430"
				height="576"
			</c:if>
	<c:if test="${picture.vertical == false}">
				width="576"
				height="430"
			</c:if>
	src="<c:out value='${picture.urlRegularImage}' />" /> </a></div>
<div id="textContentBox">
<p class="comments"><c:out value="${picture.description}" /></p>
<p class="author"><strong><c:out
	value="${picture.nickname}" /></strong>, <fmt:formatDate type="both"
	dateStyle="short" timeStyle="short" value="${picture.date}" /> -
Hosted by ImageShack.us</p>
</div>
</div>

<c:if test="${mobility == true}">
	<div id="mobility" class="ads" align="center"><script
		type="text/javascript"><!--
			  // XHTML should not attempt to parse these strings, declare them CDATA.
			  /* <![CDATA[ */
			  window.googleAfmcRequest = {
			    client: 'ca-mb-pub-2107319658156058',
			    format: '320x50_mb',
			    output: 'html',
			    slotname: '7737624623',
			  };
			  /* ]]> */
			//--></script> <script type="text/javascript"
		src="http://pagead2.googlesyndication.com/pagead/show_afmc_ads.js"></script>
	</div>
</c:if>
<c:if test="${mobility == false}">
	<div id="desktop" class="ads" align="center"><script
		type="text/javascript"><!--
			google_ad_client = "ca-pub-2107319658156058";
			/* PictDiiLo - 728x90 - Web */
			google_ad_slot = "5408882245";
			google_ad_width = 728;
			google_ad_height = 90;
			//-->
		</script> <script type="text/javascript"
		src="http://pagead2.googlesyndication.com/pagead/show_ads.js">
		</script></div>
</c:if>
<div id="mapContentBox">
<div id="googlemap" class="gmap"></div>
<div id="googlestreetview" class="gmap"></div>
</div>
</body>
</html>
