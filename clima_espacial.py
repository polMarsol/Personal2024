# Title: Clima Espacial
import requests
import sqlite3

# Conectar a la base de datos (se crea si no existe)
conn = sqlite3.connect('clima_espacial.db')
c = conn.cursor()

# Crear una tabla si no existe
c.execute('''
    CREATE TABLE IF NOT EXISTS viento_solar (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        time_tag TEXT NOT NULL,
        speed REAL NOT NULL,
        dens REAL NOT NULL
    )
''')
conn.commit()

# Nueva URL de la API para datos del viento solar (ACE SWEPAM)
url = 'https://services.swpc.noaa.gov/json/ace/swepam/ace_swepam_1h.json'

# Hacer la solicitud GET a la API
response = requests.get(url)

if response.status_code == 200:
    # Si la respuesta es exitosa, imprimimos los datos
    data = response.json()
    print("Datos actuales de viento solar:")
    
    for item in data:
        fecha = item.get('time_tag', 'N/A')
        velocidad = item.get('speed', None)  # Cambiar 'N/A' a None para mejor manejo
        densidad = item.get('dens', None)

        # Verificamos que los valores sean válidos antes de la inserción
        if velocidad is not None and densidad is not None:
            try:
                # Convertimos a float para asegurarnos de que se puede insertar
                velocidad = float(velocidad)
                densidad = float(densidad)

                # Insertar los datos en la base de datos
                c.execute('INSERT INTO viento_solar (time_tag, speed, dens) VALUES (?, ?, ?)',
                          (fecha, velocidad, densidad))
                conn.commit()  # Confirmar la transacción

                # Formateamos densidad para mostrarla en p/cm³
                densidad_formateada = f"{densidad:.2f}"
                print(f"Fecha: {fecha}, Velocidad: {velocidad} km/s, Densidad: {densidad_formateada} p/cm³")
            except ValueError as ve:
                print(f"Error al convertir valores para la fecha {fecha}: {ve}")
        else:
            print(f"Datos no disponibles para la fecha: {fecha}. Velocidad: {velocidad}, Densidad: {densidad}")
else:
    print(f"Error al obtener datos: {response.status_code}")

# Cerrar la conexión a la base de datos
conn.close()
