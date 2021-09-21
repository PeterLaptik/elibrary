
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
			if (!searchChoice || !searchVal)
				return;

			let object = { 'likeVal': searchVal, 'page': searchPage };
			let path = null;
			if (searchChoice == 'book') {
				path = router.getSearchByBookName();
			} else if (searchChoice == 'author') {
				path = router.getSearchByAuthor();
			} else {
				return
			}

			let xhr = new XMLHttpRequest();
			xhr.open('POST', path);
			xhr.setRequestHeader('Content-Type', 'application/json');
			xhr.send(JSON.stringify(object));
			xhr.callBackObject = this;
			xhr.onload = function() {
				let loadedData = JSON.parse(xhr.response);
				searchList.books = loadedData.books;
				searchList.pagesNumber = loadedData.pagesNumber;
				searchList.currentPage = loadedData.currentPage;
				this.callBackObject.$forceUpdate();
				Vue.nextTick(function() {
					rebuildSearchPageList();
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
		if(this.mainState==true)
			rebuildPageList();
		else if(this.searchState==true)
			rebuildSearchPageList();
			
		updateCollapsibles();
	}
}).mount('#main-page');