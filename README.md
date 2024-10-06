# Novela

## Descripción

Este proyecto de Android desarrollado con kotline y compose tiene la funcionalidad de gestionar una colección de novelas. Permite a los usuarios agregar, eliminar, ver detalles y marcar novelas como favoritas. Además, los usuarios pueden agregar reseñas a las novelas.

## Funcionalidades

* **Agregar Novelas:** Los usuarios pueden agregar nuevas novelas a su colección, proporcionando el título, autor, año y sinopsis.
* **Eliminar Novelas:** Los usuarios pueden eliminar novelas de su colección.
* **Ver Detalles de las Novelas:** Los usuarios pueden ver los detalles de una novela, incluyendo el título, autor, año, sinopsis y reseñas.
* **Marcar Novelas como Favoritas:** Los usuarios pueden marcar novelas como favoritas para acceder a ellas fácilmente.
* **Agregar Reseñas:** Los usuarios pueden agregar reseñas a las novelas, incluyendo su nombre y el texto de la reseña.
* **Ver Reseñas:** Los usuarios pueden ver las reseñas de una novela en la pantalla de detalles.

## Estructura del Proyecto

El proyecto está organizado en los siguientes paquetes:

* **`DataBase`:** Contiene las clases de datos para las novelas y las reseñas.
* **`ui.Screens`:** Contiene las pantallas de la aplicación, como la pantalla de inicio, la pantalla de detalles de la novela, la pantalla de favoritos y la pantalla de añadir reseña.
* **`ui.AppNavegation`:** Contiene la lógica de navegación de la aplicación.

## Uso

1. Inicia la aplicación.
2. En la pantalla de inicio, puedes ver la lista de novelas.
3. Para agregar una novela, haz clic en el botón "Añadir Novela".
4. Para ver los detalles de una novela, haz clic en la novela en la lista.
5. Para marcar una novela como favorita, haz clic en el icono de corazón.
6. Para agregar una reseña a una novela, haz clic en el botón "Añadir reseña" en la pantalla de detalles de la novela.
7. Para ver las reseñas de una novela, desplázate hacia abajo en la pantalla de detalles de la novela.
8. Para volver a la pantalla anterior, haz clic en el icono de flecha hacia atrás en la barra de herramientas.

## Tecnologías Utilizadas

* Kotlin
* Jetpack Compose
* Room (para la base de datos)

