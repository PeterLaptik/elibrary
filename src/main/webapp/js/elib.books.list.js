
const app = Vue.createApp({
    data() {
        return { 
				bookList: bookList, 
				historyList: historyList,
				searchList: searchList,
				mainState: true,
				historyState: false,
				searchState: false
		}
    },

    methods:{
        updateBooks: function(id, page) {
            let xhr = new XMLHttpRequest();
            xhr.open('GET',
            router.getBooksPage(id, page));
            xhr.send();
            xhr.callBackObject = this;
            xhr.onload = function () {
                let loadedData = JSON.parse(xhr.response);
                bookList.books = loadedData.books;
                bookList.pagesNumber = loadedData.pagesNumber;
                bookList.currentPage = loadedData.currentPage;
                this.callBackObject.$forceUpdate();
                Vue.nextTick(function () {
                    rebuildPageList();
                    updateCollapsibles();
                  })
            }
        },

		searchBooks: function () {
			console.log('Start search...');
			selected = document.getElementById('search-choice').value;
			searchVal = document.getElementById('input-text').value;

			if (!selected || !searchVal)
				return;

			let object = { 'likeVal': searchVal, 'page': 0 };
			let path = null;
			if (selected == 'book') {
				path = router.getSearchByBookName();
			} else if (selected == 'author') {
				path = router.getSearchByAuthor();
			} else {
				return
			}

			console.log('Start search...' + selected + ' : ' + path);

			let xhr = new XMLHttpRequest();
			xhr.open('POST', path);
			xhr.setRequestHeader('Content-Type', 'application/json');
			xhr.send(JSON.stringify(object));
			xhr.callBackObject = this;
			xhr.onload = function() {
				console.log('Response:' + xhr.response);
				let loadedData = JSON.parse(xhr.response);
				searchList.books = loadedData.books;
				searchList.pagesNumber = loadedData.pagesNumber;
				searchList.currentPage = loadedData.currentPage;
				this.callBackObject.$forceUpdate();
				Vue.nextTick(function() {
					rebuildPageList();
					updateCollapsibles();
				})
			}
		},

		setMainState: function() {
			this.clearStates();
			this.mainState = true;
			openNav();
		},
		
		setHistoryState: function() {
			this.clearStates();
			this.historyState = true;
			this.loadHistory();
			openNav();
		},
		
		setSearchState: function() {
			this.clearStates();
			this.searchState = true;
			openNav();
		},
		
		clearStates: function() {
			this.mainState = false;
			this.historyState = false;
			this.searchState = false;
		},
		
		loadHistory: function() {
			let xhr = new XMLHttpRequest();
            xhr.open('GET', router.getBookHistory());
            xhr.send();
            xhr.callBackObject = this;
            xhr.onload = function () {
                let loadedData = JSON.parse(xhr.response);
                historyList.books = loadedData.books;
                this.callBackObject.$forceUpdate();
            }
		}
    },

	updated() {
		rebuildPageList();
	}
}).mount('#main-page');