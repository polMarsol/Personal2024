from flask import Flask, render_template
import sqlite3
import numpy as np
import pandas as pd
import plotly.express as px
import plotly.io as pio

app = Flask(__name__)

# Función para obtener datos de la base de datos
def get_data():
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

    return fechas, velocidades, densidades

# Función para combinar listas
def combine_lists(fechas, velocidades, densidades):
    return zip(fechas, velocidades, densidades)

# Función para crear gráficos
def create_plot(fechas, velocidades, densidades):
    df = pd.DataFrame({
        'Fecha': pd.to_datetime(fechas),
        'Velocidad': velocidades,
        'Densidad': densidades
    })

    # Gráfico de Velocidades
    fig1 = px.line(df, x='Fecha', y='Velocidad', title='Velocidad del Viento Solar')
    plot_velocidades = pio.to_html(fig1, full_html=False)

    # Gráfico de Densidades
    fig2 = px.line(df, x='Fecha', y='Densidad', title='Densidad del Viento Solar')
    plot_densidades = pio.to_html(fig2, full_html=False)

    return plot_velocidades, plot_densidades

@app.route('/')
def index():
    fechas, velocidades, densidades = get_data()
    combined_data = combine_lists(fechas, velocidades, densidades)
    plot_velocidades, plot_densidades = create_plot(fechas, velocidades, densidades)
    return render_template('index.html', combined_data=combined_data, plot_velocidades=plot_velocidades, plot_densidades=plot_densidades)

if __name__ == '__main__':
    app.run(debug=True)
