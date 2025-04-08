const artistList = document.getElementById("artist-list");
const loadMoreButton = document.getElementById("load-more-button");
const spinner = document.getElementById("spinner");

let currentPage = 0;
const pageSize = 3;
let totalPages = null;

async function fetchArtists(page) {
    spinner.style.display = "block"; // Show spinner
    loadMoreButton.disabled = true; // Disable button while loading

    try {
        const response = await fetch(`/api/artists?page=${page}&size=${pageSize}`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();

        renderArtists(data.content);

        totalPages = data.totalPages;
        currentPage = data.number;

        // Hide the "Load More" button if there are no more pages
        if (currentPage + 1 >= totalPages) {
            loadMoreButton.style.display = "none";
        }
    } catch (error) {
        console.error("Error fetching artists:", error);
    } finally {
        spinner.style.display = "none"; // Hide spinner
        loadMoreButton.disabled = false; // Re-enable button
    }
}

function renderArtists(artists) {
    artists.forEach(artist => {
        const div = document.createElement("div");
        div.className = "artist-card"; // Use the same class as in your CSS
        div.innerHTML = `<a href="/artists/${artist.id}" class="btn-wine">${artist.nickname}</a>`;
        artistList.appendChild(div);
    });
}

// Event listener for the "Load More" button
loadMoreButton.addEventListener("click", () => {
    fetchArtists(currentPage + 1);
});

// Initial fetch
fetchArtists(currentPage);
