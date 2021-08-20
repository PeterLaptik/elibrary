const appNavigation = Vue.createApp({
	data: function () {
		return {
			sectionsTree: sectionsTree,
		}
	},
	created: function () {
		this.fetchData();
	},

	methods: {
		onChangeData: function (sectionsTree) {
			console.log('sferferferf');
		},
		fetchData: function () {
			let xhr = new XMLHttpRequest();
			xhr.open('GET',
				router.getSectionsPath());
			xhr.send();
			xhr.onload = function () {
				if (sectionsTree.children == undefined)
				sectionsTree.children = [];

				let loadedData = JSON.parse(xhr.response);
				sectionsTree.children = loadedData.children
			}
			return true;
		}
	}
})


let selectedItem = null;
appNavigation.provide('selectedItem', selectedItem);

appNavigation.component("tree-item", {
	template: '#item-template',
	isSelected: false,
	props: {
		item: Object
	},
	data: function () {
		return {
			hover: false,
			isOpen: false,
			style: this.getStyleClass()
		};
	},
	computed: {
		isFolder: function () {
			return this.item.children && this.item.children.length;
		}
	},
	methods: {
		toggle: function () {
			this.autoUpdate();

			if (this.isFolder) {
				this.isOpen = !this.isOpen;
			}

			if (selectedItem)
				selectedItem.$forceUpdate();

			let needBooksTobeUpdated = (selectedItem != this);
			selectedItem = this;
			selectedItem.$forceUpdate();
			currentSectionId = this.item.id;
			if(needBooksTobeUpdated==true)
				updateBookList(this.item.id);
		},
		getStyleClass: function () {
			let style = "item";
			if (this.hover == true)
				style = "bold";

			if ((selectedItem) && (selectedItem.item.id == this.item.id))
				style = "selected";

			return style;
		},
		autoUpdate: function () {
			let x = this.item;
			let y = this.isFolder;
		}
	},
})

appNavigation.mount('#book-navigator')