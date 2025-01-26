# **Pokédex App**

Pokédex App es una aplicación móvil que permite a los usuarios explorar, capturar y gestionar información sobre Pokémon de manera intuitiva y visual. ¡Sumérgete en el mundo de Pokémon y organiza tu propia colección!

---

## **📌 Tabla de Contenidos**
- [Introducción](#introducción)
- [Características principales](#características-principales)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Tecnologías utilizadas](#tecnologías-utilizadas)
- [Capturas de pantalla](#capturas-de-pantalla)
- [Instrucciones de uso](#instrucciones-de-uso)
- [Conclusiones del desarrollador](#conclusiones-del-desarrollador)
- [Licencia](#licencia)

---

## **🔍 Introducción**
Pokédex App es una aplicación interactiva diseñada para:
- Consultar la información de los primeros 150 Pokémon.
- Gestionar una lista personalizada de Pokémon capturados, que se almacenan en la nube mediante Firebase Firestore.
- Personalizar configuraciones como idioma y opciones de interacción.

La aplicación se conecta a la [PokéAPI](https://pokeapi.co/) para obtener información actualizada sobre Pokémon.

---

## **🌟 Características principales**
1. **Autenticación**:
    - Inicio de sesión y registro mediante Firebase Authentication.
    - Opción de iniciar sesión con Google.

2. **Exploración de Pokédex**:
    - Visualización de una lista dinámica de Pokémon con imágenes y detalles básicos.
    - Detalles ampliados: tipos, índice, peso, altura y más.

3. **Gestión de Pokémon capturados**:
    - Captura Pokémon desde la Pokédex y guárdalos en una lista personalizada.
    - Elimina Pokémon capturados mediante la funcionalidad "Swipe to Delete".

4. **Configuraciones personalizables**:
    - Cambia el idioma de la aplicación (Español/Inglés).
    - Activa/desactiva la opción de eliminar Pokémon deslizando.

5. **Diseño moderno y responsivo**:
    - Implementación de Material Design para garantizar una experiencia visual atractiva.

## **🔧 Tecnologías utilizadas**

El desarrollo de esta aplicación incluye:

-   **Java**: Lenguaje principal para la lógica del proyecto.
-   **Firebase**:
    -   **Authentication**: Autenticación de usuarios.
    -   **Firestore**: Almacenamiento en la nube de los Pokémon capturados.
-   **Retrofit**: Cliente HTTP para consumir datos de la PokéAPI.
-   **Glide**: Carga y visualización de imágenes desde URL.
-   **RecyclerView**: Gestión de listas dinámicas.
-   **Navigation Component**: Navegación fluida entre fragmentos.
-   **Material Design**: Diseño moderno y consistente.
-   **Soporte Multilenguaje**: Español e Inglés.

## **📸 Capturas de pantalla**

-   **Pantalla de Inicio de Sesión**
    Permite a los usuarios autenticarse mediante correo y contraseña o con Google.
-   **Pokédex**
    Explora todos los Pokémon con detalles básicos y captura los que desees.
-   **Mis Pokémon Capturados**
    Administra tus Pokémon capturados con opción de eliminar.
-   **Ajustes**
    Configura la aplicación: idioma, eliminar deslizando, entre otros.

(Aquí deberías insertar las imágenes)

## **🚀 Instrucciones de uso**

1. **Clonar el repositorio**:

    ```bash
    git clone https://github.com/tu_usuario/tu_repositorio.git
    ```
2. **Abrir el proyecto en Android Studio**:
    -   Abre Android Studio y selecciona "Open an Existing Project".
    -   Navega hasta la carpeta del repositorio clonado.
3. **Configurar Firebase**:
    -   Crea un proyecto en Firebase Console.
    -   Activa Authentication y Cloud Firestore.
    -   Descarga el archivo `google-services.json` y colócalo en la carpeta `app/`.
4. **Ejecutar la aplicación**:
    -   Conecta un dispositivo Android o usa un emulador.
    -   Haz clic en "Run" para compilar y ejecutar.

## **📙 Conclusiones del desarrollador**

El desarrollo de Pokédex App fue una experiencia enriquecedora. Algunos puntos destacados incluyen:

-   **Integración de Firebase**: Uso de Firestore y Authentication.
-   **Consumo de APIs REST**: Implementación eficiente de Retrofit.
-   **Diseño centrado en el usuario**: Garantizar una experiencia fluida con Material Design y soporte multilenguaje.

**Desafíos superados**:

-   Gestión de cambios dinámicos de idioma.
-   Manejo de listas dinámicas con RecyclerView.
-   Resolución de problemas relacionados con la sincronización de datos en Firestore.

**Reflexión**: Este proyecto permitió aplicar conocimientos avanzados de Android y explorar nuevas tecnologías como Glide y Firebase Firestore.

## **⚖️ Licencia**

Este proyecto está disponible bajo la [Licencia MIT](Insertar enlace al archivo de licencia).

¡Gracias por explorar este proyecto! Si tienes preguntas o sugerencias, no dudes en abrir un issue o contactar al desarrollador. 😊


## **📁 Estructura del proyecto**
```plaintext
Tarea_03BLL/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/luquelafuente/bernardo/tarea_03bll/
│   │   │   │   ├── ApiClient.java              // Configuración de Retrofit
│   │   │   │   ├── LoginActivity.java          // Actividad de inicio de sesión
│   │   │   │   ├── RegisterActivity.java       // Actividad de registro
│   │   │   │   ├── MainActivity.java           // Actividad principal con navegación
│   │   │   │   ├── PokedexFragment.java        // Fragmento de la Pokédex
│   │   │   │   ├── MyPokemonsFragment.java     // Fragmento de Pokémon capturados
│   │   │   │   ├── SettingsFragment.java       // Fragmento de ajustes
│   │   │   │   ├── PokemonAdapter.java         // Adaptador para RecyclerView
│   │   │   │   ├── Pokemon.java                // Clase modelo para Pokémon
│   │   │   │   ├── PokemonApi.java             // Interface para Retrofit
│   │   │   │   ├── PokemonResponse.java        // Modelo para respuestas de la PokéAPI
│   │   │   │   ├── PokemonDetail.java          // Modelo para los detalles de Pokémon
│   │   │   ├── res/
│   │   │   │   ├── layout/                     // Archivos XML para vistas
│   │   │   │   ├── values/                     // Strings, colores y estilos
│   │   │   │   ├── drawable/                   // Recursos gráficos
│   │   │   ├── AndroidManifest.xml             // Configuración de actividades y permisos
├── build.gradle                                // Configuración de dependencias
├── README.md                                   // Archivo de documentación