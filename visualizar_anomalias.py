import sqlite3
import matplotlib.pyplot as plt

# Conectar a la base de datos de anomalías
conn = sqlite3.connect('anomalias_viento_solar.db')
c = conn.cursor()

# Consultar anomalías
c.execute('SELECT tipo, valor FROM anomalias')
anomalies = c.fetchall()

# Cerrar la conexión
conn.close()

# Separar los datos en listas
tipos = [anomaly[0] for anomaly in anomalies]
valores = [anomaly[1] for anomaly in anomalies]

# Visualizar las anomalías
plt.figure(figsize=(10, 5))

# Crear un gráfico de barras
plt.bar(tipos, valores, color=['blue' if t == 'velocidad' else 'red' for t in tipos])

# Añadir etiquetas y título
plt.xlabel('Tipo de Anomalía')
plt.ylabel('Valor')
plt.title('Anomalías del Viento Solar')
plt.grid()

# Mostrar el gráfico
plt.tight_layout()
plt.show()
