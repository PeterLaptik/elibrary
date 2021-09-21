/**
 * Colour schemes
 */

const LIGHT_BODY_STYLE = "#F0F0F0";
const LIGHT_LINK_STYLE = "#818181";
const LIGHT_LINK_OVER_STYLE = "black";
const LIGHT_ACTIVE_SCHEME = "black"
const LIGHT_TEXT = "black";

const DARK_BODY_STYLE = "black";
const DARK_LINK_STYLE = "#F0F0F0";
const DARK_LINK_OVER_STYLE = "green";
const DARK_ACTIVE_SCHEME = "yellow";
const DARK_TEXT = "white";

let isDark = true;

let applyDarkScheme = function() {
	isDark = false;
	// Navigation bar
	let navBar = document.getElementById("side-nav");
	document.body.style.background = DARK_BODY_STYLE;
	navBar.style.background = DARK_BODY_STYLE;
	// Links
	let links = document.getElementsByTagName("a");
	for (var i = 0; i < links.length; i++) {
		links[i].style.color = DARK_LINK_STYLE;
		links[i].style.transition = "0.5 s";
		if (links[i].href) {
			links[i].addEventListener("mouseleave", function(event) {
				event.target.style.color = DARK_LINK_STYLE;
			})
			links[i].addEventListener("mouseover", function(event) {
				event.target.style.color = DARK_LINK_OVER_STYLE;
			})
		}
	}
	
	// Buttons
	let lbutton = document.getElementById("lightscheme-button");
	if (lbutton) {
		lbutton.style.color = DARK_LINK_STYLE;
		lbutton.style.backgroundColor = DARK_BODY_STYLE;
		lbutton.addEventListener("mouseleave", function(event) {
			event.target.style.color = DARK_LINK_STYLE;
		})
		lbutton.addEventListener("mouseover", function(event) {
			event.target.style.color = DARK_LINK_OVER_STYLE;
		})
	}

	let dbutton = document.getElementById("darkscheme-button");
	if (dbutton) {
		dbutton.style.color = DARK_LINK_STYLE;
		dbutton.style.backgroundColor = DARK_BODY_STYLE;
		dbutton.addEventListener("mouseleave", function(event) {
			event.target.style.color = DARK_ACTIVE_SCHEME;
		})
		dbutton.addEventListener("mouseover", function(event) {
			event.target.style.color = DARK_ACTIVE_SCHEME;
		})
	}
	
	document.getElementById("toolbar").style.backgroundColor = DARK_BODY_STYLE;
	document.getElementById("bookmarks-toolbar").style.backgroundColor = DARK_BODY_STYLE;
	
	let elements = document.getElementsByTagName('span');
	for(let i=0; i<elements.length; i++) {
		elements[i].style.color = DARK_TEXT;
	}
	writeCookieValue(COL_COOKIE, COL_DARK);
}

let applyLightScheme = function() {
	isDark = false;
	document.body.style.background = LIGHT_BODY_STYLE;
	// Navigation bar
	let navBar = document.getElementById("side-nav");
	navBar.style.background = LIGHT_BODY_STYLE;
	// Links
	let links = document.getElementsByTagName("a");
	for (var i = 0; i < links.length; i++) {
		links[i].style.color = LIGHT_LINK_STYLE;
		links[i].style.transition = "0.5 s";
		if (links[i].href) {
			links[i].addEventListener("mouseleave", function(event) {
				event.target.style.color = LIGHT_LINK_STYLE;
			})
			links[i].addEventListener("mouseover", function(event) {
				event.target.style.color = LIGHT_LINK_OVER_STYLE;
			})
		}
	}
	
	// Buttons
	let lbutton = document.getElementById("lightscheme-button");
	if (lbutton) {
		lbutton.style.color = LIGHT_LINK_OVER_STYLE;
		lbutton.style.backgroundColor = LIGHT_BODY_STYLE;
		lbutton.addEventListener("mouseleave", function(event) {
			event.target.style.color = LIGHT_ACTIVE_SCHEME;
		})
		lbutton.addEventListener("mouseover", function(event) {
			event.target.style.color = LIGHT_ACTIVE_SCHEME;
		})
	}

	let dbutton = document.getElementById("darkscheme-button");
	if (dbutton) {
		dbutton.style.color = LIGHT_LINK_STYLE;
		dbutton.style.backgroundColor = LIGHT_BODY_STYLE;
		dbutton.addEventListener("mouseleave", function(event) {
			event.target.style.color = LIGHT_LINK_STYLE;
		})
		dbutton.addEventListener("mouseover", function(event) {
			event.target.style.color = LIGHT_LINK_OVER_STYLE;
		})
	}
	
	document.getElementById("toolbar").style.backgroundColor = LIGHT_BODY_STYLE;
	document.getElementById("bookmarks-toolbar").style.backgroundColor = LIGHT_BODY_STYLE;
	
	let elements = document.getElementsByTagName('span');
	for(let i=0; i<elements.length; i++) {
		elements[i].style.color = LIGHT_TEXT;
	}
	writeCookieValue(COL_COOKIE, COL_LIGHT);
}

function updateColours() {
	if (isDark == false)
		applyLightScheme();
	else
		applyDarkScheme();
}