# 🎵 SoundFlow

SoundFlow es una aplicación para gestionar audios y listas de reproducción. Permite al usuario añadir canciones, podcasts y audiolibros, crear listas personalizadas y organizar el contenido musical de forma sencilla.

Autor:José Antonio Ariza Aguilera

## 🚀 Funcionalidades

-  **Gestión de audios**:
    - Añadir canciones, podcasts y audiolibros con sus datos específicos.
    - Visualización general de todos los audios.

-  **Listas de reproducción**:
    - Crear nuevas listas.
    - Ver las canciones que contiene cada lista.
    - Añadir y eliminar audios de listas de reproducción.

## 🧱 Estructura del proyecto

El proyecto está estructurado según el patrón **Modelo-Vista-Controlador (MVC)**:

- `model/`: contiene las clases `Audio`, `Cancion`, `Podcast`, `AudioLibro`, `ListaReproduccion`, etc.
- `DAO/`: acceso a la base de datos con clases como `AudioDAO`, `RelacionListaAudioDAO`, etc.
- `viewController/`: controladores JavaFX que conectan las vistas con el modelo.
- `view/`: archivos FXML con las interfaces gráficas.

## 🛠️ Tecnologías utilizadas

-  **Java 17**
-  **JavaFX**
-  **MySQL**
-  **JDBC**
-  **FXML**

## 🗄️ Base de datos

Importar la base de datos con el archivo(SoundFlow.sql)


