# Novela

## Enlace al Proyecto

Puedes acceder al código fuente completo del proyecto en GitHub a través del siguiente enlace:

[Repositorio en GitHub](https://github.com/dxn1l/DMR3A-Novelas.git)

## Descripción

Este proyecto de Android, desarrollado con Kotlin, Jetpack Compose y Firebase, permite gestionar una colección de novelas. Los usuarios pueden agregar, editar, eliminar, ver detalles, marcar novelas como favoritas y agregar reseñas. Las novelas y reseñas se gestionan utilizando Firebase Realtime Database.

## Funcionalidades

- **Agregar Novelas:** Los usuarios pueden agregar nuevas novelas proporcionando título, autor, año y sinopsis, con validación de datos.
- **Editar Novelas:** Los usuarios pueden editar la información de una novela existente.
- **Eliminar Novelas:** Los usuarios pueden eliminar novelas de su colección.
- **Ver Detalles de las Novelas:** Los usuarios pueden ver detalles completos de una novela, incluyendo reseñas asociadas.
- **Marcar como Favorita:** Los usuarios pueden marcar novelas como favoritas para un acceso rápido en una pantalla dedicada.
- **Agregar y Ver Reseñas:** Los usuarios pueden agregar reseñas a las novelas, así como ver todas las reseñas en la pantalla de detalles.
- **Eliminar Reseñas:** Los usuarios pueden eliminar reseñas directamente desde la lista de reseñas.

## Estructura del Proyecto

El proyecto está organizado en los siguientes paquetes:

- **`database`:** Contiene las clases para gestionar las operaciones con Firebase, como `FirebaseNovelRepository`.
- **`ui.screens`:** Contiene las pantallas principales de la aplicación:
    - **Pantalla de inicio** (muestra la lista de novelas y permite agregar nuevas).
    - **Pantalla de favoritos** (muestra las novelas marcadas como favoritas).
    - **Pantalla de detalles** (muestra los detalles de una novela y las reseñas).
    - **Pantalla para agregar/editar reseñas** y **novelas**.
- **`ui.components`:** Contiene los componentes reutilizables como los ítems de novela y reseña.
- **`navigation`:** Contiene la lógica de navegación entre las diferentes pantallas de la app.

## Uso

1. Inicia la aplicación.
2. En la pantalla de inicio, podrás ver la lista de novelas.
3. Para agregar una nueva novela, haz clic en el botón "Añadir Novela".
4. Para editar una novela, haz clic en el botón de edición en la lista o en la pantalla de detalles.
5. Para ver los detalles de una novela, selecciona la novela de la lista.
6. Para marcar una novela como favorita, utiliza el icono de corazón en la pantalla de detalles o en la lista.
7. Para agregar o eliminar reseñas, navega a la pantalla de detalles de la novela y utiliza los botones correspondientes.

## Tecnologías Utilizadas

- Kotlin
- Jetpack Compose
- Firebase Realtime Database
- Firebase Authentication (si corresponde)
- Room (anteriormente utilizado para la base de datos local)


