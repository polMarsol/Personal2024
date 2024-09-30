# Crear un archivo README.md con el contenido proporcionado

readme_content = """
# Space Climate Web

## Descripción

Space Climate Web es una aplicación web diseñada para visualizar y analizar datos sobre el viento solar. La aplicación permite a los usuarios consultar datos, visualizarlos en gráficos interactivos y exportarlos a formatos como CSV para un análisis más profundo.

## Tecnologías Utilizadas

- **Flask**: Framework web ligero para Python.
- **SQLite**: Base de datos para almacenar los datos sobre el viento solar.
- **Pandas**: Para manipulación y análisis de datos.
- **Plotly**: Biblioteca para gráficos interactivos.
- **Jinja2**: Motor de plantillas para generar HTML dinámico.

## Instalación

1. **Clonar el repositorio**:

   ```bash
   git clone <URL_DEL_REPOSITORIO>
   cd space_climate_web
   ```
2. **Crear un entorno virtual (opcional pero recomendado):**

    ```python
    python -m venv venv
    source venv/bin/activate  # Para Linux/Mac
    venv\\Scripts\\activate  # Para Windows
    ```
3. **Instalar las dependencias:**

    ```bash
    pip install -r requirements.txt
    ```

4. **Crear la base de datos:**
Asegúrate de tener los datos necesarios para llenar la base de datos `clima_espacial.db`. Puedes utilizar scripts o herramientas para importar datos.


## Uso

1. **Ejecutar la aplicación:**
    
    ```bash
    python app.py
    ```

2. **Navegar en la aplicación:**

En la página principal, podrás visualizar gráficos y acceder a diferentes funciones de la aplicación.

3. **Exportar datos:**

Los usuarios pueden exportar los datos a formato CSV desde la interfaz.

## Estructura del proyecto

```plaintext
space_climate_web/
├── app.py                  # Archivo principal de la aplicación
├── clima_espacial.db       # Base de datos SQLite
├── requirements.txt        # Lista de dependencias
├── templates/              # Carpeta que contiene las plantillas HTML
│   ├── index.html          # Plantilla principal
└── static/                 # Carpeta para archivos estáticos (CSS, JavaScript, etc.)
```

## Funcionalidades Implementadas

1. **Conexión a la base de datos:**

Se establece una conexión a la base de datos SQLite clima_espacial.db para obtener datos sobre el viento solar.

2. **Visualización de datos:**

Los datos se muestran en gráficos interactivos utilizando Plotly.

3. **Exportación de datos:**

Se implementa una funcionalidad para exportar datos a formato CSV.

4. **Interfaz de usuario:**

Se utiliza Jinja2 para generar HTML dinámico y mostrar los datos en la aplicación web.


## Contribuciones
Si deseas contribuir a este proyecto, por favor haz un fork del repositorio y envía un pull request con tus cambios.

## Licencia
Este proyecto está bajo la Licencia MIT. Consulta el archivo LICENSE para más información.

## Contacto
Si tienes preguntas o sugerencias, por favor contacta a @polMarsol.