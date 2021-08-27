const app = Vue.createApp({
    data() {
        return { bookList: bookList }
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
                    updateCollapsibles();
                    rebuildPageList();
                  })
            }
        }
    }
}).mount('#book-list');