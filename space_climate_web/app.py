from flask import Flask, render_template, request, send_file
import sqlite3
import pandas as pd
import plotly.express as px
import plotly.io as pio

app = Flask(__name__)

# Función para obtener datos de la base de datos
def get_data(start_date=None, end_date=None):
    conn = sqlite3.connect('clima_espacial.db')
    c = conn.cursor()
    
    # Consultar los datos de la tabla con rango de fechas
    if start_date and end_date:
        c.execute('SELECT time_tag, speed, dens FROM viento_solar WHERE time_tag BETWEEN ? AND ?', (start_date, end_date))
    else:
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
    fig1 = px.line(df, x='Fecha', y='Velocidad', title='Velocidad del Viento Solar', labels={'Velocidad': 'Velocidad (km/s)', 'Fecha': 'Fecha'})
    plot_velocidades = pio.to_html(fig1, full_html=False)

    # Gráfico de Densidades
    fig2 = px.line(df, x='Fecha', y='Densidad', title='Densidad del Viento Solar', labels={'Densidad': 'Densidad (p/cm³)', 'Fecha': 'Fecha'})
    plot_densidades = pio.to_html(fig2, full_html=False)

    return plot_velocidades, plot_densidades

# Función para crear un gráfico de dispersión
def create_map(fechas, velocidades):
    df = pd.DataFrame({
        'Fecha': pd.to_datetime(fechas),
        'Velocidad': velocidades
    })
    
    # Gráfico de dispersión en lugar de un mapa
    fig_map = px.scatter(
        df,
        x='Fecha',
        y='Velocidad',
        title='Velocidad del Viento Solar a lo Largo del Tiempo',
        labels={'Velocidad': 'Velocidad (km/s)', 'Fecha': 'Fecha'},
    )
    
    plot_map = pio.to_html(fig_map, full_html=False)
    return plot_map

@app.route('/', methods=['GET', 'POST'])
def index():
    if request.method == 'POST':
        start_date = request.form.get('start_date')
        end_date = request.form.get('end_date')
        fechas, velocidades, densidades = get_data(start_date, end_date)
    else:
        fechas, velocidades, densidades = get_data()

    combined_data = combine_lists(fechas, velocidades, densidades)
    plot_velocidades, plot_densidades = create_plot(fechas, velocidades, densidades)
    plot_map = create_map(fechas, velocidades)
    
    return render_template('index.html', combined_data=combined_data, plot_velocidades=plot_velocidades, plot_densidades=plot_densidades, plot_map=plot_map)

@app.route('/export', methods=['GET', 'POST'])
def export_data():
    if request.method == 'POST':
        start_date = request.form.get('start_date')
        end_date = request.form.get('end_date')
        fechas, velocidades, densidades = get_data(start_date, end_date)

        # Crear un DataFrame para exportar
        df = pd.DataFrame({
            'Fecha': fechas,
            'Velocidad (km/s)': velocidades,
            'Densidad (p/cm³)': densidades
        })

        # Guardar el archivo CSV
        file_path = 'datos_viento_solar.csv'
        df.to_csv(file_path, index=False)

        # Enviar el archivo al usuario
        return send_file(file_path, as_attachment=True)

if __name__ == '__main__':
    app.run(debug=True)
