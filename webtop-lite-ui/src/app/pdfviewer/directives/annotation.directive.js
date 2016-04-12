(function() {
  // 'use strict';

  angular
    .module('pdfviewer')
    .directive('draw', function () {
		return {
		  restrict: "A",
		  link: function(scope, element){
		  	var ctx=element[0].getContext("2d");
		  	var cw=element[0].width;
		  	var ch=element[0].height;
		  	function reOffset(){
		  	  var BB = element[0].getBoundingClientRect();
		  	  offsetX = BB.left;
		  	  offsetY = BB.top;        
		  	}
		  	var offsetX,offsetY;
		  	reOffset();
		  	window.onscroll=function(e){ reOffset(); }
		  	window.onresize=function(e){ reOffset(); }

		  	var isDown=false;
		  	var startX,startY;

		  	var rects=[];
		  	var newRect;

		  	element.bind('mousedown', function(e){handleMouseDown(e);});
		  	element.bind('mousemove', function(e){handleMouseMove(e);});
		  	element.bind('mouseup', function(e){handleMouseUp(e);});
		  	element.bind('mouseout', function(e){handleMouseOut(e);});


		  	function handleMouseDown(e){
		  	  // tell the browser we're handling this event
		  	  e.preventDefault();
		  	  e.stopPropagation();

		  	  startX=parseInt(e.offsetX);
		  	  startY=parseInt(e.offsetY);

		  	  // Put your mousedown stuff here
		  	  isDown=true;
		  	}

		  	function handleMouseUp(e){
		  	  // tell the browser we're handling this event
		  	  e.preventDefault();
		  	  e.stopPropagation();

		  	  mouseX=parseInt(e.offsetX);
		  	  mouseY=parseInt(e.offsetY);

		  	  // Put your mouseup stuff here
		  	  isDown=false;

		  	  if(!willOverlap(newRect)){
		  	    rects.push(newRect);
		  	  }
		  	  drawAll();
		  	}

		  	function drawAll(){
		  	  ctx.clearRect(0, 0, element[0].width, element[0].height);
		  	  ctx.fillStyle = "rgba(255,255,0,0.4)";
		  	  for(var i=0;i<rects.length;i++){
		  	    var r=rects[i];
		  	    ctx.fillRect(r.left,r.top,r.right-r.left,r.bottom-r.top);
		  	  }
		  	}

		  	function handleMouseOut(e){
		  	  // tell the browser we're handling this event
		  	  e.preventDefault();
		  	  e.stopPropagation();

		  	  mouseX=parseInt(e.offsetX);
		  	  mouseY=parseInt(e.offsetY);

		  	  // Put your mouseOut stuff here
		  	  isDown=false;
		  	}

		  	function handleMouseMove(e){
		  	  if(!isDown){return;}
		  	  // tell the browser we're handling this event
		  	  e.preventDefault();
		  	  e.stopPropagation();

		  	  mouseX=parseInt(e.offsetX);
		  	  mouseY=parseInt(e.offsetY);

		  	  newRect={
		  	    left:Math.min(startX,mouseX),
		  	    right:Math.max(startX,mouseX),
		  	    top:Math.min(startY,mouseY),
		  	    bottom:Math.max(startY,mouseY),
		  	  }

		  	  drawAll();
		  	  ctx.fillStyle = "rgba(255,255,0,0.6)";
		  	  ctx.fillRect(startX,startY,mouseX-startX,mouseY-startY);

		  	}


		  	function willOverlap(newRect){

		  	  // shortcut to the new potential rect
		  	  var r2=newRect;

		  	  // test if one rect is completely inside another rect
		  	  var isInside=function(rect1,rect2){
		  	    return(rect2.left>=rect1.left && 
		  	           rect2.right<=rect1.right && 
		  	           rect2.top>=rect1.top &&
		  	           rect2.bottom<=rect1.bottom);
		  	  }

		  	  // test if the new rect is overlapping any existing rect
		  	  var isOverlapping=false;
		  	  for(var i=0;i<rects.length;i++){
		  	    var r1=rects[i];
		  	    //
		  	    var isIntersecting = !(r2.left>r1.right ||
		  	                           r2.right<r1.left ||
		  	                           r2.top>r1.bottom ||
		  	                           r2.bottom<r1.top);
		  	    //
		  	    var isContained= isInside(r1,r2) || isInside(r2,r1);
		  	    //
		  	    if(isIntersecting || isContained){
		  	      isOverlapping=true;
		  	    }
		  	  }
		  	  return(isOverlapping);
		  	}
		  }
		};
	});

})();