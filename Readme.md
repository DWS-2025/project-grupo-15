Cosas que voy encontrando que hay que retocar/cambiar:

- Picture.java:
 imageFilename no tiene que ser String. Y tenenmos que pasarla como parametro y validarla todavia.

 - PictureController: (POST de /picture/new)
 no hace falta pasar todo por @RequestParam, creo.

 - Artist.java:
 anadir el @ToString.


