/**
 * Colour schemes
 */

const LIGHT_BODY_STYLE = "#F0F0F0";
const LIGHT_LINK_STYLE = "#818181";
const LIGHT_LINK_OVER_STYLE = "black";
const LIGHT_ACTIVE_SCHEME = "black"

const DARK_BODY_STYLE = "black";
const DARK_LINK_STYLE = "#F0F0F0";
const DARK_LINK_OVER_STYLE = "green";
const DARK_ACTIVE_SCHEME = "yellow"

let applyDarkScheme = function() {
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
	
	viewer.configure({
		theme: "dark"
	});
}

let applyLightScheme = function() {
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
	
	viewer.configure({
		theme: "light"
	});
}