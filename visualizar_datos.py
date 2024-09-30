import sqlite3
import matplotlib.pyplot as plt
from datetime import datetime

# Conectar a la base de datos
conn = sqlite3.connect('clima_espacial.db')
c = conn.cursor()

# Consultar los datos de la tabla
c.execute('SELECT time_tag, speed, dens FROM viento_solar ORDER BY time_tag')
data = c.fetchall()

# Cerrar la conexión
conn.close()

# Separar los datos en listas
fechas = [datetime.fromisoformat(row[0]) for row in data]
velocidades = [row[1] for row in data]
densidades = [row[2] for row in data]

# Crear gráficos
plt.figure(figsize=(14, 7))

# Gráfico de Velocidad
plt.subplot(2, 1, 1)
plt.plot(fechas, velocidades, label='Velocidad del Viento Solar', color='b')
plt.title('Velocidad del Viento Solar a lo Largo del Tiempo')
plt.xlabel('Fecha')
plt.ylabel('Velocidad (km/s)')
plt.xticks(rotation=45)
plt.grid()
plt.legend()

# Gráfico de Densidad
plt.subplot(2, 1, 2)
plt.plot(fechas, densidades, label='Densidad del Viento Solar', color='r')
plt.title('Densidad del Viento Solar a lo Largo del Tiempo')
plt.xlabel('Fecha')
plt.ylabel('Densidad (p/cm³)')
plt.xticks(rotation=45)
plt.grid()
plt.legend()

# Mostrar los gráficos
plt.tight_layout()
plt.show()
