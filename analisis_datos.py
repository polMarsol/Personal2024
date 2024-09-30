import sqlite3
import numpy as np
import matplotlib.pyplot as plt

# Conectar a la base de datos principal
conn = sqlite3.connect('clima_espacial.db')
c = conn.cursor()

# Consultar los datos de la tabla
c.execute('SELECT speed, dens FROM viento_solar')
data = c.fetchall()

# Cerrar la conexión
conn.close()

# Separar los datos en listas
velocidades = [row[0] for row in data]
densidades = [row[1] for row in data]

# Convertir las listas a arrays de NumPy para cálculos más fáciles
velocidades_array = np.array(velocidades)
densidades_array = np.array(densidades)

# Calcular estadísticas
velocidad_promedio = np.mean(velocidades_array)
velocidad_maxima = np.max(velocidades_array)
velocidad_minima = np.min(velocidades_array)

densidad_promedio = np.mean(densidades_array)
densidad_maxima = np.max(densidades_array)
densidad_minima = np.min(densidades_array)

# Mostrar los resultados
print("Estadísticas de Velocidad del Viento Solar:")
print(f"Promedio: {velocidad_promedio:.2f} km/s")
print(f"Máximo: {velocidad_maxima:.2f} km/s")
print(f"Mínimo: {velocidad_minima:.2f} km/s")

print("\nEstadísticas de Densidad del Viento Solar:")
print(f"Promedio: {densidad_promedio:.2f} p/cm³")
print(f"Máximo: {densidad_maxima:.2f} p/cm³")
print(f"Mínimo: {densidad_minima:.2f} p/cm³")

# Visualización de Estadísticas
labels = ['Promedio', 'Máximo', 'Mínimo']
velocidades_stats = [velocidad_promedio, velocidad_maxima, velocidad_minima]
densidades_stats = [densidad_promedio, densidad_maxima, densidad_minima]

x = np.arange(len(labels))  # el número de etiquetas

# Crear gráficos
plt.figure(figsize=(14, 7))

# Gráfico de Velocidades
plt.subplot(1, 2, 1)
plt.bar(x - 0.1, velocidades_stats, width=0.2, label='Velocidades', color='b')
plt.title('Estadísticas de Velocidad del Viento Solar')
plt.xticks(x, labels)
plt.ylabel('Velocidad (km/s)')
plt.grid()

# Gráfico de Densidades
plt.subplot(1, 2, 2)
plt.bar(x + 0.1, densidades_stats, width=0.2, label='Densidades', color='r')
plt.title('Estadísticas de Densidad del Viento Solar')
plt.xticks(x, labels)
plt.ylabel('Densidad (p/cm³)')
plt.grid()

plt.tight_layout()
plt.show()

# Calcular la desviación estándar
velocidad_std = np.std(velocidades_array)
densidad_std = np.std(densidades_array)

# Establecer umbrales para anomalías
velocidad_umbral_superior = velocidad_promedio + velocidad_std
velocidad_umbral_inferior = velocidad_promedio - velocidad_std

densidad_umbral_superior = densidad_promedio + densidad_std
densidad_umbral_inferior = densidad_promedio - densidad_std

# Identificar anomalías
anomalies_velocidad = [v for v in velocidades_array if v > velocidad_umbral_superior or v < velocidad_umbral_inferior]
anomalies_densidad = [d for d in densidades_array if d > densidad_umbral_superior or d < densidad_umbral_inferior]

# Imprimir anomalías de forma más legible
print("\nAnomalías en la Velocidad del Viento Solar:")
for anomaly in anomalies_velocidad:
    print(f"{anomaly:.2f} km/s")  # Formatear como número normal

print("\nAnomalías en la Densidad del Viento Solar:")
for anomaly in anomalies_densidad:
    print(f"{anomaly:.2f} p/cm³")  # Formatear como número normal

# Guardar anomalías en una nueva base de datos
conn_anomalias = sqlite3.connect('anomalias_viento_solar.db')
c_anomalias = conn_anomalias.cursor()

# Crear una tabla para anomalías si no existe
c_anomalias.execute('''
    CREATE TABLE IF NOT EXISTS anomalias (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        tipo TEXT NOT NULL,
        valor REAL NOT NULL
    )
''')

# Insertar anomalías de velocidad
for anomaly in anomalies_velocidad:
    c_anomalias.execute('INSERT INTO anomalias (tipo, valor) VALUES (?, ?)', ('velocidad', anomaly))

# Insertar anomalías de densidad
for anomaly in anomalies_densidad:
    c_anomalias.execute('INSERT INTO anomalias (tipo, valor) VALUES (?, ?)', ('densidad', anomaly))

# Confirmar los cambios y cerrar la conexión
conn_anomalias.commit()
conn_anomalias.close()

print("\nAnomalías guardadas en 'anomalias_viento_solar.db'.")
