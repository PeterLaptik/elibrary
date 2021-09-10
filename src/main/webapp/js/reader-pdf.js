let driver = (function() {
    let currentPageIndex = 0;
    let pageMode = 1;
    let cursorIndex = Math.floor(currentPageIndex / pageMode);
    let pdfInstance = null;
    let totalPagesCount = 0;
	let pdfScale = 1;
	let pagination = true;
  
    const viewport = document.querySelector("#viewport");
    window.initPDFViewer = function(pdfURL) {
      pdfjsLib.getDocument(pdfURL).then(pdf => {
        pdfInstance = pdf;
        totalPagesCount = pdf.numPages;
        initPager();
        initPageMode();
        render();
      });
    };
  
    function onPagerButtonsClick(event) {
      const action = event.target.getAttribute("data-pager");
      if (action === "prev") {
		if(!pagination)
			return;
			
        if (currentPageIndex === 0) {
          return;
        }
        currentPageIndex -= pageMode;
        if (currentPageIndex < 0) {
          currentPageIndex = 0;
        }
		writePageHistory(currentPageIndex);
        render();
      }
      if (action === "next") {
		if(!pagination)
			return;
        if (currentPageIndex === totalPagesCount - 1) {
          return;
        }
        currentPageIndex += pageMode;
        if (currentPageIndex > totalPagesCount - 1) {
          currentPageIndex = totalPagesCount - 1;
        }
		writePageHistory(currentPageIndex);
        render();
      }
		if (action === "zoom-in") {
			console.log('zoom in:' + pdfScale);
			pdfScale = pdfScale + 0.25;
			render();
		}
		if (action === "zoom-out") {
			if (pdfScale <= 0.25) {
               return;
            }
			console.log('zoom out:' + pdfScale);
            pdfScale = pdfScale - 0.25;
			render();
		}
		if (action === "pagination") {
			pagination = !pagination;
			pageMode = 1;
			render();
		}
    }

	function writePageHistory(pageNumber){
		let pathToPost = router.getBookPageHistory(bookId);
		let xhr = new XMLHttpRequest();
		xhr.open("POST", pathToPost, true);
		xhr.setRequestHeader('Content-Type', 'application/json');
			xhr.send(JSON.stringify({
			    page: pageNumber
		}));
	}

    function initPager() {
      const pager = document.querySelector("#pager");
      pager.addEventListener("click", onPagerButtonsClick);
      return () => {
        pager.removeEventListener("click", onPagerButtonsClick);
      };
    }
	
    function onPageModeChange(event) {
	  if(!pagination)
		return;
		
      pageMode = Number(event.target.value);
      render();
    }

    function initPageMode() {
      const input = document.querySelector("#page-mode input");
      input.setAttribute("max", 4);
      input.addEventListener("change", onPageModeChange);
      return () => {
        input.removeEventListener("change", onPageModeChange);
      };
    }
  
    function render() {
      cursorIndex = Math.floor(currentPageIndex / pageMode);
      const startPageIndex = cursorIndex * pageMode;
      const endPageIndex = pagination ? 
        (startPageIndex + pageMode < totalPagesCount
          ? startPageIndex + pageMode - 1
          : totalPagesCount - 1) : totalPagesCount - 1;
  
      const renderPagesPromises = [];
      for (let i = startPageIndex; i <= endPageIndex; i++) {
        renderPagesPromises.push(pdfInstance.getPage(i + 1));
      }
  
      Promise.all(renderPagesPromises).then(pages => {
        const pagesHTML = `<div style="width: ${
          pageMode > 1 ? "50%" : "100%"
        }"><canvas></canvas></div>`.repeat(pages.length);
        viewport.innerHTML = pagesHTML;
        pages.forEach(renderPage);
      });
    }
  
    function renderPage(page) {
		console.log('rendering...');
      let pdfViewport = page.getViewport(pdfScale);
  
      const container =
        viewport.children[page.pageIndex - cursorIndex * pageMode];
      pdfViewport = page.getViewport(container.offsetWidth / pdfViewport.width);
      const canvas = container.children[0];
      const context = canvas.getContext("2d");
      canvas.height = pdfViewport.height;
      canvas.width = pdfViewport.width;
	  canvas.style.display = "block";
  
      page.render({
        canvasContext: context,
        viewport: pdfViewport
      });
    }

	return {
		setPage: function(page){
			currentPageIndex = page;
			render();
		}
	}
  })();