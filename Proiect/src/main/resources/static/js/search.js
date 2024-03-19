const searchInput = document.getElementById('search-bar');
const list = document.getElementById('list');
const listItems = list.getElementsByTagName('li');

searchInput.addEventListener('keyup', function (event) {
    const searchQuery = event.target.value.toLowerCase();

    for (let i = 0; i < listItems.length; i++) {
        const currentItem = listItems[i].textContent.toLowerCase();

        if (currentItem.includes(searchQuery)) {
            listItems[i].style.display = 'block';
        } else {
            listItems[i].style.display = 'none';
        }
    }
}
);