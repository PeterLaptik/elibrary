/**
 * Pages navigators for book lists
 */

let rebuildPageList = function() {
	let pageNav = document.getElementById("pageNavigator");
	if (!pageNav)
		return;

	pageNav.innerHTML = '';

	const MAX_PAGES = 10;
	const PAGE_WINDOW_MARGIN = 3;
	let pageOverflow = bookList.pagesNumber > MAX_PAGES;

	for (let i = 0; i < bookList.pagesNumber; i++) {
		let minPage = currentPage - PAGE_WINDOW_MARGIN;
		let maxPage = currentPage + PAGE_WINDOW_MARGIN;
		if ((i < minPage || i > maxPage) && i != 0 && i != bookList.pagesNumber - 1 
			&& bookList.pagesNumber>MAX_PAGES)
			continue;

		if (pageOverflow == true && i == bookList.pagesNumber - 1 && maxPage < bookList.pagesNumber - 2) {
			let dummyElem = document.createElement("a");
			dummyElem.textContent = '...';
			pageNav.appendChild(dummyElem);
		}

		let elem = document.createElement("a");
		elem.textContent = ' ' + (i + 1) + ' ';
		elem.id = i != currentPage ? 'page_link' : 'page_link_selected';
		elem.href = "#";
		elem.onclick = function(event) {
			currentPage = i;
			updateBookList(currentSectionId);
		};

		pageNav.appendChild(elem);
		if (pageOverflow == true && i == 0 && minPage > 1) {
			let dummyElem = document.createElement("a");
			dummyElem.textContent = '...';
			pageNav.appendChild(dummyElem);
		}
	}
}

let rebuildSearchPageList = function() {
	let pageNav = document.getElementById("pageSearchNavigator");
	if (!pageNav)
		return;

	pageNav.innerHTML = '';

	const MAX_PAGES = 10;
	const PAGE_WINDOW_MARGIN = 3;
	let pageOverflow = searchList.pagesNumber > MAX_PAGES;

	for (let i = 0; i < searchList.pagesNumber; i++) {
		let minPage = searchPage - PAGE_WINDOW_MARGIN;
		let maxPage = searchPage + PAGE_WINDOW_MARGIN;
		if ((i < minPage || i > maxPage) && i != 0 && i != searchList.pagesNumber - 1
			&& searchList.pagesNumber>MAX_PAGES)
			continue;

		if (pageOverflow == true && i == searchList.pagesNumber - 1 && maxPage < searchList.pagesNumber - 2) {
			let dummyElem = document.createElement("a");
			dummyElem.textContent = '...';
			pageNav.appendChild(dummyElem);
		}

		let elem = document.createElement("a");
		elem.textContent = ' ' + (i + 1) + ' ';
		elem.id = i != searchPage ? 'page_link' : 'page_link_selected';
		elem.href = "#";
		elem.onclick = function(event) {
			searchPage = i;
			updateSearchBookList(searchPage);
		};

		pageNav.appendChild(elem);
		if (pageOverflow == true && i == 0 && minPage > 1) {
			let dummyElem = document.createElement("a");
			dummyElem.textContent = '...';
			pageNav.appendChild(dummyElem);
		}
	}
}