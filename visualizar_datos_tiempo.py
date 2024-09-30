import sqlite3
import numpy as np
import matplotlib.pyplot as plt

# Conectar a la base de datos
conn = sqlite3.connect('clima_espacial.db')
c = conn.cursor()

# Consultar los datos de la tabla
c.execute('SELECT time_tag, speed, dens FROM viento_solar')
data = c.fetchall()

# Cerrar la conexión
conn.close()

# Separar los datos en listas
fechas = [row[0] for row in data]
velocidades = [row[1] for row in data]
densidades = [row[2] for row in data]

# Convertir las fechas a un formato manejable
fechas = np.array(fechas, dtype='datetime64')

# Visualización de datos a lo largo del tiempo
plt.figure(figsize=(14, 7))

# Gráfico de Velocidades
plt.subplot(2, 1, 1)
plt.plot(fechas, velocidades, color='b', label='Velocidad (km/s)')
plt.title('Velocidad del Viento Solar a lo Largo del Tiempo')
plt.xlabel('Fecha')
plt.ylabel('Velocidad (km/s)')
plt.grid()
plt.xticks(rotation=45)

# Gráfico de Densidades
plt.subplot(2, 1, 2)
plt.plot(fechas, densidades, color='r', label='Densidad (p/cm³)')
plt.title('Densidad del Viento Solar a lo Largo del Tiempo')
plt.xlabel('Fecha')
plt.ylabel('Densidad (p/cm³)')
plt.grid()
plt.xticks(rotation=45)

plt.tight_layout()
plt.show()
