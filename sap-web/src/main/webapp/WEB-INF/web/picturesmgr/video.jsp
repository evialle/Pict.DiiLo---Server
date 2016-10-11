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
<!-- Video -->
<!-- Include the VideoJS Library -->
<script src="<c:out value="${serverRegularPrefix}" />/videojs/video.js"
	type="text/javascript" charset="utf-8"></script>

<script type="text/javascript">
    // Must come after the video.js library

    // Add VideoJS to all video tags on the page when the DOM is ready
    VideoJS.setupAllWhenReady();

    /* ============= OR ============ */

    // Setup and store a reference to the player(s).
    // Must happen after the DOM is loaded
    // You can use any library's DOM Ready method instead of VideoJS.DOMReady

    /*
    VideoJS.DOMReady(function(){
      
      // Using the video's ID or element
      var myPlayer = VideoJS.setup("example_video_1");
      
      // OR using an array of video elements/IDs
      // Note: It returns an array of players
      var myManyPlayers = VideoJS.setup(["example_video_1", "example_video_2", video3Element]);

      // OR all videos on the page
      var myManyPlayers = VideoJS.setup("All");

      // After you have references to your players you can...(example)
      myPlayer.play(); // Starts playing the video for this player.
    });
    */

    /* ========= SETTING OPTIONS ========= */

    // Set options when setting up the videos. The defaults are shown here.

    /*
    VideoJS.setupAllWhenReady({
      controlsBelow: false, // Display control bar below video instead of in front of
      controlsHiding: true, // Hide controls when mouse is not over the video
      defaultVolume: 0.85, // Will be overridden by user's last volume if available
      flashVersion: 9, // Required flash version for fallback
      linksHiding: true // Hide download links when video is supported
    });
    */

    // Or as the second option of VideoJS.setup
    
    /*
    VideoJS.DOMReady(function(){
      var myPlayer = VideoJS.setup("example_video_1", {
        // Same options
      });
    });
    */

  </script>

<!-- Include the VideoJS Stylesheet -->
<link rel="stylesheet"
	href="<c:out value="${serverRegularPrefix}" />/videojs/video-js.css"
	type="text/css" media="screen" title="Video JS">
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
<a href="http://itunes.apple.com/fr/app/pict-diilo/id409361000"><img
	src="<c:out value='${serverRegularPrefix}' />interface/img/as_available_appstore_icon.png"
	alt="" /></a></div>
<c:if test="${picture.vertical}">
	<div id="contentBox" class="portraitBox">
</c:if>
<c:if test="${picture.vertical == false}">
	<div id="contentBox" class="landscapeBox">
</c:if>

<div id="imageContentBox"><!-- Begin VideoJS -->
<div class="video-js-box hu-css"><!-- Using the Video for Everybody Embed Code http://camendesign.com/code/video_for_everybody -->
<video id="example_video_1" class="video-js" width="576" height="430"
	controls="controls" preload="auto"
	poster="<c:out value='${picture.urlRegularImage}' />"> <source
	src="<c:out value='${picture.urlRegularVideo}' />" type='video/mp4' />
<!-- Flash Fallback. Use any flash video player here. Make sure to keep the vjs-flash-fallback class. -->
<object id="flash_fallback_1" class="vjs-flash-fallback" width="576"
	height="430" type="application/x-shockwave-flash"
	data="http://releases.flowplayer.org/swf/flowplayer-3.2.1.swf">
	<param name="movie"
		value="http://releases.flowplayer.org/swf/flowplayer-3.2.1.swf" />
	<param name="allowfullscreen" value="true" />
	<param name="flashvars"
		value='config={"playlist":["<c:out value='${picture.urlRegularImage}' />", {"url": "<c:out value='${picture.urlRegularVideo}' />","autoPlay":false,"autoBuffering":true}]}' />
	<!-- Image Fallback. Typically the same as the poster image. --> <img
		src="<c:out value='${picture.urlRegularImage}' />" width="576"
		height="430" alt="Poster Image"
		title="No video playback capabilities." /> </object> </video></div>
<!-- End VideoJS --></div>
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