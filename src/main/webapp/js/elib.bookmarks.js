/** Bookmarks appending and deleting routines **/

function appendBookMark() {
	let modalDlg = document.getElementById("modal-dialogue");
	modalDlg.style.display = "none";
	let inputText = document.getElementById("input-text");
	if (!inputText.value)
		return;

	let obj = {
		text: inputText.value,
		page: currentPage
	};
	writeBookmark(obj);
}

let writeBookmark = function(bookmark) {
	let pathToPost = router.getBookmarksPost(bookId);
	var xhr = new XMLHttpRequest();
	xhr.open("POST", pathToPost, true);
	xhr.setRequestHeader('Content-Type', 'application/json');
	xhr.onload = function() {
		app.updateBookmarks();
	};
	xhr.send(JSON.stringify(bookmark));
}

let deleteBookmark = function(bookmark, page) {
	let delObj = {
		bookmark_text: bookmark,
		page_number: page
	};

	let pathToDelete = router.getBookmarksDelete(bookId);
	var xhr = new XMLHttpRequest();
	xhr.open("DELETE", pathToDelete, true);
	xhr.setRequestHeader('Content-Type', 'application/json');
	xhr.onload = function() {
		app.updateBookmarks();
	};
	xhr.send(JSON.stringify(delObj));
}