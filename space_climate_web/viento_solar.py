import sqlite3

# Conectar a la base de datos
conn = sqlite3.connect('clima_espacial.db')
c = conn.cursor()

# Crear la tabla viento_solar
c.execute('''
CREATE TABLE IF NOT EXISTS viento_solar (
    time_tag TEXT,
    speed REAL,
    dens REAL
)
''')

# Borrar datos existentes en caso de que desees empezar de cero
c.execute('DELETE FROM viento_solar')

# Insertar datos de ejemplo
data = [
    ('2024-01-01', 400.5, 5.1),
    ('2024-01-02', 450.2, 4.8),
    ('2024-01-03', 470.1, 5.5),
    ('2024-01-04', 490.3, 6.2),
    ('2024-01-05', 420.8, 4.5)
]

c.executemany("INSERT INTO viento_solar (time_tag, speed, dens) VALUES (?, ?, ?)", data)

# Guardar (commit) los cambios y cerrar la conexión
conn.commit()
conn.close()

print("Tabla creada y datos insertados correctamente.")
