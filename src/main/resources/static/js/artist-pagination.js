let currentPage = 1;

document.addEventListener("DOMContentLoaded", function () {
    const loadMoreBtn = document.getElementById("load-more");
    const spinner = document.getElementById("spinner");

    if (loadMoreBtn) {
        loadMoreBtn.addEventListener("click", function () {
            spinner.style.display = "inline-block";

            fetch(`/artists/more?page=${currentPage}`)
                .then(response => response.json())
                .then(artists => {
                    const list = document.getElementById("artist-list");

                    artists.forEach(artist => {
                        const div = document.createElement("div");
                        div.className = "artist-card";
                        div.innerHTML = `<a href="/artists/${artist.id}" class="btn-wine">${artist.nickname}</a>`;
                        list.appendChild(div);
                    });

                    if (artists.length < 10) {
                        loadMoreBtn.style.display = "none";
                    }

                    currentPage++;
                })
                .catch(error => console.error("Error al cargar más artistas:", error))
                .finally(() => {
                    spinner.style.display = "none";
                });
        });
    }
});
