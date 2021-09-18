

// If absolute URL from the remote server is provided, configure the CORS
// header on that server.


var url = router.getBook(bookId);


// Loaded via <script> tag, create shortcut to access PDF.js exports.
var pdfjsLib = window['pdfjs-dist/build/pdf'];

// The workerSrc property shall be specified.
pdfjsLib.GlobalWorkerOptions.workerSrc = 'js/pdf.worker.js';

var pdfDoc = null,
	pageRendering = false,
	pageNumPending = null,
	scale = 0.8,
	canvas = document.getElementById('the-canvas'),
	ctx = canvas.getContext('2d');

let isStartPageSet = false;

/**
 * Get page info from document, resize canvas accordingly, and render page.
 * @param num Page number.
 */
function renderPage(num) {
	pageRendering = true;
	// Using promise to fetch the page
	console.log('Got page:' + num);
	pdfDoc.getPage(num).then(function(page) {
		var viewport = page.getViewport({ scale: scale });
		canvas.height = viewport.height;
		canvas.width = viewport.width;
		canvas.style.justifyItems = "center";

		// Render PDF page into canvas context
		var renderContext = {
			canvasContext: ctx,
			viewport: viewport
		};
		var renderTask = page.render(renderContext);

		// Wait for rendering to finish
		renderTask.promise.then(function() {
			pageRendering = false;
			
			if(isStartPageSet==false) {
				console.log('rendered:' + pdfDoc);
				getLastOpenedPage();
				isStartPageSet = true;
			}
			
			document.getElementById('total-pages').textContent = pdfDoc.numPages;
			pdfDoc.numPages
			
			if (pageNumPending !== null) {
				// New page rendering is pending
				renderPage(pageNumPending);
				pageNumPending = null;
			}
		});
	});

	// Update page counters
	document.getElementById('page-count').textContent = num;
}

/**
 * If another page rendering in progress, waits until the rendering is
 * finised. Otherwise, executes rendering immediately.
 */
function queueRenderPage(num) {
	if (pageRendering) {
		pageNumPending = num;
	} else {
		renderPage(num);
	}
}

/**
 * Displays previous page.
 */
function onPrevPage() {
	if (currentPage <= 1) {
		return;
	}
	currentPage--;
	queueRenderPage(currentPage);
	writePageHistory();
}
document.getElementById('prev').addEventListener('click', onPrevPage);


/**
 * Displays next page.
 */
function onNextPage() {
	if (currentPage >= pdfDoc.numPages) {
		return;
	}
	currentPage++;
	queueRenderPage(currentPage);
	writePageHistory();
}
document.getElementById('next').addEventListener('click', onNextPage);


function onSetPage(page) {
	currentPage = currentPage = parseInt(page);
	queueRenderPage(currentPage);
}

function onZoomIn() {
	if(scale>5.0)
		return;
	scale *= 1.2;
	console.log('scale:' + scale);
	queueRenderPage(currentPage);
}
document.getElementById('zoom-in').addEventListener('click', onZoomIn);


function onZoomOut() {
	if(scale<0.2)
		return;
	scale /= 1.2;
	console.log('scale:' + scale);
	queueRenderPage(currentPage);
}
document.getElementById('zoom-out').addEventListener('click', onZoomOut);


/**
 * Asynchronously downloads PDF.
 */
pdfjsLib.getDocument(url).promise.then(function(pdfDoc_) {
	pdfDoc = pdfDoc_;
	document.getElementById('page-count').textContent = pdfDoc.numPages;

	// Initial/first page rendering
	renderPage(currentPage);
});


function writePageHistory() {
	let pathToPost = router.getBookPageHistory(bookId);
	let xhr = new XMLHttpRequest();
	xhr.open("POST", pathToPost, true);
	xhr.setRequestHeader('Content-Type', 'application/json');
	xhr.send(JSON.stringify({
		page: currentPage
	}));
}