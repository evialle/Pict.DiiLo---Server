<%@ page isELIgnored="false"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/core" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>DiiLo</title>
<link href="<c:out value="${serverRegularPrefix}" />/interface/style.css" rel="stylesheet" type="text/css" />
<link rel="icon" type="image/png" href="<c:out value='${serverRegularPrefix}' />favicon.png" />
<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
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
<div id="marginHigh">
	<img src="<c:out value='${serverRegularPrefix}' />interface/img/logoPictDiiLo.jpg"/>
</div>
<c:if test="${picture.vertical}">
	<div id="contentBox" class="portraitBox">
</c:if>
<c:if test="${picture.vertical == false}">
	<div id="contentBox" class="landscapeBox">
</c:if>
  <div id="imageContentBox">
     <a href="<c:out value='${serverStaticPictureUrl}' />pictures/<c:out value='${picture.fileId}' /><c:out value='${suffixPictureUrl}'/>" target="_blank">
     	<img id="mainPict" src="<c:out value='${serverStaticPictureUrl}' />pictures/<c:out value='${picture.fileId}' /><c:out value='${suffixPictureUrl}'/>" />
     </a>
  </div>
  <div id="textContentBox">
    <p class="comments"><c:out value="${picture.description}" /></p>
    <p class="author"><strong><c:out value="${picture.nickname}" /></strong>, <fmt:formatDate type="both" dateStyle="short"
			timeStyle="short" value="${picture.date}" /></p>
  </div>
</div>
<div id="mapContentBox">
  <div id="googlemap" class="gmap"></div>
  <div id="googlestreetview" class="gmap"></div>
</div>
</body>
</html>
