document.getElementById('load-more').addEventListener('click', function () {
  const button = this;
  const spinner = document.getElementById('spinner');
  const artistList = document.getElementById('artist-list');
  const page = parseInt(button.getAttribute('data-page')) || 1;

  const urlParams = new URLSearchParams(window.location.search);
  const name = urlParams.get('name') || '';
  const nickname = urlParams.get('nickname') || '';
  const birthDate = urlParams.get('birthDate') || '';

  button.style.display = 'none';
  spinner.style.display = 'block';

  let url = `/artists/more/?page=${page}`;
  if (name || nickname || birthDate) {
    url = `/artists/more/?page=${page}&name=${encodeURIComponent(name)}&nickname=${encodeURIComponent(nickname)}&birthDate=${encodeURIComponent(birthDate)}`;
  }

  fetch(url)
    .then(response => {
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return response.json();
    })
    .then(artists => {
      if (artists.length === 0) {
        button.style.display = 'none';
        spinner.style.display = 'none';
        return;
      }

      artists.forEach(artist => {
        const artistHTML = `
          <div class="feat_prod_box">
            <div class="prod_det_box">
              <div class="box_top"></div>
              <div class="box_center">
                <div class="prod_title">${artist.name}</div>
                <p class="details">${artist.nickname}</p>
                <p class="birthDate">Precio: ${artist.birthDate}€</p>
                <button onclick="window.location.href='/view/${artist.id}/'">Más información</button>
                <div class="clear"></div>
              </div>
              <div class="box_bottom"></div>
            </div>
            <div class="clear"></div>
          </div>
        `;
        artistList.insertAdjacentHTML('beforeend', artistHTML);
      });

      button.setAttribute('data-page', page + 1);
      button.style.display = 'block';
      spinner.style.display = 'none';
    })
    .catch(error => {
      console.error('Error al cargar más artistas:', error);
      spinner.style.display = 'none';
      button.style.display = 'block';
    });
});