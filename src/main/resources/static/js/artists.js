const artistList = document.getElementById("artist-list");
const loadMoreButton = document.getElementById("load-more-button");
const spinner = document.getElementById("spinner");

let currentPage = 0;
const pageSize = 3;
let totalPages = null;

async function fetchArtists(page) {
    spinner.style.display = "block";
    loadMoreButton.disabled = true;

    try {
        const response = await fetch(`/api/artists?page=${page}&size=${pageSize}`);
        const data = await response.json();

        renderArtists(data.content);

        totalPages = data.totalPages;
        currentPage = data.number;

        if (currentPage + 1 >= totalPages) {
            loadMoreButton.style.display = "none";
        }
    } catch (error) {
        console.error("Error fetching artists:", error);
    } finally {
        spinner.style.display = "none";
        loadMoreButton.disabled = false;
    }
}

function renderArtists(artists) {
    artists.forEach(artist => {
        const div = document.createElement("div");
        div.className = "artist-item";
        div.innerHTML = `<a href="/artists/${artist.id}">${artist.nickname}</a>`;
        artistList.appendChild(div);
    });
}

loadMoreButton.addEventListener("click", () => {
    fetchArtists(currentPage + 1);
});

fetchArtists(currentPage);
