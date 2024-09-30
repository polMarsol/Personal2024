import sqlite3

# Conectar a la base de datos
conn = sqlite3.connect('clima_espacial.db')
c = conn.cursor()

# Consultar todos los datos
c.execute('SELECT * FROM viento_solar')
resultados = c.fetchall()

print("Datos almacenados en la base de datos:")
for fila in resultados:
    print(f"ID: {fila[0]}, Fecha: {fila[1]}, Velocidad: {fila[2]} km/s, Densidad: {fila[3]} p/cm³")

# Cerrar la conexión a la base de datos
conn.close()
